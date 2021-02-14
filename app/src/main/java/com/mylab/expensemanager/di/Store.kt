package com.mylab.expensemanager.di

import android.app.Application
import androidx.room.Room
import com.mylab.expensemanager.db.ExpenseDao
import com.mylab.expensemanager.db.ExpenseDatabase


fun provideDatabase(application: Application): ExpenseDatabase {
    return Room.databaseBuilder(
        application,
        ExpenseDatabase::class.java,
        "expense_database"
    ).build()

}

fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
    return database.expenseDao()
}

