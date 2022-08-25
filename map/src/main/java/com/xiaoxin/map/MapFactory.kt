package com.xiaoxin.map

/**
 * @author: Admin
 * @date: 2022-05-07
 */
object MapFactory {

    private const val gaoDe = "gaode"
    private const val google = "google"
    private const val baidu = "baidu"

    fun getMap(typeMap: String): IMap {
       return when (typeMap) {
            gaoDe -> {
                GaoDeMap()
            }
            google -> {
                GoogleMap()
            }
            else->{
                GaoDeMap()
            }
        }
    }


}