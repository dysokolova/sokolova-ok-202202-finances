package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperation
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getError

data class DbResponse<T>(
    override val result: T?,
    override val isSuccess: Boolean,
    override val errors: List<FinsError> = emptyList(),
) : IDbResponse<T> {

    companion object {
        fun DbResponse<FinsAccount>.toDbAccountResponse() = DbAccountResponse(this.result, this.isSuccess, this.errors)
        fun DbResponse<List<FinsAccount>>.toDbAccountsResponse() =
            DbAccountsResponse(this.result, this.isSuccess, this.errors)

        fun DbResponse<FinsOperation>.toDbOperationResponse() =
            DbOperationResponse(this.result, this.isSuccess, this.errors)

        fun DbResponse<List<FinsOperation>>.toDbOperationsResponse() =
            DbOperationsResponse(this.result, this.isSuccess, this.errors)

        fun <T> resultError(stub: FinsStubs, value: String = ""): DbResponse<T> {
            val error = stub.getError(value)
            if (error != null) return DbResponse<T>(
                result = null,
                isSuccess = false,
                errors = listOf(
                    error,
                )
            ) else return DbResponse(
                result = null,
                isSuccess = true,
                errors = emptyList(),
            )

        }
    }
}