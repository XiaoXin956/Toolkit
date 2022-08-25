package com.xiaoxin.common.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.lang.ref.SoftReference

/**
 * @author: Admin
 * @date: 2021-08-23
 */
class ActivityManagers {

    private var activityManagers: ActivityManagers? = null
    private var taskMaps = HashMap<String, SoftReference<Activity>>() // 存储

    fun getActivityManager(context: Context?): ActivityManagers? {
        if (activityManagers == null) {
            context?.let {
                activityManagers = ActivityManagers()
            }
        }
        return activityManagers
    }

    // 添加activity
    fun putActivity(activity: Activity?) {
        activity?.let {
            taskMaps.put(activity.toString(), SoftReference(activity))
        }
    }

    // 移除activity
    fun removeActivity(activity: Activity?) {
        activity?.let {
            taskMaps.remove(activity.toString())
        }
    }

    // 退出程序
    fun exit() {
        val iterator: Iterator<Map.Entry<String, SoftReference<Activity>>> = taskMaps.entries.iterator()
        while (iterator.hasNext()) {
            val activityReference = iterator.next().value
            val activity = activityReference.get()
            activity?.let {
                activity.finish()
            }
        }
    }

    // 刪除指定activity
    fun finishActivity(cla: Class<*>) {
        val iterator: Iterator<Map.Entry<String, SoftReference<Activity>>> = taskMaps.entries.iterator()
        while (iterator.hasNext()) {
            val activityReference = iterator.next().value
            val activity = activityReference.get()
            activity?.let {
                if (it.javaClass == cla) {
                    it.finish()
                }
            }
        }
    }

    // 跳转
    fun toActivity(context: Context, activityName: String, vararg args: Any?): Boolean {
        return try {
            val intent = Intent()
            intent.setClassName(context, activityName)
            args.let {
                for (arg in args) {
                    val obj = args[0]
                    if (obj is Bundle) {
                        intent.putExtras(obj)
                    }
                }
            }
            context.startActivity(intent)
            true
        } catch (ex: Exception) {
            false
        }
    }

    // 跳转
    fun toActivity(context: Context?, activity: Class<*>, vararg args: Any?): Boolean {
        return try {
            val intent = Intent(context,activity::class.java)
            args.let {
                for (arg in args) {
                    val obj = args[0]
                    if (obj is Bundle) {
                        intent.putExtras(obj)
                    }
                }
            }
            context?.startActivity(intent)
            true
        } catch (ex: Exception) {
            false
        }
    }
}