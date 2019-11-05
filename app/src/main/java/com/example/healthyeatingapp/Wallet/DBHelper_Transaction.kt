package com.example.healthyeatingapp.Wallet

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.healthyeatingapp.enumeration.TransactionType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DBHelper_Transaction(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "walletTransaction.db"
        var balance: Double = 0.0

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TableInfo_Transaction.TABLE_NAME + "(" +
                    TableInfo_Transaction.COLUMN_TRANSACTIONTYPE + " TEXT," +
                    TableInfo_Transaction.COLUMN_AMOUNT + " DOUBLE," +
                    TableInfo_Transaction.COLUMN_DATE_AND_TIME + " TEXT PRIMARY KEY" + ")"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TableInfo_Transaction.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DBHelper_Transaction.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DBHelper_Transaction.SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    fun clearDatabase() {
        val db = writableDatabase

        val clearDBQuery: String = "DELETE FROM " + TableInfo_Transaction.TABLE_NAME
        db.execSQL(clearDBQuery)
        balance = 0.00
    }

    fun insertTransaction(transaction: DataRecord_Transaction): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(
            TableInfo_Transaction.COLUMN_TRANSACTIONTYPE,
            transaction.transactionType.toString()
        )
        values.put(TableInfo_Transaction.COLUMN_AMOUNT, transaction.amount)
        values.put(TableInfo_Transaction.COLUMN_DATE_AND_TIME, transaction.transactionDate)

        if (transaction.transactionType == TransactionType.DEBIT) {
            balance += transaction.amount
        } else {
            balance -= transaction.amount
        }

        val newRowId = db.insert(TableInfo_Transaction.TABLE_NAME, null, values)
        return true
    }

    fun readAllTransactions(): ArrayList<DataRecord_Transaction> {
        val transactions = ArrayList<DataRecord_Transaction>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + TableInfo_Transaction.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(DBHelper_Transaction.SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var type: TransactionType
        var amount: Double
        var date_and_time: String
        balance = 0.00
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                type = TransactionType.valueOf(
                    cursor.getString(
                        cursor.getColumnIndex(TableInfo_Transaction.COLUMN_TRANSACTIONTYPE)
                    )
                )
                amount =
                    cursor.getDouble(cursor.getColumnIndex(TableInfo_Transaction.COLUMN_AMOUNT))
                date_and_time =
                    cursor.getString(cursor.getColumnIndex(TableInfo_Transaction.COLUMN_DATE_AND_TIME))
                transactions.add(
                    DataRecord_Transaction(
                        type,
                        amount,
                        date_and_time
                    )
                )

                if (type == TransactionType.DEBIT) {
                    balance += amount
                } else {
                    balance -= amount
                }

                cursor.moveToNext()
            }
        }

        return ArrayList(transactions.sortedByDescending { dateTimeStrToLocalDateTime(selector(it)) })
    }

    fun selector(t: DataRecord_Transaction): String = t.transactionDate

    fun dateTimeStrToLocalDateTime(input: String): LocalDateTime =
        LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))


}