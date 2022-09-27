package ru.otus.otuskotlin.sokolova.finances.app.rabbit

import ru.otus.otuskotlin.sokolova.finances.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.controller.RabbitController
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.processor.RabbitDirectProcessorV1
import ru.otus.otuskotlin.sokolova.finances.backend.services.FinsService

fun main() {
    val config = RabbitConfig()
    val service = FinsService()

    val producerConfig = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "transport-exchange",
        queue = "v1-queue",
        consumerTag = "v1-consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessorV1(
            config = config,
            processorConfig = producerConfig,
            service = service
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    controller.start()
}