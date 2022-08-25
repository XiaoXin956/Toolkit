package com.xiaoxin.toolkit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xiaoxin.toolkit.bean.Hitokoto
import com.xiaoxin.toolkit.constant.ConstantConfig
import com.xiaoxin.toolkit.repository.HitokotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HitokotoViewModel : BaseViewModel() {

    private val hitokotoRepository by lazy { HitokotoRepository() }

    val hitokotoRes by lazy { MutableLiveData<Hitokoto>() }
    fun reqHitokoto(queryMap: Map<String, Any>) {
        viewModelScope.launch {
            val hitokotoApi = hitokotoRepository.getHitokoto(ConstantConfig.hitokoto, queryMap)
            withContext(Dispatchers.Main) {
                if (hitokotoApi != null) {
                    hitokotoRes.value = hitokotoApi
                }else{
                    hitokotoRes.value = null
                }
            }
        }
    }
}