package ru.otus.otuskotlin.sokolova.finances.mappers.v1.exceptions
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand

class UnknownFinsCommand(command: FinsCommand) : Throwable("Wrong command $command at mapping toTransport stage")
