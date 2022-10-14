package ru.otus.otuskotlin.sokolova.finances.common.repo

interface IRepository {
    suspend fun accountCreate(rq: DbAccountRequest): DbAccountResponse
    suspend fun accountRead(rq: DbAccountIdRequest): DbAccountResponse

    suspend fun accountUpdate(rq: DbAccountRequest): DbAccountResponse
    suspend fun accountDelete(rq: DbAccountIdRequest): DbAccountResponse
    suspend fun accountSearch(rq: DbAccountFilterRequest): DbAccountsResponse
    suspend fun accountHistory(rq: DbAccountHistoryRequest): DbOperationsResponse
    suspend fun operationCreate(rq: DbOperationRequest): DbOperationResponse
    suspend fun operationRead(rq: DbOperationIdRequest): DbOperationResponse
    suspend fun operationUpdate(rq: DbOperationRequest): DbOperationResponse
    suspend fun operationDelete(rq: DbOperationIdRequest): DbOperationResponse

    companion object {

        val NONE = object : IRepository {
            override suspend fun accountCreate(rq: DbAccountRequest): DbAccountResponse {
                TODO("Not yet implemented")
            }

            override suspend fun accountRead(rq: DbAccountIdRequest): DbAccountResponse {
                TODO("Not yet implemented")
            }

            override suspend fun accountUpdate(rq: DbAccountRequest): DbAccountResponse {
                TODO("Not yet implemented")
            }

            override suspend fun accountDelete(rq: DbAccountIdRequest): DbAccountResponse {
                TODO("Not yet implemented")
            }

            override suspend fun accountSearch(rq: DbAccountFilterRequest): DbAccountsResponse {
                TODO("Not yet implemented")
            }

            override suspend fun accountHistory(rq: DbAccountHistoryRequest): DbOperationsResponse {
                TODO("Not yet implemented")
            }

            override suspend fun operationCreate(rq: DbOperationRequest): DbOperationResponse {
                TODO("Not yet implemented")
            }

            override suspend fun operationRead(rq: DbOperationIdRequest): DbOperationResponse {
                TODO("Not yet implemented")
            }

            override suspend fun operationUpdate(rq: DbOperationRequest): DbOperationResponse {
                TODO("Not yet implemented")
            }

            override suspend fun operationDelete(rq: DbOperationIdRequest): DbOperationResponse {
                TODO("Not yet implemented")
            }

        }
    }
}
