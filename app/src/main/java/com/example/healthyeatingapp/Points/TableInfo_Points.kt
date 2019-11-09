package com.example.healthyeatingapp.Points

import android.provider.BaseColumns

class TableInfo_Points : BaseColumns {
    companion object {
        val TABLE_NAME = "points"
        val COLUMN_POINTSNAME = "points_name"
        val COLUMN_POINTSAMOUNT = "points_amount"
        val COLUMN_DATE_AND_TIME = "date_and_time"
    }
}