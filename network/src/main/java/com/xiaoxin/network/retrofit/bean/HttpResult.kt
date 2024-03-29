package com.xiaoxin.network.retrofit.bean

/**
 * @author: Admin
 * @date: 2022-03-24
 */
enum class HttpResult(var code: Int, var msg: String) {

    SUCCESS(0,"获取成功"),
    ERROR(-1,"数据异常，请稍后再试"),

    // HTTP code 异常
    // 200
    HTTP200(200,"成功"),
    HTTP201(201,"已创建"),
    HTTP202(202,"已接受"),
    HTTP203(203,"非授权信息"),
    HTTP204(204,"无内容"),
    HTTP205(205,"重置内容"),
    HTTP206(206,"部分内容"),
    // 300
    HTTP300(300,"多种选择"),
    HTTP301(301,"永久移动"),
    HTTP302(302,"临时移动"),
    HTTP303(303,"查看其他位置"),
    HTTP304(304,"未修改"),
    HTTP305(305,"使用代理"),
    HTTP306(306,"临时重定向"),
    // 400
    HTTP400(400,"错误请求"),
    HTTP401(401,"未授权"),
    HTTP402(402,"服务器拒绝请求"),
    HTTP404(404,"未找到"),
    HTTP405(405,"方法禁用"),
    HTTP406(406,"不接受"),
    HTTP407(407,"需要代理授权"),
    HTTP408(408,"请求超时"),
    HTTP409(409,"冲突"),
    HTTP410(410,"已删除"),
    HTTP411(411,"需要有效长度"),
    HTTP412(412,"未满足前提条件"),
    HTTP413(413,"请求实体过大"),
    HTTP414(414,"请求的 URI 过长"),
    HTTP415(415,"不支持的媒体类型"),
    HTTP416(416,"请求范围不符合要求"),
    HTTP417(417,"未满足期望值"),
    // 500
    HTTP500(500,"服务器内部错误"),
    HTTP501(501,"尚未实施"),
    HTTP502(502,"错误网关"),
    HTTP503(503,"服务不可用"),
    HTTP504(504,"网关超时"),
    HTTP505(505,"HTTP 版本不受支持"),

    ParseException(-50,"解析异常"),
    ConnectException(-51,"连接异常"),
    UnknownHostException(-52,"目标主机异常"),
    SocketTimeoutException(-53,"网络超时"),
    FileNotFoundException(-54,"文件不存在"),

    // 以下逻辑异常


}