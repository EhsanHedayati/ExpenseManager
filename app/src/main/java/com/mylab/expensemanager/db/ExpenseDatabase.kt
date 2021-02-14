package com.mylab.expensemanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec

@Database(entities = [Expense::class, ExpenseSpec::class], version = 2, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDataBase(context: Context): ExpenseDatabase {

            var tempInstance = INSTANCE
            if (tempInstance != null) {

                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ExpenseDatabase::class.java,
                    "expense_database"
                ).build()
                INSTANCE = instance
                return instance

            }
        }
    }
}