package ru.otus.otuskotlin.sokolova.finances.stubs

import io.ktor.client.plugins.websocket.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import ru.otus.otuskotlin.sokolova.finances.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.sokolova.finances.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WebsocketTest {

    @Test
    fun create() {
        testApplication {
            val client = createClient {
                install(WebSockets)
            }

            client.webSocket("/ws/v1") {
                kotlin.run {
                    val incoming = incoming.receive()
                    val response = apiV1ResponseDeserialize<IResponse>((incoming as Frame.Text).readText())
                    assertIs<AccountCreateResponse>(response)
                }
                val requestObj = AccountCreateRequest(
                    requestType = "accountCreate",
                    requestId = "e022eca4-84b2-401a-b15b-0a0efa1f7f43",
                    userId = "bf2a5cb6-7811-4269-8620-a7facc145229",
                    account = AccountData(
                        name = "Тинёк-осн",
                        description = "основной счет в Тинькофф",
                        amount = "0.0"
                    ),
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    )
                )
                send(Frame.Text(apiV1RequestSerialize(requestObj)))
                val incoming = incoming.receive()
                val responseObj = apiV1ResponseDeserialize<AccountCreateResponse>((incoming as Frame.Text).readText())
                assertEquals("bf2a5cb6-7811-4269-8620-a7facc145229", responseObj.userId)
                assertEquals("Тинёк-осн", responseObj.account?.name)
            }
        }
    }


    @Test
    fun update() {
        testApplication {
            val client = createClient {
                install(WebSockets)
            }

            client.webSocket("/ws/v1") {
                kotlin.run {
                    val incoming = incoming.receive()
                    val response = apiV1ResponseDeserialize<IResponse>((incoming as Frame.Text).readText())
                    assertIs<AccountCreateResponse>(response)
                }
                val requestObj = AccountUpdateRequest(
                    requestType = "accountUpdate",
                    requestId = "e022eca4-84b2-401a-b15b-0a0efa1f7f43",
                    userId = "bf2a5cb6-7811-4269-8620-a7facc145229",
                    account = Account(
                        name = "Тинёк-осн",
                        description = "основной счет в Тинькофф",
                        amount = "0.0",
                        accountId = "1b7a5447-c1f8-4ed0-a554-8f69d1f5b315"
                    ),
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    )
                )
                send(Frame.Text(apiV1RequestSerialize(requestObj)))
                val incoming = incoming.receive()
                val responseObj = apiV1ResponseDeserialize<AccountUpdateResponse>((incoming as Frame.Text).readText())
                assertEquals("bf2a5cb6-7811-4269-8620-a7facc145229", responseObj.userId)
                assertEquals("1b7a5447-c1f8-4ed0-a554-8f69d1f5b315", responseObj.account?.accountId)
            }
        }
    }
}