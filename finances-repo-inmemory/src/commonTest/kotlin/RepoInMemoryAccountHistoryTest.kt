import ru.otus.otuskotlin.sokolova.finances.backend.repo.test.RepoAccountCreateTest
import ru.otus.otuskotlin.sokolova.finances.backend.repo.test.RepoAccountHistoryTest
import ru.otus.otuskotlin.sokolova.finances.backend.repo.test.RepoAccountSearchTest
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository

class RepoInMemoryAccountHistoryTest : RepoAccountHistoryTest() {
    override val repo: IRepository = RepoInMemory(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}