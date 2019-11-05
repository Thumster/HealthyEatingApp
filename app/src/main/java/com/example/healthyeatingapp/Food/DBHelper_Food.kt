package com.example.healthyeatingapp.Food

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper_Food(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = TableInfo_Food.TABLE_NAME + ".db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TableInfo_Food.TABLE_NAME + "(" +
                    TableInfo_Food.COLUMN_FOODNAME + " TEXT," +
                    TableInfo_Food.COLUMN_FOODPRICE + " DOUBLE," +
                    TableInfo_Food.COLUMN_CALORIES + " INTEGER," +
                    TableInfo_Food.COLUMN_FATS + " INTEGER," +
                    TableInfo_Food.COLUMN_PROTEIN + " INTEGER," +
                    TableInfo_Food.COLUMN_CARBOHYDRATE + " INTEGER," +
                    TableInfo_Food.COLUMN_SODIUM + " INTEGER," +
                    TableInfo_Food.COLUMN_SUGAR + " INTEGER," +
                    TableInfo_Food.COLUMN_DATE_AND_TIME + " TEXT PRIMARY KEY" + ")"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TableInfo_Food.TABLE_NAME
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

        val clearDBQuery: String = "DELETE FROM " + TableInfo_Food.TABLE_NAME
        db.execSQL(clearDBQuery)
    }

    fun insertFood(food: DataRecord_Food): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(TableInfo_Food.COLUMN_FOODNAME, food.name)
        values.put(TableInfo_Food.COLUMN_FOODPRICE, food.price)
        values.put(TableInfo_Food.COLUMN_CALORIES, food.calories)
        values.put(TableInfo_Food.COLUMN_FATS, food.fats)
        values.put(TableInfo_Food.COLUMN_PROTEIN, food.protein)
        values.put(TableInfo_Food.COLUMN_CARBOHYDRATE, food.carbohydrate)
        values.put(TableInfo_Food.COLUMN_SODIUM, food.sodium)
        values.put(TableInfo_Food.COLUMN_SUGAR, food.sugar)
        values.put(TableInfo_Food.COLUMN_DATE_AND_TIME, food.date_and_time)

        val newRowId = db.insert(TableInfo_Food.TABLE_NAME, null, values)
        return true
    }

    fun readAllFoods(): ArrayList<DataRecord_Food> {
        val foods = ArrayList<DataRecord_Food>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + TableInfo_Food.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var price: Double
        var calories: Int
        var fats: Int
        var protein: Int
        var carbohydrate: Int
        var sodium: Int
        var sugar: Int
        var date_and_time: String
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(TableInfo_Food.COLUMN_FOODNAME))
                price = cursor.getDouble(cursor.getColumnIndex(TableInfo_Food.COLUMN_FOODPRICE))
                calories = cursor.getInt(cursor.getColumnIndex(TableInfo_Food.COLUMN_CALORIES))
                fats = cursor.getInt(cursor.getColumnIndex(TableInfo_Food.COLUMN_FATS))
                protein = cursor.getInt(cursor.getColumnIndex(TableInfo_Food.COLUMN_PROTEIN))
                carbohydrate =
                    cursor.getInt(cursor.getColumnIndex(TableInfo_Food.COLUMN_CARBOHYDRATE))
                sodium = cursor.getInt(cursor.getColumnIndex(TableInfo_Food.COLUMN_SODIUM))
                sugar = cursor.getInt(cursor.getColumnIndex(TableInfo_Food.COLUMN_SUGAR))
                date_and_time =
                    cursor.getString(cursor.getColumnIndex(TableInfo_Food.COLUMN_DATE_AND_TIME))
                foods.add(
                    DataRecord_Food(
                        name,
                        price,
                        calories,
                        fats,
                        protein,
                        carbohydrate,
                        sodium,
                        sugar,
                        date_and_time
                    )
                )
                cursor.moveToNext()
            }
        }
        return foods
    }
}