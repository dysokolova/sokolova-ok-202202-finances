package ru.otus.otuskotlin.sokolova.finances.common.models

import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository

data class FinsSettings(
    val repoStub: IRepository = IRepository.NONE,
    val repoTest: IRepository = IRepository.NONE,
    val repoProd: IRepository = IRepository.NONE,
)
