package io.github.numq.starsnomore.credentials

import io.github.numq.starsnomore.interactor.Interactor

class GetCredentials(private val credentialsManager: CredentialsManager) : Interactor<Unit, Credentials> {
    override suspend fun execute(input: Unit) = Result.success(credentialsManager.credentials.value)
}