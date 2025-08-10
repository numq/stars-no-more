package io.github.numq.starsnomore.credentials

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.json.Json
import java.io.File

interface CredentialsManager {
    val credentials: StateFlow<Credentials>

    fun updateCredentials(credentials: Credentials): Result<Unit>

    class Default(private val json: Json) : CredentialsManager {
        private val FILE_PATH = "${File(System.getProperty("user.dir")).parent}/credentials.json"

        private val _credentials = MutableStateFlow(loadCredentials())

        override val credentials = _credentials.asStateFlow()

        private fun loadCredentials(): Credentials {
            val file = File(FILE_PATH)

            return when {
                !file.exists() -> {
                    file.createNewFile()

                    Credentials()
                }

                file.length() == 0L -> Credentials()

                else -> json.decodeFromString(file.readText())
            }
        }

        override fun updateCredentials(credentials: Credentials) = runCatching {
            File(FILE_PATH).writeText(Json.encodeToString(_credentials.updateAndGet { credentials }))
        }
    }
}