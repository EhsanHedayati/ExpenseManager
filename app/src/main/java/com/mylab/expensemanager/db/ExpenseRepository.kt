package com.mylab.expensemanager.db

import androidx.lifecycle.LiveData
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec

class ExpenseRepository(private val expenseDao: ExpenseDao) {


    val expenseSpecCount: LiveData<List<ExpenseSpec>> = expenseDao.expenseSpecCount()
    val readAllIncomeSpec: LiveData<List<ExpenseSpec>> = expenseDao.readAllIncomeSpec()
    val readAllExpenseSpec: LiveData<List<ExpenseSpec>> = expenseDao.readAllExpenseSpec()
    val fillIncomeSpinner: LiveData<List<ExpenseSpec>> = expenseDao.fillIncomeSpinner()
    val fillExpenseSpinner: LiveData<List<ExpenseSpec>> = expenseDao.fillExpenseSpinner()
    val totalLiveIncome: LiveData<Long> = expenseDao.totalLiveIncome()
    val totalLiveExpense: LiveData<Long> = expenseDao.totalLiveExpense()


    suspend fun provideTitle(startDate: Long, endDate: Long): List<String> {
        return expenseDao.provideTitle(startDate, endDate)

    }


    suspend fun weekParametric(title: String, startDate: Long, endDate: Long): Long {

        return expenseDao.weekParametric(title, startDate, endDate)
    }

    suspend fun weekExpenseParametric(title: String, startDate: Long, endDate: Long): Long {
        return expenseDao.weekExpenseParametric(title, startDate, endDate)
    }


    fun incomeTitleList(): LiveData<List<String>> {
        return expenseDao.incomeTitleList()

    }


    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)

    }

    suspend fun insertAllExpenseSpec(objects: List<ExpenseSpec>) {
        expenseDao.insertAllExpenseSpec(objects)
    }

    /*fun totalLiveIncome(): LiveData<Long> {

        return expenseDao.totalLiveIncome()
    }

    fun totalLiveExpense(): LiveData<Long> {
        return expenseDao.totalLiveExpense()
    }*/


    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)

    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)

    }

    suspend fun deleteAllExpense() {
        expenseDao.deleteAllExpense()

    }


    suspend fun expenseDateRangeQuery(startDate: Long, endDate: Long): List<Expense> {
        return expenseDao.expenseDateRangeQuery(startDate, endDate)

    }

    suspend fun incomeDateRangeQuery(startDate: Long, endDate: Long): List<Expense> {
        return expenseDao.incomeDateRangeQuery(startDate, endDate)

    }

    suspend fun incomeSumQuery(startDate: Long, endDate: Long): Long {
        return expenseDao.incomeSumQuery(startDate, endDate)
    }

    suspend fun expenseSumQuery(startDate: Long, endDate: Long): Long {
        return expenseDao.expenseSumQuery(startDate, endDate)
    }


    suspend fun weekIncome(
        startDate: Long,
        endDate: Long
    ): Long? {
        return expenseDao.weekIncome(startDate, endDate)
    }

    suspend fun weekExpense(
        startDate: Long,
        endDate: Long
    ): Long? {
        return expenseDao.weekExpense(startDate, endDate)
    }


    fun weekPay(
        startDate: Long,
        endDate: Long
    ): LiveData<Long> {
        return expenseDao.weekPay(startDate, endDate)
    }


}