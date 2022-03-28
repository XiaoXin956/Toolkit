package com.xiaoxin.common

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * @author: Admin
 * @date: 2022-03-23
 */
class DataStoreUtils(context: Context, name: String) {

    var context: Context? = null

    // 需要先初始化
    fun init(context: Context) {
        this.context = context
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "common"
    )

    // 获取
    fun get(context: Context, key: String): Any {
        val intPreferencesKey = intPreferencesKey(name = key)
        val data: Flow<Any> = context.dataStore.data
            .map {
                it[intPreferencesKey] ?: -1
            }
        return data
    }

    fun put(context: Context, key: String, data: Any) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit {
            }
        }
    }

}