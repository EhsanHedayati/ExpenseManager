package com.mylab.expensemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoundedDialogTwoViewModel(private val expenseRepository: ExpenseRepository): ViewModel() {

    var title: String? = null


    fun deleteExpenseSpec(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpenseSpec(title)
        }
    }

    fun deleteExpense(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpense(title)
        }
    }


}