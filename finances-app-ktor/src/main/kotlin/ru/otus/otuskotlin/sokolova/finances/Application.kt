package ru.otus.otuskotlin.sokolova.finances

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.sokolova.finances.api.config.jsonConfig
import ru.otus.otuskotlin.sokolova.finances.api.v1
import ru.otus.otuskotlin.sokolova.finances.api.v1.finsWsHandlerV1
import ru.otus.otuskotlin.sokolova.finances.backend.services.FinsService
import ru.otus.otuskotlin.sokolova.finances.common.KtorUserSession
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSettings
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.backend.repo.postgresql.RepoSQL

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = MapApplicationConfig()
        module {
            module()
        }

        connector {
            port = 8080
            host = "0.0.0.0"
        }
    }).start(true)
}

@Suppress("unused")
fun Application.module(
    settings: FinsSettings? = null,
) {
    // Generally not needed as it is replaced by a `routing`
    install(Routing)

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(WebSockets)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost() // TODO remove
    }

    install(ContentNegotiation) {
        jackson {
            jsonConfig()
        }
    }

    val corSettings by lazy {
        settings ?: FinsSettings(
            repoTest = RepoInMemory(),
            //     repoProd = RepoSQL(url = "jdbc:postgresql://postgresql:5432/financesdevdb")
            repoProd = RepoSQL()
        )
    }

    val finsService by lazy { FinsService(corSettings) }
    val sessions = mutableSetOf<KtorUserSession>()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        v1(finsService)

        static("static") {
            resources("static")
        }
        webSocket("/ws/v1") {
            finsWsHandlerV1(finsService, sessions)
        }
    }
}