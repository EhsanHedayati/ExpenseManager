package com.mylab.expensemanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mylab.expensemanager.databinding.IncomeListItemBinding
import com.mylab.expensemanager.datamodel.ExpenseSpec

class IncomeListAdapter :
    ListAdapter<ExpenseSpec, IncomeListAdapter.IncomeViewHolder>(IncomeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = IncomeListItemBinding.inflate(layoutInflater)
        return IncomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    class IncomeViewHolder(val binding: IncomeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val circleImage = binding.listIncomeImage
        private val titleTextView = binding.listIncomeTitle
        private val percentTextView = binding.listIncomePercent
        private val progressBar = binding.listIncomeProgress


        fun bind(expenseSpec: ExpenseSpec) {


            circleImage.setImageResource(expenseSpec.image)
            titleTextView.text = expenseSpec.title
            percentTextView.text = String.format("%d%1s", expenseSpec.sum,"%")
            progressBar.progress = expenseSpec.sum.toInt()


        }
    }

    class IncomeDiffUtil : DiffUtil.ItemCallback<ExpenseSpec>() {
        override fun areItemsTheSame(oldItem: ExpenseSpec, newItem: ExpenseSpec): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ExpenseSpec, newItem: ExpenseSpec): Boolean {
            return oldItem == newItem
        }

    }


}
