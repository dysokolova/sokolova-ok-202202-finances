package ru.otus.otuskotlin.sokolova.finances.api.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationResponseTest {
    val createResponse = AccountCreateResponse(
        account = Account(
            userId = "1",
            name = "Тинёк-осн",
            description = "основной счет в Тинькофф",
            amount = "0.0"
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = apiV1ResponseSerialize(createResponse)
        println(jsonString)
        assertContains(jsonString, """"UserId":"1"""")
        assertContains(jsonString, """"responseType":"accountCreate"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = apiV1ResponseSerialize(createResponse)
        val decoded = apiV1ResponseDeserialize<AccountCreateResponse>(jsonString)
        println(decoded)
        assertEquals("1", decoded.account?.userId)
        assertEquals("Тинёк-осн", decoded.account?.name)
        assertEquals("основной счет в Тинькофф", decoded.account?.description)
        assertEquals("0.0", decoded.account?.amount)
    }

    @Test
    fun deserializeIResponseTest() {
        val jsonString = apiV1ResponseSerialize(createResponse)
        val decoded = apiV1ResponseDeserialize<IResponse>(jsonString) as AccountCreateResponse
        println(decoded)
        assertEquals("1", decoded.account?.userId)
        assertEquals("Тинёк-осн", decoded.account?.name)
        assertEquals("основной счет в Тинькофф", decoded.account?.description)
        assertEquals("0.0", decoded.account?.amount)
    }
}
