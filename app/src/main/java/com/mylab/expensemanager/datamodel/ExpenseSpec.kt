package com.mylab.expensemanager.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expense_spec_table")
data class ExpenseSpec(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val image: Int,
    val expenseType: Int,
    var sum: Long = 0L
)