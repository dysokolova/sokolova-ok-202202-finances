package ru.otus.otuskotlin.sokolova.finances.api

import io.ktor.server.routing.*
import ru.otus.otuskotlin.sokolova.finances.api.v1.v1Account
import ru.otus.otuskotlin.sokolova.finances.api.v1.v1Operation
import ru.otus.otuskotlin.sokolova.finances.backend.services.FinsService

internal fun Routing.v1(service: FinsService) {
    route("v1") {
        v1Account(service)
        v1Operation(service)
    }
}