package com.example.healthyeatingapp.Points

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.healthyeatingapp.R
import kotlinx.android.synthetic.main.transaction_card.view.*

class PointsAdapter(private val myDataset: ArrayList<DataRecord_Points>) :
    RecyclerView.Adapter<PointsAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: LinearLayout) :
        RecyclerView.ViewHolder(cardView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PointsAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.points_card,
                parent,
                false
            ) as LinearLayout

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val point: DataRecord_Points = myDataset.get(position)

        val pointsName = point.name
        var pointsAmount = point.amount
        var pointAmountStr = ""
        if (point.amount > 0) {
            pointAmountStr += "+ "
        } else if (point.amount < 0) {
            pointAmountStr += "  "
        }
        pointAmountStr += pointsAmount
        val date_and_time = point.date_and_time

        holder.cardView.textview_name.text = pointsName
        holder.cardView.textview_amount.text = pointAmountStr
        holder.cardView.textview_date.text = date_and_time
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}