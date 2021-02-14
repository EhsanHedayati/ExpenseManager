package com.mylab.expensemanager.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class IncomeViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {
private val TAG = "IncomeViewModel"

    private val persianCalendar = PersianCalendar()
    private val day = persianCalendar.persianDay
    private val month = persianCalendar.persianMonthName
    val dayMonth = "$day  $month"


    private val onLineIncome: LiveData<Long> = expenseRepository.totalLiveIncome()
    private val onLineExpense: LiveData<Long> = expenseRepository.totalLiveExpense()
    val balance = MediatorLiveData<Long>()

    val expenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()


    init {
        balance.addSource(onLineIncome, this::onChanged)
        balance.addSource(onLineExpense, this::onChanged)

        expenseSpecs.addSource(expenseRepository.readAllExpenseSpec) {
            addWeekDataToSpec(it)
        }

        expenseSpecs.addSource(expenseRepository.totalLiveExpense()){
            expenseRepository.readAllExpenseSpec.value?.let {
                addWeekDataToSpec(it)
            }
        }

    }

    private fun addWeekDataToSpec(it: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val income = weekIncome()
            if (income != null) {
                it.forEach { expenseSpec ->

                    try {
                        val value = weekParametric(expenseSpec.title)

                        if (value > 0) {
                            Log.i(TAG, "viewList: $value")
                            val percent = 100 * value / income
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            expenseSpecs.postValue(list)

        }
    }


    private suspend fun weekIncome(
        startDate: Long = Date().time - 7 * 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekIncome(startDate, endDate)
    }


    private suspend fun weekParametric(
        title: String,
        startDate: Long = Date().time - 7 * 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long {

        return expenseRepository.weekParametric(title, startDate, endDate)
    }

    /*private fun weekIncome(
        startDate: Long = Date().time - 7 * 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ) {
        weekIncome = expenseRepository.weekIncome(startDate, endDate)
    }*/


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