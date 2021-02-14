package com.mylab.expensemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseEntryViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    val fillExpenseSpinner: LiveData<List<ExpenseSpec>> = expenseRepository.fillExpenseSpinner

    fun insertExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpense(expense)
        }
    }


}