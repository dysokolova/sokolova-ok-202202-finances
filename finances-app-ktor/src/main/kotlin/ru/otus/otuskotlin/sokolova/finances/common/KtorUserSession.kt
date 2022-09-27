package ru.otus.otuskotlin.sokolova.finances.common

import io.ktor.websocket.*
import ru.otus.otuskotlin.sokolova.finances.common.models.IClientSession

data class KtorUserSession(
    override val fwSession: WebSocketSession
): IClientSession<WebSocketSession>