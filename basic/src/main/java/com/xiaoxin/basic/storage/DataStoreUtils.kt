package com.xiaoxin.basic.storage

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.jvm.Throws

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "common")

object DataStoreUtils {

    private lateinit var dataStore: DataStore<Preferences>

    fun init(context: Context) {
        dataStore = context.dataStore
    }

    suspend fun <V> putData(key: String, value: V) {
        when (value) {
            is Long -> saveLongData(key, value)
            is String -> saveStringData(key, value)
            is Int -> saveIntData(key, value)
            is Boolean -> saveBooleanData(key, value)
            is Float -> saveFloatData(key, value)
            else -> throw IllegalArgumentException("这种类型不能保存到 DataStore")
        }
    }

    private suspend fun saveFloatData(key: String, value: Float) {
        dataStore.edit {
            it[floatPreferencesKey(key)] = value
        }
    }

    private suspend fun saveBooleanData(key: String, value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    private suspend fun saveIntData(key: String, value: Int) {
        dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    private suspend fun saveStringData(key: String, value: String) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    private suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String, default: T): Flow<T> {
        val data = when (default) {
            is Long -> readLongFlow(key, default)
            is String -> readStringFlow(key, default)
            is Int -> readIntFlow(key, default)
            is Boolean -> readBooleanFlow(key, default)
            is Float -> readFloatFlow(key, default)
            else -> throw IllegalArgumentException("这种类型不能保存到 DataStore")
        }
        return data as Flow<T>
    }

    private fun readFloatFlow(key: String, default: Float): Flow<Float> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { it[floatPreferencesKey(key)] ?: default }

    private fun readBooleanFlow(key: String, default: Boolean): Flow<Boolean> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { it[booleanPreferencesKey(key)] ?: default }

    private fun readIntFlow(key: String, default: Int): Flow<Int> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { it[intPreferencesKey(key)] ?: default }

    private fun readStringFlow(key: String, default: String): Flow<String> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { it[stringPreferencesKey(key)] ?: default }

    private fun readLongFlow(key: String, default: Long = 0L): Flow<Long> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[longPreferencesKey(key)] ?: default
            }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}