package com.example.healthyeatingapp.Points

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DBHelper_Points(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = TableInfo_Points.TABLE_NAME + ".db"
        var totalPoints: Int = 0

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TableInfo_Points.TABLE_NAME + "(" +
                    TableInfo_Points.COLUMN_POINTSNAME + " TEXT," +
                    TableInfo_Points.COLUMN_POINTSAMOUNT + " INTEGER," +
                    TableInfo_Points.COLUMN_DATE_AND_TIME + " TEXT PRIMARY KEY" + ")"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TableInfo_Points.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    fun clearDatabase() {
        val db = writableDatabase

        val clearDBQuery: String = "DELETE FROM " + TableInfo_Points.TABLE_NAME
        db.execSQL(clearDBQuery)
    }

    fun insertPoints(points: DataRecord_Points): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(TableInfo_Points.COLUMN_POINTSNAME, points.name)
        values.put(TableInfo_Points.COLUMN_POINTSAMOUNT, points.amount)
        values.put(TableInfo_Points.COLUMN_DATE_AND_TIME, points.date_and_time)

        val newRowId = db.insert(TableInfo_Points.TABLE_NAME, null, values)
        totalPoints += points.amount
        return true
    }

    fun readAllPoints(): ArrayList<DataRecord_Points> {
        val points = ArrayList<DataRecord_Points>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + TableInfo_Points.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var amount: Int
        var date_and_time: String
        totalPoints = 0
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(TableInfo_Points.COLUMN_POINTSNAME))
                amount = cursor.getInt(cursor.getColumnIndex(TableInfo_Points.COLUMN_POINTSAMOUNT))
                date_and_time =
                    cursor.getString(cursor.getColumnIndex(TableInfo_Points.COLUMN_DATE_AND_TIME))
                points.add(
                    DataRecord_Points(
                        name,
                        amount,
                        date_and_time
                    )
                )

                totalPoints += amount
                cursor.moveToNext()
            }
        }
        return ArrayList(points.sortedByDescending { dateTimeStrToLocalDateTime(selector(it)) })
    }

    fun selector(p: DataRecord_Points): String = p.date_and_time

    fun dateTimeStrToLocalDateTime(input: String): LocalDateTime =
        LocalDateTime.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
}