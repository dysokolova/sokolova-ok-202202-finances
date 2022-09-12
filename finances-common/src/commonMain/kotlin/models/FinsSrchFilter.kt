package ru.otus.otuskotlin.sokolova.finances.common.models

data class FinsSrchFilter(
    var searchFilter: String = "",
){
    fun deepCopy(
    ) = FinsSrchFilter(
        searchFilter = this@FinsSrchFilter.searchFilter
    )
}
