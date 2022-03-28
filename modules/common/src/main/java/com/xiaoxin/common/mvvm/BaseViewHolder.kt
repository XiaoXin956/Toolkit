package com.xiaoxin.common.mvvm

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Admin
 * @date: 2021-09-03
 */
class BaseViewHolder<VDB : ViewDataBinding?> : RecyclerView.ViewHolder {
    var binding: VDB? = null

    constructor(binding: VDB) : super(binding!!.root) {
        this.binding = binding
    }

    constructor(itemView: View) : super(itemView) {}
}