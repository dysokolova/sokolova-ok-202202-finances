package ru.otus.otuskotlin.sokolova.finances.api.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.sokolova.finances.backend.services.*

fun Route.v1Account(accountService: AccountService) {
    route("account") {
        post("create") {
            call.accountCreate(accountService)
        }
        post("read") {
            call.accountRead(accountService)
        }
        post("update") {
            call.accountUpdate(accountService)
        }
        post("delete") {
            call.accountDelete(accountService)
        }
        post("search") {
            call.accountSearch(accountService)
        }
        post("history") {
            call.accountHistory(accountService)
        }
    }
}

fun Route.v1Operation(operationService: OperationService) {
    route("operation") {
        post("create") {
            call.operationCreate(operationService)
        }
        post("read") {
            call.operationRead(operationService)
        }
        post("update") {
            call.operationUpdate(operationService)
        }
        post("delete") {
            call.operationDelete(operationService)
        }
    }
}
