package com.example.healthyeatingapp.Wallet

import com.example.healthyeatingapp.enumeration.TransactionType
import java.io.Serializable
import java.util.*

class DataRecord_Transaction(
    val transactionType: TransactionType,
    val name: String,
    val amount: Double,
    val transactionDate: String
)