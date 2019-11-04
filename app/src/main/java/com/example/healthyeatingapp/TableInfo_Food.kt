package com.example.healthyeatingapp

import android.provider.BaseColumns

class TableInfo_Food : BaseColumns {

    companion object {
        val TABLE_NAME          = "food"
        val COLUMN_FOODNAME     = "food_name"
        val COLUMN_FOODPRICE    = "food_price"
        val COLUMN_CALORIES     = "food_calories"
        val COLUMN_FATS         = "food_fats"
        val COLUMN_PROTEIN      = "food_protein"
        val COLUMN_CARBOHYDRATE = "food_carbohydrate"
        val COLUMN_SODIUM       = "food_sodium"
        val COLUMN_SUGAR        = "food_sugar"
        val COLUMN_DATE_AND_TIME = "date_and_time"
    }

}