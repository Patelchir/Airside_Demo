package com.example.airside_demo.bind

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@Suppress("UNCHECKED_CAST")
abstract class GenericRecyclerViewAdapter<T, D> (
    val context: Context,
    private var mArrayList: ArrayList<T>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    abstract val layoutResId: Int

    abstract fun onBindData(model: T, position: Int, dataBinding: D)

    abstract fun onItemClick(
        model: T,
        position: Int,
        mDataBinding: ViewDataBinding
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutResId,
            parent,
            false
        )
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(
            mArrayList!![position], holder.adapterPosition,
            dataBinding = (holder as GenericRecyclerViewAdapter<*, *>.ItemViewHolder).mDataBinding as D
        )

        (holder.mDataBinding as ViewDataBinding).root.setOnClickListener {
            onItemClick(
                mArrayList!!.get(position),
                position, holder.mDataBinding as ViewDataBinding
            )
        }
    }

    override fun getItemCount(): Int {
        return mArrayList!!.size
    }


    fun getItem(position: Int): T {
        return mArrayList!![position]
    }
    internal inner class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var mDataBinding: D = binding as D

    }
}