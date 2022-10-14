package ru.otus.otuskotlin.sokolova.finances.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.helpers.errorAdministration
import ru.otus.otuskotlin.sokolova.finances.common.helpers.fail
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsWorkMode
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository


fun ICorChainDsl<FinsContext>.initRepo(title: String) = worker {
    this.title = title
    description = "Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы"
    handle {
        repo = when (workMode) {
            FinsWorkMode.TEST -> settings.repoTest
            FinsWorkMode.STUB -> IRepository.NONE
            else -> settings.repoProd
        }
        if (workMode != FinsWorkMode.STUB && repo == IRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
