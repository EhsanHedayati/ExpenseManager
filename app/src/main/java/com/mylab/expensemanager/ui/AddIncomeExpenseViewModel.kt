package com.mylab.expensemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.R
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddIncomeExpenseViewModel(val expenseRepository: ExpenseRepository) : ViewModel() {


    fun getExtraIncome(): List<Int> {
        val extraIncome = mutableListOf<Int>()
        extraIncome.add(R.drawable.ic_icon_business)
        extraIncome.add(R.drawable.ic_icon_agriculture)
        extraIncome.add(R.drawable.ic_icon_invention)
        extraIncome.add(R.drawable.ic_icon_brokerage)
        return extraIncome
    }

    fun getExtraExpense(): List<Int> {
        val extraExpense = mutableListOf<Int>()
        extraExpense.add(R.drawable.ic_icon_restaurant)
        extraExpense.add(R.drawable.ic_icon_travel)
        extraExpense.add(R.drawable.ic_icon_decoration)
        extraExpense.add(R.drawable.ic_icon_education)
        return extraExpense
    }

    fun insertExpenseSpec(expenseSpec: ExpenseSpec) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpenseSpec(expenseSpec)
        }
    }
}