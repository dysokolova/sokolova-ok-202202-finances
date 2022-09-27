package ru.otus.otuskotlin.sokolova.finances.api.v1


import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.backend.services.FinsService
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.KtorUserSession
import ru.otus.otuskotlin.sokolova.finances.common.helpers.addError
import ru.otus.otuskotlin.sokolova.finances.common.helpers.asFinsError
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.fromTransport
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.toTransport
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.toTransportAccountCreate
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.toTransportOperationDelete


suspend fun WebSocketSession.finsWsHandlerV1(
    finsService: FinsService,
    sessions: MutableSet<KtorUserSession>,
) {
    val userSession = KtorUserSession(this)
    sessions.add(userSession)

    run {
        val ctx = FinsContext(
            timeStart = Clock.System.now()
        )
        // обработка запроса на инициализацию
        outgoing.send(Frame.Text(apiV1ResponseSerialize(ctx.toTransportAccountCreate())))
    }
    incoming
        .receiveAsFlow()
        .mapNotNull { it as? Frame.Text }
        .map { frame ->
            val jsonStr = frame.readText()
            apiV1RequestDeserialize<IRequest>(jsonStr)
        }
        .flowOn(Dispatchers.IO)
        .map { request ->
            val ctx = FinsContext(
                timeStart = Clock.System.now()
            ).apply { fromTransport(request) }
            finsService.exec(ctx)
            ctx
        }
        .flowOn(Dispatchers.Default)
        .map {
            outgoing.send(Frame.Text(apiV1ResponseSerialize(it.toTransport())))
        }
        .flowOn(Dispatchers.IO)
        // Обработка исключений с завершением flow
        .catch { e ->
            if (e is ClosedReceiveChannelException) { sessions.remove(userSession) }
            else {
                val ctx = FinsContext(
                    timeStart = Clock.System.now()
                )
                ctx.addError(e.asFinsError())
                outgoing.send(Frame.Text(apiV1ResponseSerialize(ctx.toTransportAccountCreate())))
                userSession.fwSession.close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, ""))
                sessions.remove(userSession)
            }
        }
        .collect()
}
