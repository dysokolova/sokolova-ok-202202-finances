package ru.otus.otuskotlin.sokolova.finances.api.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.sokolova.finances.backend.services.*

fun Route.v1Account(service: FinsService) {
    route("account") {
        post("create") {
            call.accountCreate(service)
        }
        post("read") {
            call.accountRead(service)
        }
        post("update") {
            call.accountUpdate(service)
        }
        post("delete") {
            call.accountDelete(service)
        }
        post("search") {
            call.accountSearch(service)
        }
        post("history") {
            call.accountHistory(service)
        }
    }
}

fun Route.v1Operation(service: FinsService) {
    route("operation") {
        post("create") {
            call.operationCreate(service)
        }
        post("read") {
            call.operationRead(service)
        }
        post("update") {
            call.operationUpdate(service)
        }
        post("delete") {
            call.operationDelete(service)
        }
    }
}
