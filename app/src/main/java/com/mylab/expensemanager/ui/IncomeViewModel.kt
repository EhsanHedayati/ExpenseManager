package com.mylab.expensemanager.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.ChartData
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.mylab.expensemanager.Duration
import com.mylab.expensemanager.datamodel.ChartInfo
import com.mylab.expensemanager.datamodel.DetailsData
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import com.mylab.expensemanager.db.ExpenseRepository
import com.mylab.expensemanager.util.firsDayOfMonth
import com.mylab.expensemanager.util.firsDayOfWeek
import com.mylab.expensemanager.util.firsDayOfYear
import com.mylab.expensemanager.util.getPersianDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.*


private const val TAG = "IncomeViewModel"
class IncomeViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {
    
    private val persianCalendar = PersianCalendar()
    private val day = persianCalendar.persianDay
    private val month = persianCalendar.persianMonthName
    val dayMonth = "$day  $month"


    private val onLineIncome: LiveData<Long> = expenseRepository.totalLiveIncome
    private val onLineExpense: LiveData<Long> = expenseRepository.totalLiveExpense

    private val _balance = MediatorLiveData<Long>()
    val balance : LiveData<Long>
    get() = _balance

    private val _expense = MediatorLiveData<MutableList<ExpenseSpec>>()
    val expense : LiveData<MutableList<ExpenseSpec>>
    get() = _expense


    var isChartDataObserved = false

    private val _chartData = MediatorLiveData<MutableList<ChartInfo>>()
    val chartData : LiveData<MutableList<ChartInfo>>
    get() = _chartData

    val isListEmpty = MediatorLiveData<Boolean>()
    val dateType = MutableLiveData(Duration.WEEK.value)//1 week , 2 month, 3 year


    init {
        _balance.addSource(onLineIncome, this::onChanged)
        _balance.addSource(onLineExpense, this::onChanged)



        _expense.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
                when (dateType.value) {
                    Duration.WEEK.value -> addWeekDataToSpec(it)
                    Duration.MONTH.value -> addMonthDataToSpec(it)
                    Duration.YEAR.value -> addYearDataToSpec(it)
                }
            }
        }

        _expense.addSource(dateType) {
            expenseRepository.readAllIncomeSpec.value?.let {
                when (dateType.value) {
                    Duration.WEEK.value -> addWeekDataToSpec(it)
                    Duration.MONTH.value -> addMonthDataToSpec(it)
                    Duration.YEAR.value -> addYearDataToSpec(it)
                }
            }
        }

        _expense.addSource(expenseRepository.readAllIncomeSpec) {
            when (dateType.value) {
                Duration.WEEK.value -> addWeekDataToSpec(it)
                Duration.MONTH.value -> addMonthDataToSpec(it)
                Duration.YEAR.value -> addYearDataToSpec(it)
            }
        }

        isListEmpty.addSource(expense) {
            isListEmpty.value = it.isNullOrEmpty()
        }



        _chartData.addSource(expenseRepository.readAllIncomeSpec) {
            when (dateType.value) {
                Duration.WEEK.value -> getWeekChartInfo(it)
                Duration.MONTH.value -> getMonthChartInfo(it)
                Duration.YEAR.value -> getYearChartInfo(it)
            }

        }

        _chartData.addSource(expenseRepository.totalLiveIncome) {
            expenseRepository.readAllIncomeSpec.value?.let {
                when (dateType.value) {
                    Duration.WEEK.value -> getWeekChartInfo(it)
                    Duration.MONTH.value -> getMonthChartInfo(it)
                    Duration.YEAR.value -> getYearChartInfo(it)
                }

            }

        }

        _chartData.addSource(dateType) {
            expenseRepository.readAllIncomeSpec.value?.let {
                when (dateType.value) {
                    Duration.WEEK.value -> getWeekChartInfo(it)
                    Duration.MONTH.value -> getMonthChartInfo(it)
                    Duration.YEAR.value -> getYearChartInfo(it)
                }

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
            isChartDataObserved = false
            _chartData.postValue(list)
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
            isChartDataObserved = false
            _chartData.postValue(list)
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
            isChartDataObserved = false
            _chartData.postValue(list)
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
                            val percent = 100 * value / income
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            isChartDataObserved = false
            _expense.postValue(list)

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
                            val percent = 100 * value / income
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            isChartDataObserved = false
            _expense.postValue(list)

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
                            val percent = 100 * value / income
                            expenseSpec.sum = percent
                            list.add(expenseSpec)
                        }

                    } catch (e: NullPointerException) {
                        e.stackTrace
                    }


                }
            }
            _expense.postValue(list)

        }
    }


    private suspend fun yearIncome(
        startDate: Long = firsDayOfYear() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time

    ): Long? {
        return expenseRepository.weekIncome(startDate, endDate)
    }

    private suspend fun yearParametric(
        title: String,
        startDate: Long = firsDayOfYear() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekParametric(title, startDate, endDate)
    }


    private suspend fun monthIncome(
        startDate: Long = firsDayOfMonth() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekIncome(startDate, endDate)
    }

    private suspend fun monthParametric(
        title: String,
        startDate: Long = firsDayOfMonth() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long {
        return expenseRepository.weekParametric(title, startDate, endDate)

    }


    private suspend fun weekIncome(
        startDate: Long = firsDayOfWeek() - 24 * 60 * 60 * 1000,
        endDate: Long = Date().time
    ): Long? {
        return expenseRepository.weekIncome(startDate, endDate)
    }


    private suspend fun weekParametric(
        title: String,
        startDate: Long = firsDayOfWeek() - 24 * 60 * 60 * 1000,
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

        _balance.value = income - expense
    }

}