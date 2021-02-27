package com.mylab.expensemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoundedDialogViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

     var expense : Expense? = null


    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpense(expense)
        }

    }


}