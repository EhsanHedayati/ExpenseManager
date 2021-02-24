package com.mylab.expensemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditDeleteViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpense(expense)
        }
    }


}