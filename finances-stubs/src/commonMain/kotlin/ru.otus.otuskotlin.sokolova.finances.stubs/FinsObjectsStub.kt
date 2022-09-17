package ru.otus.otuskotlin.sokolova.finances.stubs

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import kotlin.math.roundToInt
import kotlin.random.Random

object FinsObjectsStub {
    val ACCOUNT_TMP: FinsAccount
        get() = FinsAccount(
            name = "Тинёк-осн",
            description = "основной счет в Тинькофф",
            amount = 120.0,
            accountId = FinsAccountId("39e75597-9c12-40dd-a3e8-540317e4fbf2")
        )
    val OPERATION_TMP: FinsOperation
        get() = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        )
    val SRCH_FILTER_TMP: FinsSrchFilter
        get() = FinsSrchFilter(
            searchFilter = "<строка поиска>"
        )
    val HIST_FILTER_TMP: FinsHistFilter
        get() = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        )

    private fun getAccount(): FinsAccount = ACCOUNT_TMP.deepCopy()
    private fun getOperation(): FinsOperation = OPERATION_TMP.deepCopy()
    private fun getSrchFilter(): FinsSrchFilter = SRCH_FILTER_TMP.copy()
    private fun getHistFilter(): FinsHistFilter = HIST_FILTER_TMP.copy()

    fun prepareUserId(): FinsUserId = FinsUserId(uuid4().toString())

    fun prepareAccountResult(block: FinsAccount.() -> Unit): FinsAccount = getAccount().apply(block)

    fun prepareSrchFilter(block: FinsSrchFilter.() -> Unit): FinsSrchFilter = getSrchFilter().apply(block)

    fun prepareHistFilter(block: FinsHistFilter.() -> Unit): FinsHistFilter = getHistFilter().apply(block)

    fun prepareOperationResult(block: FinsOperation.() -> Unit): FinsOperation = getOperation().apply(block)

    fun prepareSearchList(searchFilter: String) = listOf(
        finsAccount((uuid4().toString()), searchFilter),
        finsAccount((uuid4().toString()), searchFilter),
        finsAccount((uuid4().toString()), searchFilter),
        finsAccount((uuid4().toString()), searchFilter),
        finsAccount((uuid4().toString()), searchFilter),
        finsAccount((uuid4().toString()), searchFilter),
    )

    private fun finsAccount(accountId: String, searchFilter: String) =
        finsAccountStub(ACCOUNT_TMP, accountId = accountId, searchFilter = searchFilter)

    private fun finsAccountStub(base: FinsAccount, accountId: String, searchFilter: String) =
        base.copy(
            name = "$searchFilter $accountId",
            description = "desc $searchFilter $accountId",
            amount = Random.nextDouble(1000.0, 5000.0).roundToInt() / 100.0,
            accountId = FinsAccountId(accountId),
        )

    fun prepareHistoryList(accountId: FinsAccountId, histFilter: FinsHistFilter) = listOf(
        finsOperation(accountId, histFilter.fromDateTime, true, (uuid4().toString())),
        finsOperation(
            accountId,
            histFilter.fromDateTime + (histFilter.toDateTime - histFilter.fromDateTime) / 5,
            false,
            (uuid4().toString())
        ),
        finsOperation(
            accountId,
            histFilter.fromDateTime + (histFilter.toDateTime - histFilter.fromDateTime) * 2 / 5,
            true,
            (uuid4().toString())
        ),
        finsOperation(
            accountId,
            histFilter.fromDateTime + (histFilter.toDateTime - histFilter.fromDateTime) * 3 / 5,
            false,
            (uuid4().toString())
        ),
        finsOperation(
            accountId,
            histFilter.fromDateTime + (histFilter.toDateTime - histFilter.fromDateTime) * 4 / 5,
            true,
            (uuid4().toString())
        ),
        finsOperation(accountId, histFilter.toDateTime, false, (uuid4().toString())),
    )

    private fun finsOperation(
        accountId: FinsAccountId,
        operationDateTime: Instant,
        from: Boolean,
        operationId: String
    ) =
        finsOperationStub(
            OPERATION_TMP,
            accountId = accountId,
            operationDateTime = operationDateTime,
            from = from,
            operationId = operationId
        )

    private fun finsOperationStub(
        base: FinsOperation,
        accountId: FinsAccountId,
        operationDateTime: Instant,
        from: Boolean,
        operationId: String
    ) = base.copy(
        description = "перевод от $operationDateTime",
        amount = Random.nextDouble(1000.0, 5000.0).roundToInt() / 100.0,
        fromAccountId = (if (from) accountId else FinsAccountId(uuid4().toString())),
        toAccountId = (if (from) FinsAccountId(uuid4().toString()) else accountId),
        operationDateTime = operationDateTime,
        operationId = FinsOperationId(operationId)
    )

}
