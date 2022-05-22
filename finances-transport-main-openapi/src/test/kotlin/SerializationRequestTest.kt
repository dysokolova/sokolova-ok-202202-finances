package ru.otus.otuskotlin.sokolova.finances.api.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationRequestTest {
    val accountCreateRequest = AccountCreateRequest(
        account = AccountData(
            userId = "1",
            name = "Тинёк-осн",
            description = "основной счет в Тинькофф"
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = apiV1RequestSerialize(accountCreateRequest)
        println(jsonString)
        assertContains(jsonString, """"UserId":"1"""")
        assertContains(jsonString, """"requestType":"accountCreate"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = apiV1RequestSerialize(accountCreateRequest)
        val decoded = apiV1RequestDeserialize<AccountCreateRequest>(jsonString)
        println(decoded)
        assertEquals("1", decoded.account?.userId)
        assertEquals("Тинёк-осн", decoded.account?.name)
        assertEquals("основной счет в Тинькофф", decoded.account?.description)
    }

    @Test
    fun deserializeIRequestTest() {
        val jsonString = apiV1RequestSerialize(accountCreateRequest)
        val decoded = apiV1RequestDeserialize<IRequest>(jsonString) as AccountCreateRequest
        println(decoded)
        assertEquals("1", decoded.account?.userId)
        assertEquals("Тинёк-осн", decoded.account?.name)
        assertEquals("основной счет в Тинькофф", decoded.account?.description)
    }
}
