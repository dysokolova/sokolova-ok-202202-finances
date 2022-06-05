package ru.otus.otuskotlin.sokolova.finances.springapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.sokolova.finances.backend.services.AccountService
import ru.otus.otuskotlin.sokolova.finances.backend.services.OperationService

@Configuration
class ServiceConfig {
    @Bean
    fun serviceAaccount(): AccountService = AccountService()

    @Bean
    fun serviceOperation(): OperationService = OperationService()
}
