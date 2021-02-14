package com.mylab.expensemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.db.ExpenseRepository

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    private val persianCalendar = PersianCalendar()
    private val day = persianCalendar.persianDay
    private val month = persianCalendar.persianMonthName
    val dayMonth = "$day  $month"

    private val onLineIncome: LiveData<Long> = expenseRepository.totalLiveIncome()
    private val onLineExpense: LiveData<Long> = expenseRepository.totalLiveExpense()
    val balance = MediatorLiveData<Long>()

    init {
        balance.addSource(onLineIncome, this::onChanged)
        balance.addSource(onLineExpense, this::onChanged)
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