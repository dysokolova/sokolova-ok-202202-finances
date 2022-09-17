package ru.otus.otuskotlin.sokolova.finances.app.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.controller.RabbitController
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.processor.RabbitDirectProcessorV1
import ru.otus.otuskotlin.sokolova.finances.backend.services.FinsService
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.ACCOUNT_TMP
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
    }

    val container by lazy {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser("guest", "guest")
            start()
        }
    }

    val rabbitMqTestPort: Int by lazy {
        container.getMappedPort(5672)
    }
    val config by lazy {
        RabbitConfig(
            port = rabbitMqTestPort
        )
    }
    val service = FinsService()
    val processor by lazy {
        RabbitDirectProcessorV1(
            config = config,
            processorConfig = RabbitExchangeConfiguration(
                keyIn = "in-v1",
                keyOut = "out-v1",
                exchange = exchange,
                queue = "v1-queue",
                consumerTag = "test-tag",
                exchangeType = exchangeType
            ),
            service = service
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    val mapper = ObjectMapper()

    @BeforeTest
    fun tearUp() {
        controller.start()
    }

    @Test
    fun adCreateTest() {
        val keyOut = processor.processorConfig.keyOut
        val keyIn = processor.processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, mapper.writeValueAsBytes(accountExCreate))

                runBlocking {
                    withTimeoutOrNull(265L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = mapper.readValue(responseJson, AccountCreateResponse::class.java)
                val expected = ACCOUNT_TMP

                assertEquals(expected.name, response.account?.name)
                assertEquals(expected.description, response.account?.description)
            }
        }
    }

    private val accountExCreate = with(ACCOUNT_TMP) {
        AccountCreateRequest(
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
    }

}
