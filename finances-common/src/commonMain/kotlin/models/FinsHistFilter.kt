package ru.otus.otuskotlin.sokolova.finances.common.models
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE

data class FinsHistFilter(
    var fromDateTime: Instant = Instant.NONE,
    var toDateTime: Instant = Instant.NONE,
){
    fun deepCopy(
    ) = FinsHistFilter(
        fromDateTime = this@FinsHistFilter.fromDateTime,
        toDateTime = this@FinsHistFilter.toDateTime
    )

    companion object {
        val NONE = FinsHistFilter(fromDateTime = Instant.NONE, toDateTime = Instant.NONE)
    }
}
