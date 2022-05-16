package id.capstone.tanamin.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences(private val dataStore: DataStore<Preferences>) {
    private val USER_TOKEN_KEY = stringPreferencesKey("token_user")
    private val USER_NAME_KEY = stringPreferencesKey("name_user")
    private val USER_EMAIL_KEY = stringPreferencesKey("email_user")
    private val STATUS_ONBOARD = booleanPreferencesKey("status_onboard")
    private val CURRENT_MODULE = stringPreferencesKey("current_module")

    fun getStatusOnBoard(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATUS_ONBOARD] ?: false
        }
    }

    suspend fun saveStatusOnBoard(statusOnBoard: Boolean) {
        dataStore.edit { preferences ->
            preferences[STATUS_ONBOARD] = statusOnBoard
        }
    }

    fun getTokenUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_TOKEN_KEY] ?: DEFAULT
        }
    }

    suspend fun saveTokenUser(token :String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }

    fun getNameUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_NAME_KEY] ?: DEFAULT
        }
    }

    suspend fun saveNameUser(name :String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    fun getEmailUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_EMAIL_KEY] ?: DEFAULT
        }
    }

    suspend fun saveEmailUser(email :String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }

    fun getCurrentModule(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CURRENT_MODULE] ?: DEFAULT
        }
    }

    suspend fun saveCurrentModule(current :String) {
        dataStore.edit { preferences ->
            preferences[CURRENT_MODULE] = current
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null
        const val DEFAULT = "DEFAULT_VALUE"

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}