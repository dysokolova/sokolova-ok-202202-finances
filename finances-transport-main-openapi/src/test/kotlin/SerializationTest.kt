package ru.otus.otuskotlin.sokolova.finances.api.v1

import org.junit.Test
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import kotlin.test.assertContains

class SerializationTest {

    @Test
    fun serTest() {
        val createRequest = AccountCreateRequest(
            requestId = "3395ac64-3cf3-478a-afc9-16fb64b88281",
            requestType = "accountCreate",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS,
            ),
            account = AccountData(
                userId = "1",
                name = "Тинёк-осн",
                description = "основной счет в Тинькофф"
            )
        )
        val jsonString = jacksonMapper.writeValueAsString(createRequest)
        println(jsonString)
        assertContains(jsonString, """"Name":"Тинёк-осн"""")
    }
}

