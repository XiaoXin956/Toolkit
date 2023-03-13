package com.xiaoxin.network.udp

import com.xiaoxin.network.NetWorkLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates

class UdpManager {

    private var socket: DatagramSocket? = null

    private var localPort by Delegates.notNull<Int>()
    private lateinit var targetHost: String
    private var targetPort by Delegates.notNull<Int>()

    // 配置参数
    suspend fun setConfig(
        localPort: Int, targetHost: String, targetPort: Int
    ) {
        this.localPort = localPort
        this.targetHost = targetHost
        this.targetPort = targetPort
        if (socket == null) {
            // 配置本地端口
            socket = withContext(Dispatchers.IO) {
                DatagramSocket(localPort)
            }
        }
    }

    // 发送数据
    suspend fun udpSendMsg(msg: String) {
        withContext(Dispatchers.IO) {
            try {
                // 将数据转为字节流
                val data = msg.toByteArray(StandardCharsets.UTF_8)
                val packet = DatagramPacket(data, data.size, InetAddress.getByName(targetHost), targetPort)
                socket!!.send(packet)
                NetWorkLog.print( "发送成功")
            } catch (e: Exception) {
                NetWorkLog.print("发送失败")
                e.printStackTrace()
            }
        }
    }

    suspend fun udpReceiveMsg() {
        withContext(Dispatchers.IO) {
            if (socket == null) {
                // 配置本地端口
                socket = withContext(Dispatchers.IO) {
                    DatagramSocket(localPort)
                }
            }
            var result:String
            while (true){
                val data = ByteArray(1024)
                val packet = DatagramPacket(data, data.size)
                NetWorkLog.print("开始接收")
                try {
                    socket?.receive(packet)
                }catch (e:Exception){
                    NetWorkLog.print("接收异常")
                }
                // 获取对方数据
                val ip = packet.address.hostAddress
                val port = packet.port
                 result = String(packet.data,0,packet.length)
                NetWorkLog.print("对方ip $ip:$port 接收完成 $result")
            }
        }
    }

    // 需要关闭
    fun onDestroy() {
        if (socket != null) {
            socket?.close()
        }
    }


}