import org.junit.Test
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.fromTransport
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.toTransport
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = AccountCreateRequest(
            requestId = "3395ac64-3cf3-478a-afc9-16fb64b88281",
            requestType = "accountCreate",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS,
            ),
            account = AccountData(
                userId = "1",
                name = "Тинёк-осн",
                description = "основной счет в Тинькофф",
                amount = "0.0"
            )
        )

        val context = FinsContext()
        context.fromTransport(req)

        assertEquals(FinsStubs.SUCCESS, context.stubCase)
        assertEquals(FinsWorkMode.STUB, context.workMode)
        assertEquals("1",  context.accountRequest.userId.asString())
        assertEquals("Тинёк-осн", context.accountRequest.name)
        assertEquals("основной счет в Тинькофф", context.accountRequest.description)
    }

    @Test
    fun toTransport() {
        val context = FinsContext(
            requestId = FinsRequestId("1234"),
            command = FinsCommand.ACCOUNTCREATE,
            accountResponse = FinsAccount(
                userId = FinsUserId("1"),
                name = "Тинёк-осн",
                description = "основной счет в Тинькофф",
                amount = 0.0,
            ),
            errors = mutableListOf(
                FinsError(
                    code = "err",
                    group = "request",
                    field = "name",
                    message = "wrong name",
                )
            ),
            state = FinsState.RUNNING,
        )

        val req = context.toTransport() as AccountCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("Тинёк-осн", req.account?.name)
        assertEquals("основной счет в Тинькофф", req.account?.description)
        assertEquals("0.0", req.account?.amount)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("name", req.errors?.firstOrNull()?.field)
        assertEquals("wrong name", req.errors?.firstOrNull()?.message)
    }
}
