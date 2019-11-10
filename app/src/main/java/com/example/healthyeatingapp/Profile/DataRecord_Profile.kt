package com.example.healthyeatingapp.Profile

import com.example.healthyeatingapp.enumeration.Gender
import java.time.Year

class DataRecord_Profile(
    val name: String,
    val age: Int,
    val gender: Gender,
    val weight: Double,
    val height: Double,
    val exercise: Int
)