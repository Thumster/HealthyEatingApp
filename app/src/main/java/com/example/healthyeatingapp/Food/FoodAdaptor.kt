package com.example.healthyeatingapp.Points

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.R
import kotlinx.android.synthetic.main.food_card.view.*
import kotlinx.android.synthetic.main.transaction_card.view.*
import kotlinx.android.synthetic.main.transaction_card.view.textview_date
import kotlinx.android.synthetic.main.transaction_card.view.textview_name

class FoodAdapter(private val myDataset: ArrayList<DataRecord_Food>) :
    RecyclerView.Adapter<FoodAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: LinearLayout) :
        RecyclerView.ViewHolder(cardView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.food_card,
                parent,
                false
            ) as LinearLayout

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val food: DataRecord_Food = myDataset.get(position)

        val foodName = food.name
        val foodCalories = food.calories.toString() + " KCAL"
        val foodCarbohydrates = food.carbohydrate.toString() + " g"
        val foodProtein = food.protein.toString() + " g"
        val foodFats = food.fats.toString() + " g"
        val foodSugar = food.sugar.toString() + " g"
        val foodSodium = food.sugar.toString() + " mg"
        val date_and_time = food.date_and_time

        holder.cardView.textview_name.text = foodName
        holder.cardView.textview_caloriesValue.text = foodCalories
        holder.cardView.textview_carbohydratesValue.text = foodCarbohydrates
        holder.cardView.textview_proteinValue.text = foodProtein
        holder.cardView.textview_fatsValue.text = foodFats
        holder.cardView.textview_sugarValue.text = foodSugar
        holder.cardView.textview_sodiumValue.text = foodSodium
        holder.cardView.textview_date.text = date_and_time
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}