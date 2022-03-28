package com.xiaoxin.common.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * @author: Admin
 * @date: 2021-08-24
 */
open class SPUtils {


    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        var SP_NAME: String = "config"
        var context: Context? = null

        fun SPUtils(context: Context?) {
            this.context = context
        }

        /**
         * 存储
         */
        @JvmStatic
        fun save(context: Context?, key: String, value: Any) {
            context?.let {
                if (sharedPreferences == null) {
                    sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                }
                when (value) {
                    is String -> {
                        sharedPreferences.edit().putString(key, value).apply()
                    }
                    is Boolean -> {
                        sharedPreferences.edit().putBoolean(key, value).apply()
                    }
                    is Long -> {
                        sharedPreferences.edit().putLong(key, value).apply()
                    }
                    is Int -> {
                        sharedPreferences.edit().putInt(key, value).apply()
                    }
                    is Float -> {
                        sharedPreferences.edit().putFloat(key, value).apply()
                    }
                    else -> {

                    }
                }

            }
        }

        /**
         * 移除
         */
        @JvmStatic
         fun remove(context: Context?, key: String) {
            context?.let {
                if (sharedPreferences == null) {
                    sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                }
                sharedPreferences.edit().remove(key).commit()
            }
        }

        /**
         * 获取
         */
        @JvmStatic
         fun get(context: Context?, key: String, defValue: Any): Any? {
            if (sharedPreferences == null) {
                sharedPreferences = context?.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)!!;
            }
            return when (defValue) {
                is String -> {
                    sharedPreferences.getString(key, defValue)
                }
                is Boolean -> {
                    sharedPreferences.getBoolean(key, defValue)
                }
                is Long -> {
                    sharedPreferences.getLong(key, defValue)
                }
                is Int -> {
                    sharedPreferences.getInt(key, defValue)
                }
                is Float -> {
                    sharedPreferences.getFloat(key, defValue)
                }
                else -> {

                }
            }

        }

        @JvmStatic
        fun get(save_db: String, b: Boolean): Boolean {
            TODO("Not yet implemented")
        }
    }


}