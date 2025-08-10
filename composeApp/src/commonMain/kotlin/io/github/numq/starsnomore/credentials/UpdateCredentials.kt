package io.github.numq.starsnomore.credentials

import io.github.numq.starsnomore.interactor.Interactor

class UpdateCredentials(
    private val credentialsManager: CredentialsManager,
) : Interactor<UpdateCredentials.Input, Unit> {
    data class Input(val credentials: Credentials)

    override suspend fun execute(input: Input) = credentialsManager.updateCredentials(credentials = input.credentials)
}