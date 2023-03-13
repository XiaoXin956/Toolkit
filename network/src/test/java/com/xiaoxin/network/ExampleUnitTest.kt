package com.xiaoxin.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xiaoxin.network.retrofit.RetrofitManager
import com.xiaoxin.network.test.JWebSocket
import com.xiaoxin.network.udp.UdpManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import java.net.URI

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {

        assertEquals(4, 2 + 2)
    }

    @Test
    fun netWorkTest() = runTest {
        var result = ""
        RetrofitManager()
            .getInstance()
            .getMethod
            .setUrl("https://api.vvhan.com/api/hotlist?type=douban")
            .requestT(
                success = {
                    result = it.toString()
                },
                error = {
                    print(it.toString())
                    assertEquals("1", "1")
                }
            )
        delay(3000)
        launch {
            var res = Gson().fromJson<Map<Any, Any>>(result, object : TypeToken<Map<Any, Any>>() {}.type).also {
            }
            assertEquals(true, res["success"])
        }
    }

    @Test
    fun netWorkUdpSendMsg() = runTest {
        launch {
            val udpManager = UdpManager()
            udpManager.setConfig(8888, "192.168.1.140", 9999)
            udpManager.udpSendMsg("这是 8888 send content")
        }
    }

    @Test
    fun netWorkUdpReceiveMsg() = runTest {
        launch {
            val udpManager = UdpManager()
            udpManager.setConfig(9999, "192.168.1.140", 8888)
            udpManager.udpReceiveMsg()
        }
    }


}