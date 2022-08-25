package com.xiaoxin.common.adapter

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListAdapter
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Admin
 * @date: 2021-08-27
 */
abstract class BaseRecycleViewAdapter<T> : RecyclerView.Adapter<SmartViewHolder>(), ListAdapter {

    final var mLayoutId: Int = 0  // 布局
    final var mList: MutableList<T>? = null  //数据

    var mLastPosition: Int = -1
    lateinit var mListener: AdapterView.OnItemClickListener

    // kt 点击事件
    lateinit var mKtListener: ((parent: AdapterView<*>?,view: View,position: Int,id: Long) -> Unit)
    fun setKtListener(mKtListener: ((parent: AdapterView<*>?,view: View,position: Int,id: Long) -> Unit)): BaseRecycleViewAdapter<T>{
        this.mKtListener = mKtListener
        return this
    }


    open fun baseRecyclerAdapter(@LayoutRes layoutId: Int) {
        setHasStableIds(false)
        mList = ArrayList()
        mLayoutId = layoutId
    }

    open fun BaseRecyclerAdapter(collection: Collection<T>?, @LayoutRes layoutId: Int) {
        setHasStableIds(false)
        mList = ArrayList(collection)
        mLayoutId = layoutId
    }

    open fun BaseRecyclerAdapter(collection: Collection<T>?, @LayoutRes layoutId: Int, listener: AdapterView.OnItemClickListener) {
        setHasStableIds(false)
        setOnItemClickListener(listener)
        mList = ArrayList(collection)
        mLayoutId = layoutId
    }

    open fun setOnItemClickListener(listener: AdapterView.OnItemClickListener): BaseRecycleViewAdapter<T>? {
        mListener = listener
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false);
//        val smartViewHolder = SmartViewHolder(view, mListener)
        val smartViewHolder = SmartViewHolder(view, mKtListener)
        return smartViewHolder
    }

    override fun onBindViewHolder(holder: SmartViewHolder, position: Int) {
        onBindViewHolder(holder, position, (if (position < mList!!.size) mList!![position] else null)!!);
    }

    abstract fun onBindViewHolder(holder: SmartViewHolder, position: Int, data: T)

    override fun getItemCount(): Int {
        return mList?.size!!
    }

    var dataSetObservable = DataSetObservable()

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        dataSetObservable.registerObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        dataSetObservable.unregisterObserver(observer)
    }

    private fun notifyListDataSetChanged() {
        dataSetObservable.notifyChanged()
    }

    fun notifyDataSetInvalidated() {
        dataSetObservable.notifyInvalidated()
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        val holder: SmartViewHolder
        if (convertView != null) {
            holder = convertView.tag as SmartViewHolder
        } else {
            holder = onCreateViewHolder(parent!!, getItemViewType(position))
            convertView = holder.itemView
            convertView.tag = holder
        }
        holder.position = position
        onBindViewHolder(holder, position)
        return convertView
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return count == 0
    }

    override fun getItem(position: Int): Any? {
        return mList!![position]
    }

    override fun getCount(): Int {
        return mList!!.size
    }

    // 以下是提供的api
    fun get(position: Int): T {
        return mList!![position]
    }

    fun getDatas(): List<T> {
        return mList!!
    }

    /**
     * 刷新数据
     * @return BaseRecycleViewAdapter<T>
     */
    fun refresh(): BaseRecycleViewAdapter<T> {
        notifyDataSetChanged()
        notifyListDataSetChanged()
        mLastPosition = -1
        return this
    }

    /**
     * 刷新添加数据
     * @param collection Collection<T>?
     * @return BaseRecycleViewAdapter<T>
     */
    fun refresh(collection: Collection<T>?): BaseRecycleViewAdapter<T> {
        mList?.clear()
        if (collection != null) {
            mList?.addAll(collection)
        }
        notifyDataSetChanged()
        notifyListDataSetChanged()
        mLastPosition = -1
        return this
    }


    /**
     * 加载更多
     *
     * @param collection
     * @return
     */
    open fun loadMore(collection: Collection<T>?): BaseRecycleViewAdapter<T>? {
        mList!!.addAll(collection!!)
        notifyDataSetChanged()
        notifyListDataSetChanged()
        return this
    }


    /**
     * 插入集合
     *
     * @param collection
     * @return
     */
    open fun insert(collection: Collection<T>): BaseRecycleViewAdapter<T>? {
        mList!!.addAll(0, collection)
        notifyItemRangeInserted(0, collection.size)
        notifyListDataSetChanged()
        return this
    }

    /**
     * 插入一条数据
     *
     * @param object
     * @return
     */
    open fun insertOne(`object`: T): BaseRecycleViewAdapter<T>? {
        mList!!.add(0, `object`)
        notifyDataSetChanged()
        notifyListDataSetChanged()
        return this
    }


    /**
     * 下标插入数据
     *
     * @param collection
     * @return
     */
    open fun insertIndex(insertPosition: Int, collection: Collection<T>?): BaseRecycleViewAdapter<T>? {
        mList!!.addAll(insertPosition, collection!!)
        notifyDataSetChanged()
        notifyListDataSetChanged()
        return this
    }

    /**
     * 删除数据
     *
     * @param deletePosition
     * @return
     */
    open fun removeData(deletePosition: Int): BaseRecycleViewAdapter<T>? {
        mList!!.removeAt(deletePosition)
        notifyItemRemoved(deletePosition)
        notifyDataSetChanged()
        notifyListDataSetChanged()
        return this
    }

}