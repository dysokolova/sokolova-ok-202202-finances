import ru.otus.otuskotlin.sokolova.finances.backend.repo.test.RepoAccountCreateTest
import ru.otus.otuskotlin.sokolova.finances.backend.repo.test.RepoAccountDeleteTest
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository

class RepoInMemoryAccountDeleteTest : RepoAccountDeleteTest() {
    override val repo: IRepository = RepoInMemory(
        initAccountObjects = initAccountObjects,
        initOperationObjects = initOperationObjects,
    )
}