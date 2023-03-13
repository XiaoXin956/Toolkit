package com.xiaoxin.basic.jsinterface

import android.net.Uri
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class XWebChromeClient : WebChromeClient {


    var choosePicture: ((filePathCallback: ValueCallback<Array<Uri>>) -> Unit)? = null

    constructor(choosePicture: ((filePathCallback: ValueCallback<Array<Uri>>) -> Unit)?) : super() {
        this.choosePicture = choosePicture
    }

    override fun onPermissionRequest(request: PermissionRequest) {
        request.grant(request.resources)
        request.origin
    }

    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
        choosePicture!!(filePathCallback)
        return true
    }

}