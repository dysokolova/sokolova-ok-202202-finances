package ru.otus.otuskotlin.sokolova.finances.api.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

fun ObjectMapper.jsonConfig() = apply {
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    enable(SerializationFeature.INDENT_OUTPUT)
    writerWithDefaultPrettyPrinter()
}