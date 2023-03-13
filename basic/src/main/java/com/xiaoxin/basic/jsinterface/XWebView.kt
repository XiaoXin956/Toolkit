package com.xiaoxin.basic.jsinterface

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.*

/**
 * @author: Admin
 * @date: 2022-05-10
 */
object XWebView {

    fun initWebView(
        context: Activity,
        webBase: WebView,
        onWebViewLoad: OnWebViewLoad,
        cacheMode: Int = WebSettings.LOAD_CACHE_ELSE_NETWORK,
        loadsImages: Boolean = true,
        javaScriptEnabled: Boolean = true,
        supportZoom: Boolean = true,
        builtInZoomControls: Boolean = true,
        displayZoomControls: Boolean = true,
        useWideViewPort: Boolean = true,
        layoutAlgorithm: WebSettings.LayoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN,
        loadWithOverviewMode: Boolean = true,
        databaseEnabled: Boolean = true,
        savePassword: Boolean = true,
        domStorageEnabled: Boolean = true,
        isSaveEnabled: Boolean = true,
        keepScreenOn: Boolean = true,
        choosePicture: ((filePathCallback: ValueCallback<Array<Uri>>) -> Unit)
    ) {
        val webSettings = webBase.settings
        webSettings.cacheMode = cacheMode //加载缓存否则网络
        webSettings.loadsImagesAutomatically = loadsImages  //图片自动缩放 打开
        webBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null) //软件解码
        webBase.setLayerType(View.LAYER_TYPE_HARDWARE, null) //硬件解码
        // 设置支持javascript脚本
        webSettings.javaScriptEnabled = javaScriptEnabled
        // 设置可以支持缩放
        webSettings.setSupportZoom(supportZoom)
        // 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        webSettings.builtInZoomControls = builtInZoomControls
        //隐藏缩放工具
        webSettings.displayZoomControls = displayZoomControls
        // 扩大比例的缩放
        webSettings.useWideViewPort = useWideViewPort
        //自适应屏幕
        webSettings.layoutAlgorithm = layoutAlgorithm

        webSettings.loadWithOverviewMode = loadWithOverviewMode
        webSettings.databaseEnabled = databaseEnabled
        //保存密码
        webSettings.savePassword = savePassword
        //是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webSettings.domStorageEnabled = domStorageEnabled

        webBase.isSaveEnabled = isSaveEnabled
        webBase.keepScreenOn = keepScreenOn

        //设置此方法可在WebView中打开链接，反之用浏览器打开
        webBase.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onWebViewLoad.onPageStarted()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!webBase.settings.loadsImagesAutomatically) {
                    webBase.settings.loadsImagesAutomatically = true
                }
                onWebViewLoad.onPageFinished()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                onWebViewLoad.shouldOverrideUrlLoading()
                return true
            }
        }

        val webChromeClient = object :WebChromeClient(){
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
                request.origin
            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
                choosePicture(filePathCallback)
                return true
            }
        }
        webBase.webChromeClient = webChromeClient
    }

    fun loadData(webView: WebView, content: String) {
        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null) //这种写法可以正确解码
    }

    fun loadUrl(webView: WebView, url: String) {
        webView.loadUrl(url) //这种写法可以正确解码
    }

    // 调用js
    fun androidTransferJs(
        webView: WebView,
        jsMethod: String,
        returnRes: ((String) -> Unit)? = null
    ) {
        webView.post {
            if (returnRes == null) {
                webView.evaluateJavascript("javascript:${jsMethod}", null)
            } else {
                webView.evaluateJavascript("javascript:${jsMethod}") {
                    returnRes.invoke(it)
                }
            }
        }
    }

    // 注入js
    fun injectionJS(webView: WebView, js: String) {
        webView.loadUrl("javascript:${js}")
    }

    @SuppressLint("JavascriptInterface")
    fun addBridgeInterface(webView: WebView, obj: Any, jsName: String) {
        webView.addJavascriptInterface(obj, jsName)
    }


    interface OnWebViewLoad {
        fun onPageStarted()
        fun onReceivedTitle(title: String)
        fun onProgressChanged(newProgress: Int)
        fun shouldOverrideUrlLoading()
        fun onPageFinished()
    }

}