package com.xiaoxin.toolkit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xiaoxin.basic.date.CalendarUtils
import com.xiaoxin.basic.date.DateBean
import com.xiaoxin.map.GoogleBean
import com.xiaoxin.map.MapHttpResponse
import com.xiaoxin.map.repository.GoogleMapRepository
import com.xiaoxin.toolkit.App
import com.xiaoxin.toolkit.HttpResponse
import com.xiaoxin.toolkit.repository.UserRepository
import kotlinx.coroutines.*
import kotlin.collections.HashMap

/**
 * @author: Admin
 * @date: 2022-03-22
 */
class UserViewModel : BaseViewModel() {

    private val userRepository by lazy {
        UserRepository()
    }

    val resText by lazy { MutableLiveData<String>() }
    val resLogin by lazy { MutableLiveData<String>() }

    val upLoadFileProgress by lazy {
        MutableLiveData<Int>()
    }

    val downLoadFileProgress by lazy {
        MutableLiveData<Int>()
    }

    val calendarData by lazy { MutableLiveData<List<DateBean>>() }

    fun login(data: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val login = userRepository.login("http://easydrop.unipock.net:9090/login", data)
            launch(Dispatchers.Main) {
                if (login.code == 0) {
                    token.value = login.value.toString()
                    App.token = login.value.toString()
                    resLogin.value = login.value.toString()
                } else {
                    App.token = ""
                    resLogin.value = login.msg
                }
            }
        }

        viewModelScope.launch {
            val job = async(Dispatchers.IO) { CalendarUtils().createCalendar("2021-10-1", null, 7) }
            val dates: List<DateBean> = job.await()
            withContext(Dispatchers.Main) {
                calendarData.value = dates
            }
        }
    }

    fun loginEx(data: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val login = userRepository.msg("http://easydrop.unipock.net:9090/login", data)
            launch(Dispatchers.Main) {
                if (login.code == 0) {
                    token.value = login.value.toString()
                    App.token = login.value.toString()
                    resLogin.value = login.value.toString()
                } else {
                    App.token = ""
                    resLogin.value = login.msg
                }
            }
        }
    }

    fun upLoadFile(map: HashMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            val headers = HashMap<String, Any>()
            headers["token"] = token.value.toString()
            val upLoadFileRes = userRepository.upLoadFile(
                "http://easydrop.unipock.net:9090/resources/cxc/upload",
                headers,
                map
            ) {
                launch(Dispatchers.Main) {
                    Log.e("文件进度", it.toString())
                    upLoadFileProgress.postValue(it.toInt())
                    upLoadFileProgress.value = it.toInt()
                }
            }
            withContext(Dispatchers.Main) {
                Log.e("文件", upLoadFileRes.toString())
            }
        }
    }

    fun downLoadFile() {
        viewModelScope.launch(Dispatchers.IO) {
            val headers = HashMap<String, Any>()
            val upLoadFileRes = userRepository.downLoadFile(
                "https://6b781829a90db6988044d27787e61582.rdt.tfogc.com:49156/imtt.dd.qq.com/sjy.10001/16891/apk/377C09332DA0F77C77D927D146FC85BE.apk?mkey=623d55958ac6d08eea81e0fc7a67202f&arrive_key=130860530074&fsname=cn.caocaokeji.user_5.4.0_51280.apk&csr=3554&cip=219.134.119.1&proto=https",
                headers,
                ""
            ) {
                launch(Dispatchers.Main) {
                    Log.e("进度", it.toString())
                    downLoadFileProgress.value = it.toInt()
                }
            }
            withContext(Dispatchers.Main) {
                Log.e("文件", upLoadFileRes.toString())
            }
        }
    }

    fun login3(data: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val login1 =
                async { userRepository.login("http://easydrop.unipock.net:9090/login", data) }
            delay(1000)
            val login2 =
                async { userRepository.login("http://easydrop.unipock.net:9090/login", data) }
            Log.e("协程返回", "${login1.await().toString()} ,\n ${login2.await()}")
        }


    }

    val resTextUpdate by lazy { MutableLiveData<String>() }

    private var job: Job? = null
    fun searchData(searchRes: String) {
        if (job == null) {
            job = viewModelScope.launch {
                delay(2000)
                // 进行数据修改
                resTextUpdate.value = searchRes
            }
        } else {
            job!!.cancel()
            job = viewModelScope.launch {
                delay(2000)
                // 进行数据修改
                resTextUpdate.value = searchRes
            }
        }

    }

    private val googleR by lazy { GoogleMapRepository() }
     val addressCode by lazy { MutableLiveData<MapHttpResponse<GoogleBean.GeoCodes>>() }
    fun reqAddressCode(maps: HashMap<String, Any>) {

        viewModelScope.launch {

            val googleCode = googleR.googleCode("AIzaSyA50Y6Y95UAiuZZ1QBY9qSFuv1JbpGP2FU", maps)
            addressCode.value = googleCode
        }

    }


}