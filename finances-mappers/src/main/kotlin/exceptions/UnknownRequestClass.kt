package ru.otus.otuskotlin.sokolova.finances.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>): RuntimeException("Class $clazz cannot be mapped to FinsContext")
