package com.xiaoxin.basiccompose.qrcode

/**
 * 解析器配置
 */
object ImageAnalyzerConfig {

    // 是否开启识别
    var isIdentify: Boolean = true

    var intervalScan: Boolean = true

    // 是否扫描条码
    var onlyBarCode: Boolean = false

    // 是否扫描二维码
    var onlyQrCode: Boolean = false

    // 是否开启多个二维码识别
    var qrCodeMultiple: Boolean = false

    // 解析间隔时间
    var interval: Long = 3000

    // 显示扫码框
    var showScanBox: Boolean = true


}