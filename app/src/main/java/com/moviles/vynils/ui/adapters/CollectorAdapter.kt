package com.moviles.vynils.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviles.vynils.R
import com.moviles.vynils.databinding.CollectorItemBinding
import com.moviles.vynils.models.Collector


class CollectorAdapter: RecyclerView.Adapter<CollectorAdapter.CollectorViewHolder>() {
    var collectors :List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAdapter.CollectorViewHolder {
        val withDataBinding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorAdapter.CollectorViewHolder.LAYOUT,
            parent,
            false)
        return CollectorAdapter.CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorAdapter.CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectors[position]
        }
    }

    override fun getItemCount(): Int {
        return collectors.size
    }


    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_item
        }

    }
}