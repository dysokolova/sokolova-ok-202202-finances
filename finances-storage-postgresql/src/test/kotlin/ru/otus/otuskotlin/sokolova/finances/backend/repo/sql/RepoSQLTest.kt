package ru.otus.otuskotlin.sokolova.finances.backend.repo.sql

import ru.otus.otuskotlin.sokolova.finances.backend.repo.postgresql.RepoSQL
import ru.otus.otuskotlin.sokolova.finances.backend.repo.test.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository

class RepoSQLAccountCreateTest : RepoAccountCreateTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLAccountReadTest : RepoAccountReadTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLAccountUpdateTest : RepoAccountUpdateTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLAccountDeleteTest : RepoAccountDeleteTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLAccountSearchTest : RepoAccountSearchTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLAccountHistoryTest : RepoAccountHistoryTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLOperationCreateTest : RepoOperationCreateTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLOperationReadTest : RepoOperationReadTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLOperationUpdateTest : RepoOperationUpdateTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}


class RepoSQLOperationDeleteTest : RepoOperationDeleteTest() {
    override val repo: IRepository = SqlTestCompanion.repoUnderTestContainer(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}