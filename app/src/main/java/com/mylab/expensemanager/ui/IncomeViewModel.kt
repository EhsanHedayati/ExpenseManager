package com.mylab.expensemanager.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.ChartData
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.datamodel.ChartInfo
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.*

class IncomeViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {
    private val TAG = "IncomeViewModel"

    private val persianCalendar = PersianCalendar()
    private val day = persianCalendar.persianDay
    private val month = persianCalendar.persianMonthName
    val dayMonth = "$day  $month"


    private val onLineIncome: LiveData<Long> = expenseRepository.totalLiveIncome
    private val onLineExpense: LiveData<Long> = expenseRepository.totalLiveExpense
    val balance = MediatorLiveData<Long>()

    val expense = MediatorLiveData<MutableList<ExpenseSpec>>()
    val weekExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val monthExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val yearExpenseSpecs = MediatorLiveData<MutableList<ExpenseSpec>>()
    val weekChartData = MediatorLiveData<MutableList<ChartInfo>>()
    val monthChartData = MediatorLiveData<MutableList<ChartInfo>>()
    val yearChartData = MediatorLiveData<MutableList<ChartInfo>>()
    val isListEmpty = MediatorLiveData<Boolean>()
    val dateType = MutableLiveData(1)//1 week , 2 month, 3 year



    init {
        balance.addSource(onLineIncome, this::onChanged)
        balance.addSource(onLineExpense, this::onChanged)

        weekExpenseSpecs.addSource(expenseRepository.readAllIncomeSpec) {
            addWeekDataToSpec(it)
        }
        weekExpenseSpecs.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
                addWeekDataToSpec(it)
            }
        }

        expense.addSource(expenseRepository.readAllIncomeSpec){
            when(dateType.value){
                1->addWeekDataToSpec(it)
                2->addMonthDataToSpec(it)
                3->addYearDataToSpec(it)
            }
        }
        monthExpenseSpecs.addSource(expenseRepository.readAllIncomeSpec) {
            addMonthDataToSpec(it)
        }
        monthExpenseSpecs.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
                addMonthDataToSpec(it)
            }
        }

        yearExpenseSpecs.addSource(expenseRepository.readAllIncomeSpec) {
            addYearDataToSpec(it)

        }
        yearExpenseSpecs.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
                addYearDataToSpec(it)
            }

        }

        weekChartData.addSource(expenseRepository.readAllIncomeSpec) {
            getWeekChartInfo(it)
        }
        weekChartData.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
                getWeekChartInfo(it)
            }
        }

        monthChartData.addSource(expenseRepository.readAllIncomeSpec) {
            getMonthChartInfo(it)
        }
        monthChartData.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
                getMonthChartInfo(it)
            }
        }

        yearChartData.addSource(expenseRepository.readAllIncomeSpec) {
            getYearChartInfo(it)
        }
        yearChartData.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
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


    private fun addYearDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val income = yearIncome()
            if (income != null) {
                it.forEach { expenseSpec ->

                    try {

                        val value = yearParametric(expenseSpec.title)

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
            expense.postValue(list)

        }


    }

    private fun addMonthDataToSpec(it: List<ExpenseSpec>) {

        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<ExpenseSpec>()
            val income = monthIncome()
            if (income != null) {
                it.forEach { expenseSpec ->

                    try {

                        val value = monthParametric(expenseSpec.title)

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
            expense.postValue(list)

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
            expense.postValue(list)

        }
    }


    private fun oneYearAgo(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        return cal.timeInMillis
    }

    private suspend fun yearIncome(
        startDate: Long = oneYearAgo(),
        endDate: Long = Date().time

    ): Long? {
        return expenseRepository.weekIncome(startDate, endDate)
    }

    private suspend fun yearParametric(
        title: String,
        startDate: Long = oneYearAgo(),
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekParametric(title, startDate, endDate)
    }


    private fun oneMonthAgo(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        return cal.timeInMillis
    }


    private suspend fun monthIncome(
        startDate: Long = oneMonthAgo(),
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekIncome(startDate, endDate)
    }

    private suspend fun monthParametric(
        title: String,
        startDate: Long = oneMonthAgo(),
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekParametric(title, startDate, endDate)
    }


    private fun oneWeekAgo(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_MONTH, -1)
        return cal.timeInMillis

    }

    //Date().time - 7 * 24 * 60 * 60 * 1000
    private suspend fun weekIncome(
        startDate: Long = oneWeekAgo(),
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