package com.mylab.expensemanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mylab.expensemanager.databinding.ThirdDetailsListItemBinding
import com.mylab.expensemanager.datamodel.DetailsData
import com.mylab.expensemanager.util.getPersianDate
import kotlinx.android.synthetic.main.third_details_list_item.view.*

class DetailsListAdapter :
    ListAdapter<DetailsData, DetailsListAdapter.ViewHolder>(DetailsDiffUtil()) {

    var itemClick: ((DetailsData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ThirdDetailsListItemBinding.inflate(layoutInflater)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.detailsData = currentList[position]
        /*val detail = currentList[position]
        holder.itemView.list_item_date.text = getPersianDate(detail.date)*/
        holder.binding.executePendingBindings()
    }


    inner class ViewHolder(val binding: ThirdDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.listItemEdit.setOnClickListener {
                itemClick?.invoke(currentList[adapterPosition])
            }
        }

    }

    class DetailsDiffUtil : DiffUtil.ItemCallback<DetailsData>() {
        override fun areItemsTheSame(oldItem: DetailsData, newItem: DetailsData): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: DetailsData, newItem: DetailsData): Boolean {
            return oldItem == newItem
        }

    }


}
