package com.example.healthyeatingapp.Profile

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.healthyeatingapp.enumeration.Gender
import java.time.Period
import java.time.Year

class DBHelper_Profile(context: Context) :
    SQLiteOpenHelper(
        context,
        DBHelper_Profile.DATABASE_NAME, null,
        DBHelper_Profile.DATABASE_VERSION
    ) {
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = TableInfo_Profile.TABLE_NAME + ".db"
        var BMR: Int = 0
        val emptyProfile = DataRecord_Profile("", 0, Gender.MALE, 0.00, 0.00)
        var user: DataRecord_Profile = emptyProfile

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TableInfo_Profile.TABLE_NAME + "(" +
                    TableInfo_Profile.COLUMN_NAME + " TEXT PRIMARY KEY," +
                    TableInfo_Profile.COLUMN_AGE + " INTEGER," +
                    TableInfo_Profile.COLUMN_GENDER + " TEXT," +
                    TableInfo_Profile.COLUMN_WEIGHT + " DOUBLE," +
                    TableInfo_Profile.COLUMN_HEIGHT + " DOUBLE" + ")"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TableInfo_Profile.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DBHelper_Profile.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DBHelper_Profile.SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    fun clearDatabase() {
        val db = writableDatabase

        val clearDBQuery: String = "DELETE FROM " + TableInfo_Profile.TABLE_NAME
        db.execSQL(clearDBQuery)
        user = emptyProfile
    }

    fun createProfile(profile: DataRecord_Profile): Boolean {
        clearDatabase()
        val db = writableDatabase
        val values = ContentValues()

        values.put(TableInfo_Profile.COLUMN_NAME, profile.name)
        values.put(TableInfo_Profile.COLUMN_AGE, profile.age)
        values.put(TableInfo_Profile.COLUMN_GENDER, profile.gender.toString())
        values.put(TableInfo_Profile.COLUMN_WEIGHT, profile.weight)
        values.put(TableInfo_Profile.COLUMN_HEIGHT, profile.height)

        val newRowId = db.insert(TableInfo_Profile.TABLE_NAME, null, values)
        BMR = calculateBMR(profile)
        user = profile
        return true
    }

    fun getProfile(): DataRecord_Profile {
        val db = writableDatabase
        var cursor: Cursor? = null
        var profile = emptyProfile

        try {
            cursor = db.rawQuery("select * from " + TableInfo_Profile.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(DBHelper_Profile.SQL_CREATE_ENTRIES)
            return profile
        }
        var name: String
        var age: Int
        var gender: String
        var weight: Double
        var height: Double
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(TableInfo_Profile.COLUMN_NAME))
                age = cursor.getInt(cursor.getColumnIndex(TableInfo_Profile.COLUMN_AGE))
                gender = cursor.getString(cursor.getColumnIndex(TableInfo_Profile.COLUMN_GENDER))
                weight = cursor.getDouble(cursor.getColumnIndex(TableInfo_Profile.COLUMN_WEIGHT))
                height = cursor.getDouble(cursor.getColumnIndex(TableInfo_Profile.COLUMN_HEIGHT))
                profile = DataRecord_Profile(
                    name,
                    age,
                    Gender.valueOf(gender),
                    weight,
                    height
                )
                cursor.moveToNext()
            }
        }
        user = profile
        BMR = calculateBMR(profile)
        return profile
    }

    fun calculateBMR(profile: DataRecord_Profile): Int {
//        Men BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) - (5.677 x age in years)
//        Women BMR = 447.593 + (9.247 x weight in kg) + (3.098 x height in cm) - (4.330 x age in years)
        var age = profile.age
        var height = profile.height
        var weight = profile.weight
        var gender = profile.gender
        var BMR: Double

        if (gender == Gender.MALE) {
            BMR = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            BMR = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }

        return BMR.toInt()
    }
}