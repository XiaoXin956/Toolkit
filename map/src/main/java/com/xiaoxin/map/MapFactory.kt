package com.xiaoxin.map

import com.xiaoxin.map.constant.ConstantMap
import com.xiaoxin.map.repository.GaoDeMap
import com.xiaoxin.map.repository.GoogleMap

/**
 * @author: Admin
 * @date: 2022-05-07
 */
object MapFactory {

    fun getMap(typeMap: String): IMap {
       return when (typeMap) {
           ConstantMap.GaoDeCode -> {
                GaoDeMap()
            }
           ConstantMap.BaiDuCode -> {
                GoogleMap()
            }
           ConstantMap.GoogleCode->{
                GaoDeMap()
            }
           else -> {
               GaoDeMap()
           }
       }
    }

}