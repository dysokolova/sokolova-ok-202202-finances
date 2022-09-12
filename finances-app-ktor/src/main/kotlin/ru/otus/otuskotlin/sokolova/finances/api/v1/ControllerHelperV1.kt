package ru.otus.otuskotlin.sokolova.finances.api.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.IRequest
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.IResponse
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.helpers.asFinsError
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.fromTransport
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.toTransport

suspend inline fun <reified Q : IRequest, reified R : IResponse>
        ApplicationCall.controllerHelperV1(command: FinsCommand? = null, block: FinsContext.() -> Unit) {
    val ctx = FinsContext(
        timeStart = Clock.System.now(),
    )
    try {
        val request = receive<Q>()
        ctx.fromTransport(request)
        ctx.block()
        val response = ctx.toTransport()
        respond(response)
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = FinsState.FAILING
        ctx.errors.add(e.asFinsError())
        ctx.block()
        val response = ctx.toTransport()
        respond(response)
    }
}
