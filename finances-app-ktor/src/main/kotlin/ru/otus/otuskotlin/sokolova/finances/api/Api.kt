package ru.otus.otuskotlin.sokolova.finances.api

import io.ktor.server.routing.*
import ru.otus.otuskotlin.sokolova.finances.api.v1.v1Account
import ru.otus.otuskotlin.sokolova.finances.api.v1.v1Operation
import ru.otus.otuskotlin.sokolova.finances.backend.services.AccountService
import ru.otus.otuskotlin.sokolova.finances.backend.services.OperationService

internal fun Routing.v1(accountService: AccountService, operationService: OperationService) {
    route("v1") {
        v1Account(accountService)
        v1Operation(operationService)
    }
}