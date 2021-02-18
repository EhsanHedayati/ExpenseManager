package com.mylab.expensemanager.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.datamodel.ChartInfo
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {
    private val TAG = "ExpenseViewModel"


    private val persianCalendar = PersianCalendar()
    private val day = persianCalendar.persianDay
    private val month = persianCalendar.persianMonthName
    val dayMonth = "$day  $month"

    private val onLineIncome: LiveData<Long> = expenseRepository.totalLiveIncome
    private val onLineExpense: LiveData<Long> = expenseRepository.totalLiveExpense
    val balance = MediatorLiveData<Long>()

    val weekExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val monthExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val yearExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val weekChartData = MediatorLiveData<MutableList<ChartInfo>>()
    val monthChartData = MediatorLiveData<MutableList<ChartInfo>>()
    val yearChartData = MediatorLiveData<MutableList<ChartInfo>>()

    init {
        balance.addSource(onLineIncome, this::onChanged)
        balance.addSource(onLineExpense, this::onChanged)

        weekExpenseSpecs.addSource(expenseRepository.readAllExpenseSpec) {
            addWeekDataToSpec(it)
        }

        weekExpenseSpecs.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                addWeekDataToSpec(it)
            }
        }

        monthExpenseSpecs.addSource(expenseRepository.readAllExpenseSpec) {
            addMonthDataToSpec(it)
        }

        monthExpenseSpecs.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                addMonthDataToSpec(it)
            }
        }

        yearExpenseSpecs.addSource(expenseRepository.readAllExpenseSpec) {
            addYearDataToSpec(it)
        }

        yearExpenseSpecs.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                addYearDataToSpec(it)
            }
        }

        weekChartData.addSource(expenseRepository.readAllExpenseSpec) {
            getWeekChartInfo(it)
        }
        weekChartData.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                getWeekChartInfo(it)
            }
        }

        monthChartData.addSource(expenseRepository.readAllExpenseSpec) {
            getMonthChartInfo(it)
        }
        monthChartData.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                getMonthChartInfo(it)
            }
        }

        yearChartData.addSource(expenseRepository.readAllExpenseSpec) {
            getYearChartInfo(it)
        }
        yearChartData.addSource(expenseRepository.totalLiveExpense) {
            expenseRepository.readAllExpenseSpec.value?.let {
                getYearChartInfo(it)
            }
        }

    }

    private fun getYearChartInfo(it: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ChartInfo>()
            it.forEach { expenseSpec ->
                try {
                    val value = yearParametric(expenseSpec.title)
                    if (value > 0) {
                        val label = expenseSpec.title
                        val chartInfo = ChartInfo(value, label)
                        list.add(chartInfo)
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            yearChartData.postValue(list)
        }


    }

    private fun getMonthChartInfo(it: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ChartInfo>()
            it.forEach { expenseSpec ->
                try {
                    val value = monthParametric(expenseSpec.title)
                    if (value > 0) {
                        val label = expenseSpec.title
                        val chartInfo = ChartInfo(value, label)
                        list.add(chartInfo)
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            monthChartData.postValue(list)
        }

    }

    private fun getWeekChartInfo(it: List<ExpenseSpec>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ChartInfo>()
            it.forEach { expenseSpec ->
                try {
                    val value = weekParametric(expenseSpec.title)
                    if (value > 0) {
                        val label = expenseSpec.title
                        val chartInfo = ChartInfo(value, label)
                        list.add(chartInfo)
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            weekChartData.postValue(list)
        }

    }

    private fun addWeekDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val expense = weekExpense()
            if (expense != null) {
                it.forEach { expenseSpec ->

                    try {
                        val value = weekParametric(expenseSpec.title)
                        if (value > 0) {
                            Log.i(TAG, "viewList: $value")
                            val percent = 100 * value / expense
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            weekExpenseSpecs.postValue(list)

        }

    }


    private fun addMonthDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val expense = monthExpense()
            if (expense != null) {
                it.forEach { expenseSpec ->

                    try {

                        val value = monthParametric(expenseSpec.title)

                        if (value > 0) {
                            Log.i(TAG, "viewList: $value")
                            val percent = 100 * value / expense
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            monthExpenseSpecs.postValue(list)

        }


    }

    private fun addYearDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val expense = yearExpense()
            if (expense != null) {
                it.forEach { expenseSpec ->

                    try {

                        val value = yearParametric(expenseSpec.title)

                        if (value > 0) {
                            Log.i(TAG, "viewList: $value")
                            val percent = 100 * value / expense
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            yearExpenseSpecs.postValue(list)

        }

    }


    private fun oneYearAgo(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        return cal.timeInMillis
    }

    private suspend fun yearExpense(
        startDate: Long = oneYearAgo(),
        endDate: Long = Date().time

    ): Long? {
        return expenseRepository.weekExpense(startDate, endDate)
    }

    private suspend fun yearParametric(
        title: String,
        startDate: Long = oneYearAgo(),
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekExpenseParametric(title, startDate, endDate)
    }

    private fun oneMonthAgo(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        return cal.timeInMillis
    }

    private suspend fun monthExpense(
        startDate: Long = oneMonthAgo(),
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekExpense(startDate, endDate)
    }

    private suspend fun monthParametric(
        title: String,
        startDate: Long = oneMonthAgo(),
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekExpenseParametric(title, startDate, endDate)
    }

    private fun oneWeekAgo(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_MONTH, -1)
        return cal.timeInMillis

    }

    private suspend fun weekExpense(
        startDate: Long = oneWeekAgo(),
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekExpense(startDate, endDate)
    }

    private suspend fun weekParametric(
        title: String,
        startDate: Long = oneWeekAgo(),
        endDate: Long = Date().time
    ): Long {

        return expenseRepository.weekExpenseParametric(title, startDate, endDate)
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