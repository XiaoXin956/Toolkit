package com.xiaoxin.network

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xiaoxin.network.retrofit.RetrofitManager

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.xiaoxin.network.test", appContext.packageName)
    }


    @Test
     fun netWorkTest() {
        z()
    }


     fun z(){
         val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        RetrofitManager()
            .getInstance()
            .getMethod
            .setUrl("https://api.vvhan.com/api/hotlist?type=douban")
//            .requestT {
//
//            }
    }
}