package com.example.healthyeatingapp.Food

import java.io.Serializable

class DataRecord_Food(
    val name: String,
    val price: Double,
    val calories: Int,
    val fats: Int,
    val protein: Int,
    val carbohydrate: Int,
    val sodium: Int,
    val sugar: Int,
    val date_and_time: String
) : Serializable