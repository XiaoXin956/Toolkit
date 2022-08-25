package com.xiaoxin.pay.wechat

import android.content.Context
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and


/**
 * @author: Admin
 * @date: 2022-05-11
 */
class WXPayUtils {

    lateinit var iwxapi: IWXAPI
    lateinit var builder: WXPayBuilder

    /**
     * 调起微信支付的方法,不需要在客户端签名
     */
    fun toWXPayNotSign(context: Context?) {
        iwxapi = WXAPIFactory.createWXAPI(context, null) //初始化微信api
        iwxapi.registerApp(builder.appId) //注册appid  appid可以在开发平台获取
        val payRunnable = Runnable()
        //这里注意要放在子线程
        {
            val request = PayReq() //调起微信APP的对象
            //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
            request.appId = builder.appId
            request.partnerId = builder.partnerId
            request.prepayId = builder.prepayId
            request.packageValue = builder.packageValue
            request.nonceStr = builder.nonceStr
            request.timeStamp = builder.timeStamp
            request.sign = builder.sign
            iwxapi.sendReq(request) //发送调起微信的请求
        }
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 调起微信支付的方法,需要在客户端签名
     */
    fun toWXPayAndSign(context: Context, appid: String?, key: String?) {
        iwxapi = WXAPIFactory.createWXAPI(context, null) //初始化微信api
        iwxapi.registerApp(appid) //注册appid  appid可以在开发平台获取
        val payRunnable = Runnable()
        //这里注意要放在子线程
        {
            val request = PayReq() //调起微信APP的对象
            //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
            request.appId = builder.appId
            request.partnerId = builder.partnerId
            request.prepayId = builder.prepayId
            request.packageValue = "Sign=WXPay"
            //                request.nonceStr = genNonceStr();
            //                request.timeStamp = String.valueOf(genTimeStamp());
            request.nonceStr = builder.nonceStr
            request.timeStamp = builder.timeStamp
            request.sign = builder.sign
            //签名
            val signParams: LinkedHashMap<String, String> = LinkedHashMap()
            signParams["appid"] = request.appId
            signParams["noncestr"] = request.nonceStr
            signParams["package"] = request.packageValue
            signParams["partnerid"] = request.partnerId
            signParams["prepayid"] = request.prepayId
            signParams["timestamp"] = request.timeStamp
            request.sign = genPackageSign(signParams, key!!)
            iwxapi.sendReq(request) //发送调起微信的请求
        }
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 调起微信APP支付，签名
     * 生成签名
     */
    private fun genPackageSign(params: LinkedHashMap<String, String>, key: String): String {
        val sb = StringBuilder()
        for ((key1, value) in params.entries) {
            sb.append(key1)
            sb.append('=')
            sb.append(value)
            sb.append('&')
        }
        sb.append("key=")
        sb.append(key)
        return getMessageDigest(sb.toString().toByteArray())!!.uppercase(Locale.getDefault())
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private fun genNonceStr(): String? {
        val random = Random()
        return getMessageDigest(java.lang.String.valueOf(random.nextInt(10000)).toByteArray())
    }

    /**
     * md5加密
     *
     * @param buffer
     * @return
     */
    private fun getMessageDigest(buffer: ByteArray): String? {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        return try {
            val mdTemp: MessageDigest = MessageDigest.getInstance("MD5")
            mdTemp.update(buffer)
            val md: ByteArray = mdTemp.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[(byte0 and 0xf).toInt()]
            }
            String(str)
        } catch (e: Exception) {
            null
        }
    }

    class WXPayBuilder {

        var appId: String? = null
        var partnerId: String? = null
        var prepayId: String? = null
        var packageValue: String? = null
        var nonceStr: String? = null
        var timeStamp: String? = null
        var sign: String? = null

        fun build(): WXPayBuilder {
            return WXPayBuilder()
        }

        fun setAppId(appId: String): WXPayBuilder {
            this.appId = appId
            return this
        }

        fun setPartnerId(partnerId: String): WXPayBuilder {
            this.partnerId = partnerId
            return this
        }

        fun setPrepayId(prepayId: String): WXPayBuilder {
            this.prepayId = prepayId
            return this
        }

        fun setPackageValue(packageValue: String): WXPayBuilder {
            this.packageValue = packageValue
            return this
        }

        fun setNonceStr(nonceStr: String): WXPayBuilder {
            this.nonceStr = nonceStr
            return this
        }

        fun setTimeStamp(timeStamp: String): WXPayBuilder {
            this.timeStamp = timeStamp
            return this
        }

        fun setSign(sign: String): WXPayBuilder {
            this.sign = sign
            return this
        }

    }

}