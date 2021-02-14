package com.mylab.expensemanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.mylab.expensemanager.datamodel.ExpenseSpec
import kotlinx.android.synthetic.main.spinner_income.view.*

class IncomeExpenseTitleAdapter(context: Context, titleList: List<ExpenseSpec>) :
    ArrayAdapter<ExpenseSpec>(context, 0, titleList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val incomeSpec = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_income, parent, false)

        view.spinnerTextView.text = incomeSpec?.title
        view.spinnerImageView.setImageResource(incomeSpec!!.image)

        return view
    }




}