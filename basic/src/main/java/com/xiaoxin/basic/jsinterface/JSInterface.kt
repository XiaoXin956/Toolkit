package com.xiaoxin.common.jsinterface

import android.webkit.JavascriptInterface

/**
 * @author: Admin
 * @date: 2022-05-10
 */
class JSInterface {

    var js:((String)->Unit)?=null
    fun jsResult(method:((String)->Unit)){
        js = method
    }

    @JavascriptInterface
    fun jsToAndroid(res:String){
        js?.invoke(res)
    }

}