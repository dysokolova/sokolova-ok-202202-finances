package ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory

import ru.otus.otuskotlin.sokolova.finances.common.repo.*
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub

class RepoStub() : IRepository {
    override suspend fun accountCreate(rq: DbAccountRequest): DbAccountResponse {
        return DbAccountResponse(
            result = FinsObjectsStub.prepareAccountResult {  },
            isSuccess = true,
        )
    }

    override suspend fun accountRead(rq: DbAccountIdRequest): DbAccountResponse {
        return DbAccountResponse(
            result = FinsObjectsStub.prepareAccountResult {  },
            isSuccess = true,
        )
    }

    override suspend fun accountUpdate(rq: DbAccountRequest): DbAccountResponse {
        return DbAccountResponse(
            result = FinsObjectsStub.prepareAccountResult {  },
            isSuccess = true,
        )
    }

    override suspend fun accountDelete(rq: DbAccountIdRequest): DbAccountResponse {
        return DbAccountResponse(
            result = FinsObjectsStub.prepareAccountResult {  },
            isSuccess = true,
        )
    }

    override suspend fun accountSearch(rq: DbAccountFilterRequest): DbAccountsResponse {
        return DbAccountsResponse(
            result = FinsObjectsStub.prepareSearchList(searchFilter = ""),
            isSuccess = true,
        )
    }

    override suspend fun accountHistory(rq: DbAccountHistoryRequest): DbOperationsResponse {
        return DbOperationsResponse(
            result = FinsObjectsStub.prepareHistoryList(FinsObjectsStub.prepareAccountResult {  }.accountId, FinsObjectsStub.prepareHistFilter {}),
            isSuccess = true,
        )
    }

    override suspend fun operationCreate(rq: DbOperationRequest): DbOperationResponse {
        return DbOperationResponse(
            result = FinsObjectsStub.prepareOperationResult {  },
            isSuccess = true,
        )
    }

    override suspend fun operationRead(rq: DbOperationIdRequest): DbOperationResponse {
        return DbOperationResponse(
            result = FinsObjectsStub.prepareOperationResult {  },
            isSuccess = true,
        )
    }

    override suspend fun operationUpdate(rq: DbOperationRequest): DbOperationResponse {
        return DbOperationResponse(
            result = FinsObjectsStub.prepareOperationResult {  },
            isSuccess = true,
        )
    }

    override suspend fun operationDelete(rq: DbOperationIdRequest): DbOperationResponse {
        return DbOperationResponse(
            result = FinsObjectsStub.prepareOperationResult {  },
            isSuccess = true,
        )
    }
}
