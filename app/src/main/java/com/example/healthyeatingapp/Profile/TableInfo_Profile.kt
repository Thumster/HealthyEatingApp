package com.example.healthyeatingapp.Profile

import android.provider.BaseColumns

class TableInfo_Profile : BaseColumns {
    companion object {
        val TABLE_NAME = "profile"
        val COLUMN_NAME = "profile_name"
        val COLUMN_AGE = "profile_age"
        val COLUMN_GENDER = "profile_gender"
        val COLUMN_WEIGHT = "profile_weight"
        val COLUMN_HEIGHT = "profile_height"
    }
}