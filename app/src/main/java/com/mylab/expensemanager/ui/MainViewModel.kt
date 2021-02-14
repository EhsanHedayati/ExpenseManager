package com.mylab.expensemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    val readAllExpenseSpec: LiveData<List<ExpenseSpec>> = expenseRepository.readAllExpenseSpec

    private val onLineIncome: LiveData<Long> = expenseRepository.totalLiveIncome()
    private val onLineExpense: LiveData<Long> = expenseRepository.totalLiveExpense()
    val balance = MediatorLiveData<Long>()

    init {
        balance.addSource(onLineIncome, this::onChanged)
        balance.addSource(onLineExpense, this::onChanged)
    }


    fun insertAllExpenseSpec(objects: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertAllExpenseSpec(objects)
        }

    }

    private fun onChanged(ignored: Long?) {
        var income = onLineIncome.value
        var expense = onLineExpense.value

        // correct value or throw exception if appropriate
        if (income == null) {
            income = 0
        }
        if (expense == null) {
            expense = 0
        }

        balance.value = income - expense
    }







}