package com.example.healthyeatingapp.Wallet

import android.provider.BaseColumns
import com.example.healthyeatingapp.enumeration.TransactionType
import java.util.*

class TableInfo_Transaction : BaseColumns {

    companion object {
        val TABLE_NAME = "walletTransaction"
        val COLUMN_TRANSACTIONTYPE = "transaction_type"
        val COLUMN_TRANSACTIONNAME = "transaction_name"
        val COLUMN_AMOUNT = "transaction_amount"
        val COLUMN_DATE_AND_TIME = "date_and_time"
    }
}