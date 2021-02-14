package com.mylab.expensemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeEntryViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {


    val fillIncomeSpinner: LiveData<List<ExpenseSpec>> = expenseRepository.fillIncomeSpinner

    fun insertExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpense(expense)
        }

    }







}