package ru.otus.otuskotlin.sokolova.finances.stubs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.datetime.Clock
import org.junit.Test
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperationId
import kotlin.test.assertEquals

class V1StubsApiTest {

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    @Test
    fun `account create`() = testApplication {
//        application {
//            dispose()
//            module()
//        }
//        environment {
//            config = MapApplicationConfig("ktor.deployment.port" to "8080")
//        }
        val client = myClient()

        val response = client.post("/v1/account/create") {
            val requestObj = AccountCreateRequest(
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
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AccountCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("bf2a5cb6-7811-4269-8620-a7facc145229", responseObj.userId)
    }

    @Test
    fun `account read`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/account/read") {
            val requestObj = AccountReadRequest(
                requestId = "12345",
                account = AccountId("123"),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AccountReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.account?.accountId)
    }

    @Test
    fun `account update`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/account/update") {
            val requestObj = AccountUpdateRequest(
                requestId = "e022eca4-84b2-401a-b15b-0a0efa1f7f43",
                userId = "bf2a5cb6-7811-4269-8620-a7facc145229",
                account = Account(
                    name = "Тинёк-осн",
                    description = "основной счет в Тинькофф",
                    amount = "0.0",
                    accountId = "123"
                ),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AccountUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("0.0", responseObj.account?.amount)
    }

    @Test
    fun `account delete`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/account/delete") {
            val requestObj = AccountDeleteRequest(
                requestId = "12345",
                account = AccountId("123"),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AccountDeleteResponse>()
        assertEquals(200, response.status.value)
    }

    @Test
    fun `account search`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/account/search") {
            val requestObj = AccountSearchRequest(
                requestId = "e022eca4-84b2-401a-b15b-0a0efa1f7f43",
                userId = "bf2a5cb6-7811-4269-8620-a7facc145229",
                searchFilter = SearchFilterObj("123"),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AccountSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("desc 123", responseObj.accounts?.first()?.account?.description?.substring(0,8))
    }

    @Test
    fun `account history`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/account/history") {
            val requestObj = AccountHistoryRequest(
                requestId = "12345",
                account = AccountId("9224ec32-23ad-486d-a0c4-c20e2424d36e"),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AccountHistoryResponse>()
        assertEquals(200, response.status.value)
        assertEquals("9224ec32-23ad-486d-a0c4-c20e2424d36e", responseObj.operations?.first()?.operation?.fromAccountId)
    }

    fun `operation create`() = testApplication {
//        application {
//            dispose()
//            module()
//        }
//        environment {
//            config = MapApplicationConfig("ktor.deployment.port" to "8080")
//        }
        val client = myClient()

        val response = client.post("/v1/operation/create") {
            val requestObj = OperationCreateRequest(
                userId = "1",
                requestId = "12345",
                operation = OperationData(
                    description = "перевод",
                    amount = "10.0",
                    fromAccountId = "1",
                    toAccountId = "2",
                    operationDateTime = Clock.System.now().toString()
                ),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<OperationCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.operation?.operationId)
    }

    @Test
    fun `operation read`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/operation/read") {
            val requestObj = OperationReadRequest(
                requestId = "12345",
                operation = OperationId("123"),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<OperationReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.operation?.operationId)
    }

    @Test
    fun `operation update`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/operation/update") {
            val requestObj = OperationUpdateRequest(
                requestId = "12345",
                userId = "1",
                operation = Operation(
                    description = "перевод",
                    amount = "10.0",
                    fromAccountId = "1",
                    toAccountId = "2",
                    operationDateTime = Clock.System.now().toString(),
                    operationId = "111"
                ),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<OperationUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.operation?.operationId)
    }

    @Test
    fun `operation delete`() = testApplication {
        val client = myClient()

        val response = client.post("/v1/operation/delete") {
            val requestObj = OperationDeleteRequest(
                requestId = "12345",
                operation = OperationId("123"),
                debug = Debug(
                    mode = RequestDebugMode.STUB,
                    stub = RequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<OperationDeleteResponse>()
        assertEquals(200, response.status.value)
    }

}
