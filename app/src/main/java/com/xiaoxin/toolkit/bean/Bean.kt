package com.xiaoxin.toolkit.bean

/*
*
* id 	    一言标识
hitokoto 	一言正文。编码方式 unicode。使用 utf-8。
type 	    类型。请参考第三节参数的表格
from 	    一言的出处
from_who 	一言的作者
creator 	添加者
creator_uid 	添加者用户标识
reviewer 	    审核员标识
uuid 	        一言唯一标识；可以链接到 https://hitokoto.cn?uuid=[uuid](opens new window) 查看这个一言的完整信息
commit_from 	提交方式
created_at 	        添加时间
length 	        句子长度
*
* */
class Hitokoto {
    var id: Int = -1   //一言标识
    var hitokoto: String? = null  //一言正文。编码方式 unicode。使用 utf-8。
    var type: String? = null   //类型。请参考第三节参数的表格
    var from: String? = null   //一言的出处
    var from_who: String? = null   //一言的作者
    var creator: String? = null   //添加者
    var creator_uid: String? = null   //添加者用户标识
    var reviewer: String? = null   // 审核员标识
    var uuid: String? =null   // 一言唯一标识；可以链接到 https://hitokoto.cn?uuid=[uuid](opens new window) 查看这个一言的完整信息
    var commit_from: String? = null   //提交方式
    var created_at: String? = null   //        添加时间
    var length: String? = null   //    句子长度
    var collection:Boolean = false  // 是否收藏
}