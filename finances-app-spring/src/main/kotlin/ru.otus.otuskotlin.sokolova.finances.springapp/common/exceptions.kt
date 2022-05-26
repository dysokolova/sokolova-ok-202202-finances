package ru.otus.otuskotlin.sokolova.finances.springapp.common

val notFoundError: (String) -> String  = {
    "Not found ad by id $it"
}