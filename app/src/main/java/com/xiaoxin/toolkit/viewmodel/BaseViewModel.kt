package com.xiaoxin.toolkit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xiaoxin.toolkit.App

/**
 * @author: Admin
 * @date: 2022-03-22
 */
open class BaseViewModel : ViewModel() {

    val token by lazy { MutableLiveData<String>() }


}