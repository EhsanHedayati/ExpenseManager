package com.mylab.expensemanager.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec


@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    fun readAllExpense(): LiveData<List<Expense>>

    @Query("DELETE FROM expense_table")
    suspend fun deleteAllExpense()


    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 0")
    suspend fun expenseDateRangeQuery(startDate: Long, endDate: Long): List<Expense>

    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    suspend fun incomeDateRangeQuery(startDate: Long, endDate: Long): List<Expense>

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    suspend fun incomeSumQuery(startDate: Long, endDate: Long): Long

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 0")
    suspend fun expenseSumQuery(startDate: Long, endDate: Long): Long

    @Query("SELECT SUM(amount) FROM expense_table WHERE amountType = 1")
    fun totalLiveIncome(): LiveData<Long>

    @Query("SELECT SUM(amount) FROM expense_table WHERE amountType = 0")
    fun totalLiveExpense(): LiveData<Long>

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    suspend fun weekIncome(startDate: Long, endDate: Long): Long?

    @Query("SELECT SUM(amount) FROM expense_table WHERE title = 'حقوق' AND date BETWEEN :startDate AND :endDate")
    fun weekPay(startDate: Long, endDate: Long): LiveData<Long>

    @Query("SELECT * FROM expense_spec_table")
    fun readAllExpenseSpec(): LiveData<List<ExpenseSpec>>

    @Query("SELECT * FROM expense_spec_table WHERE expenseType = 1")
    fun fillIncomeSpinner(): LiveData<List<ExpenseSpec>>

    @Query("SELECT * FROM expense_spec_table WHERE expenseType = 0")
    fun fillExpenseSpinner(): LiveData<List<ExpenseSpec>>

    @Insert
    @JvmSuppressWildcards
    suspend fun insertAllExpenseSpec(objects: List<ExpenseSpec>)

    /*@Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate AND amountType = 1")
    fun weekIncomeList(startDate: Long, endDate: Long): LiveData<List<Expense>>*/

    @Query("SELECT title FROM expense_spec_table WHERE expenseType = 1")
    fun incomeTitleList(): LiveData<List<String>>

    @Query("SELECT SUM(amount) FROM expense_table WHERE title = :title AND date BETWEEN :startDate AND :endDate")
    suspend fun weekParametric(title: String, startDate: Long, endDate: Long): Long


}