package dingshaoshuai.base.mvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class DataBindingViewHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)

abstract class BaseDataBindingAdapter<T, E : ViewDataBinding> :
    RecyclerView.Adapter<DataBindingViewHolder<E>>() {
    var itemClickListener: ItemClickListener<T>? = null
    var itemChildClickListener: ItemChildClickListener<T>? = null

    protected abstract val layoutId: Int
    protected abstract fun onBind(binding: E, data: T, position: Int)

    var dataList: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<E> {
        val binding: E = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId, parent, false
        )
        return DataBindingViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList?.size ?: 0

    override fun onBindViewHolder(holder: DataBindingViewHolder<E>, position: Int) {
        dataList ?: return
        onBind(holder.binding, dataList!![position], position)
        holder.binding.executePendingBindings()
    }

    interface ItemClickListener<T> {
        fun onItemClick(data: T)
    }

    interface ItemChildClickListener<T> {
        fun onItemChildClick(view: View, data: T)
    }
}