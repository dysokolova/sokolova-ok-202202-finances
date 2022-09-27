package ru.otus.otuskotlin.sokolova.finances.app.rabbit.processor


import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.IRequest
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.RabbitProcessorBase
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.sokolova.finances.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.sokolova.finances.backend.services.FinsService
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.helpers.addError
import ru.otus.otuskotlin.sokolova.finances.common.helpers.asFinsError
import ru.otus.otuskotlin.sokolova.finances.common.helpers.fail
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.fromTransport
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.toTransport

class RabbitDirectProcessorV1(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val service: FinsService,
) : RabbitProcessorBase(config, processorConfig) {

    private val context = FinsContext()

    override suspend fun Channel.processMessage(message: Delivery) {
        context.apply {
            timeStart = Clock.System.now()
        }

        jacksonMapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this).also {
                println("TYPE: ${this::class.simpleName}")
            }
        }
        val response = service.exec(context).run { context.toTransport() }
        jacksonMapper.writeValueAsBytes(response).also {
            println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }.also {
            println("published")
        }
    }

    override fun Channel.onError(e: Throwable) {
        e.printStackTrace()
        context.fail(error = e.asFinsError())
        val response = context.toTransport()
        jacksonMapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}