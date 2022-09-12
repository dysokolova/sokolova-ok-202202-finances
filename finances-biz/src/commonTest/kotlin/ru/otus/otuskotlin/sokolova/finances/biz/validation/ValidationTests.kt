package ru.otus.otuskotlin.sokolova.finances.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.ACCOUNT_TMP
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.HIST_FILTER_TMP
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.OPERATION_TMP
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.SRCH_FILTER_TMP
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationUserIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = ACCOUNT_TMP,
        operationRequest = OPERATION_TMP,
        accountFilterRequest = SRCH_FILTER_TMP,
        accountHistoryRequest = HIST_FILTER_TMP,
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatUserIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-4dd4-9706-803983b39858"),
        accountRequest = ACCOUNT_TMP,
        operationRequest = OPERATION_TMP,
        accountFilterRequest = SRCH_FILTER_TMP,
        accountHistoryRequest = HIST_FILTER_TMP,
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("UserId", error?.field)
    assertContains(error?.message ?: "", "UserId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNotEmptyUserIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        accountRequest = ACCOUNT_TMP,
        operationRequest = OPERATION_TMP,
        accountFilterRequest = SRCH_FILTER_TMP,
        accountHistoryRequest = HIST_FILTER_TMP,
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("UserId", error?.field)
    assertContains(error?.message ?: "", "UserId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNameTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            name = "main account",
            amount = 0.0,
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("main account", ctx.accountValidated.name)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNotEmptyNameTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            name = "   ",
            amount = 0.0,
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("Name", error?.field)
    assertContains(error?.message ?: "", "Name")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationAmountTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            name = "main account",
            amount = 1.0,
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 1.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals(
        1.0,
        if (!ctx.accountValidated.amount.isNaN()) ctx.accountValidated.amount else ctx.operationValidated.amount
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatAmountTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            name = "main account",
            amount = Double.NaN,
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = Double.NaN,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("Amount", error?.field)
    assertContains(error?.message ?: "", "Amount")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            name = "main account",
            amount = 0.0,
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
        accountHistoryRequest = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("55d5cab1-3471-4878-9a2d-160bb88886f3", ctx.accountValidated.accountId.asString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            name = "main account",
            amount = 0.0,
            accountId = FinsAccountId("55d5cab1-34714878-9a2d-160bb88886f3"),
        ),
        accountHistoryRequest = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("AccountId", error?.field)
    assertContains(error?.message ?: "", "AccountId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNotEmptyAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            name = "main account",
            amount = 0.0,
            accountId = FinsAccountId("55d5cab1-34714878-9a2d-160bb88886f3"),
        ),
        accountHistoryRequest = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("AccountId", error?.field)
    assertContains(error?.message ?: "", "AccountId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationSearchFilterTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountFilterRequest = FinsSrchFilter(
            searchFilter = "<строка поиска>"
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("<строка поиска>", ctx.accountFilterValidated.searchFilter)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNotEmptySearchFilterTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountFilterRequest = FinsSrchFilter(
            searchFilter = ""
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("SearchFilter", error?.field)
    assertContains(error?.message ?: "", "SearchFilter")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFromDateTimeTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
        accountHistoryRequest = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("2022-02-12T12:00:00Z", ctx.accountHistoryValidated.fromDateTime.toString())
}


@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatFromDateTimeTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
        accountHistoryRequest = FinsHistFilter(
            fromDateTime = Instant.NONE,
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("fromDateTime", error?.field)
    assertContains(error?.message ?: "", "fromDateTime")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationToDateTimeTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
        accountHistoryRequest = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("2023-02-12T09:00:00Z", ctx.accountHistoryValidated.toDateTime.toString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatToDateTimeTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        accountRequest = FinsAccount(
            accountId = FinsAccountId("55d5cab1-3471-4878-9a2d-160bb88886f3")
        ),
        accountHistoryRequest = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.NONE
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("toDateTime", error?.field)
    assertContains(error?.message ?: "", "toDateTime")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFromAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc", ctx.operationValidated.fromAccountId.asString())
    }


@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatFromAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("FromAccountId", error?.field)
    assertContains(error?.message ?: "", "FromAccountId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNotEmptyFromAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("     "),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("FromAccountId", error?.field)
    assertContains(error?.message ?: "", "FromAccountId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatToAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ToAccountId", error?.field)
    assertContains(error?.message ?: "", "ToAccountId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationToAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("ef349779-f895-478c-94f1-19b50fa2eaf9", ctx.operationValidated.toAccountId.asString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNotEmptyToAccountIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("     "),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ToAccountId", error?.field)
    assertContains(error?.message ?: "", "ToAccountId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationOperationDateTimeTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("2022-02-12T12:00:00Z", ctx.operationValidated.operationDateTime.toString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatOperationDateTimeTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Instant.NONE,
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("OperationDateTime", error?.field)
    assertContains(error?.message ?: "", "OperationDateTime")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationOperationIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de-4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FinsState.FAILING, ctx.state)
    assertEquals("e2477813-46de-4298-aca9-ebbbbebeed21", ctx.operationValidated.operationId.asString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNotEmptyOperationIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("   ")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("OperationId", error?.field)
    assertContains(error?.message ?: "", "OperationId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFormatOperationIdTest(command: FinsCommand, processor: FinsProcessor) = runTest {
    val ctx = FinsContext(
        command = command,
        state = FinsState.NONE,
        workMode = FinsWorkMode.TEST,
        userId = FinsUserId("985f67fc-cefd-4dd4-9706-803983b39858"),
        operationRequest = FinsOperation(
            description = "перевод",
            amount = 10.0,
            fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc"),
            toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9"),
            operationDateTime = Clock.System.now(),
            operationId = FinsOperationId("e2477813-46de4298-aca9-ebbbbebeed21")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FinsState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("OperationId", error?.field)
    assertContains(error?.message ?: "", "OperationId")
}
