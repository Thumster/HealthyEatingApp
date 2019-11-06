package com.example.healthyeatingapp.Wallet

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.healthyeatingapp.R
import com.example.healthyeatingapp.enumeration.TransactionType
import kotlinx.android.synthetic.main.transaction_card.view.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class TransactionAdapter(private val myDataset: ArrayList<DataRecord_Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: LinearLayout) :
        RecyclerView.ViewHolder(cardView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.transaction_card,
                parent,
                false
            ) as LinearLayout

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val transaction: DataRecord_Transaction = myDataset.get(position)

        val transactionType = transaction.transactionType
        val transactionName = transaction.name
        var transactionAmount = ""
        if (transactionType == TransactionType.DEBIT) {
            transactionAmount += "+ S$ "
            holder.cardView.outlineSpotShadowColor = Color.parseColor("#2dff53")
        } else if (transactionType == TransactionType.CREDIT) {
            transactionAmount += "- S$ "
        }
        transactionAmount += String.format("%.2f", transaction.amount)
        val date_and_time = transaction.transactionDate

        holder.cardView.textview_name.text = transactionName
        holder.cardView.textview_type.text = transactionType.toString()
        holder.cardView.textview_amount.text = transactionAmount
        holder.cardView.textview_date.text = date_and_time
        Log.e("HERE", holder.cardView.outlineSpotShadowColor.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}