package com.xiaoxin.basic.fragment

/**
 * 修改activity 父容器的标题
 */
object TitleChangeManager {


    private val listeners: ArrayList<TitleChangeListener> = ArrayList<TitleChangeListener>()

    fun addListener(titleChangeListener: TitleChangeListener) {
        listeners.add(titleChangeListener)
    }

    fun removeListener(titleChangeListener: TitleChangeListener) {
        listeners.remove(titleChangeListener)
    }

    fun setTitle(title: String) {
        listeners.forEach {
            it.titleChange(title = title)
        }
    }

}

interface TitleChangeListener {

    fun titleChange(title: String)

}

