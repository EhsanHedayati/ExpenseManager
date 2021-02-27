package com.mylab.expensemanager.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteIncomeExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    val expenseSpec = MutableLiveData<ExpenseSpec>()


    fun existenceExpenseSpec(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val receivedExpenseSpec = expenseRepository.existenceExpenseSpec(title)
            expenseSpec.postValue(receivedExpenseSpec)
        }
    }

    /*fun deleteExpenseSpec(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpenseSpec(title)
        }
    }

    fun deleteExpense(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpense(title)
        }
    }*/


}