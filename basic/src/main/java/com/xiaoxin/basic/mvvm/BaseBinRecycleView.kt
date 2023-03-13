package com.xiaoxin.basic.mvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Admin
 * @date: 2021-09-03
 */
abstract class BaseBinRecycleView<T, VDB : ViewDataBinding?> : RecyclerView.Adapter<BaseViewHolder<VDB>> {

    private var lists: MutableList<T>

    private var mItemListener: ((view: View?, t: T, position: Int) -> Unit)? = null

    constructor() {
        lists = ArrayList()
    }

    constructor(lists: List<T>?) {
        this.lists = (lists ?: ArrayList()) as MutableList<T>
    }


    fun setItemListener(itemListener: ((view: View?, data: T, position: Int) -> Unit)?) {
        this.mItemListener = itemListener
    }

    protected abstract val layoutId: Int
    fun refresh(lists: List<T>?) {
        if (lists != null) {
            this.lists.clear()
            this.lists.addAll(lists)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VDB> {
        val holder = createViewHolder<VDB>(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        )
        val view = holder.binding!!.root
        view.rootView.setOnClickListener { v ->
            if (mItemListener != null) {
                val position = holder.adapterPosition
                mItemListener!!.invoke(v, lists[position], position)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VDB>, position: Int) {
        onBinData(holder, position, lists[position])
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    fun getData(): List<T> {
        return lists
    }

    fun getDataIndexOf(t:T?): Int {
        return lists.indexOf(t)
    }

    fun getData(position: Int): T {
        return lists[position]
    }

    abstract fun onBinData(holder: BaseViewHolder<VDB>, position: Int, data: T)
    private fun <HVDB : ViewDataBinding?> createViewHolder(binding: HVDB): BaseViewHolder<HVDB> {
        return BaseViewHolder(binding)
    }
}