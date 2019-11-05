package com.example.healthyeatingapp.Wallet

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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
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
        var transactionAmount = ""
        if (transactionType == TransactionType.DEBIT) {
            transactionAmount += "+ S$ "
        } else {
            transactionAmount += "- S$ "
        }
        transactionAmount += transaction.amount.toString()
        val date_and_time = transaction.transactionDate

        holder.cardView.textview_type.text = transactionType.toString()
        holder.cardView.textview_amount.text = transactionAmount
        holder.cardView.textview_date.text = date_and_time
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}