package com.example.healthyeatingapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar

class Fragment_Dashboard : Fragment() {
    private lateinit var goalExpandableView: ConstraintLayout
    private lateinit var goalArrowBtn: Button
    private lateinit var goalCardView: CardView
    private lateinit var progressCalories: RoundCornerProgressBar
    private lateinit var progressCarbohydrates: RoundCornerProgressBar
    private lateinit var progressProtein: RoundCornerProgressBar
    private lateinit var progressFats: RoundCornerProgressBar
    private lateinit var progressSugar: RoundCornerProgressBar
    private lateinit var progressSodium: RoundCornerProgressBar

    private lateinit var pointsExpandableView: ConstraintLayout
    private lateinit var pointsArrowBtn: Button
    private lateinit var pointsCardView: CardView
    private lateinit var foodExpandableView: ConstraintLayout
    private lateinit var foodArrowBtn: Button
    private lateinit var foodCardView: CardView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__dashboard, container, false)
        goalExpandableView = view.findViewById<ConstraintLayout>(R.id.goalExpandableView)
        goalArrowBtn = view.findViewById<Button>(R.id.goalArrowBtn)
        goalCardView = view.findViewById<CardView>(R.id.goalCardView)

        progressCalories =
            view.findViewById(R.id.caloriesProgressBar)
        progressCalories.max = 100.toFloat()
        progressCalories.progress = 80.toFloat()
        progressCalories.secondaryProgress = 90.toFloat()

        progressCarbohydrates =
            view.findViewById(R.id.carbohydratesProgressBar)
        progressCarbohydrates.max = 100.toFloat()
        progressCarbohydrates.progress = 80.toFloat()
        progressCarbohydrates.secondaryProgress = 90.toFloat()

        progressProtein =
            view.findViewById(R.id.proteinProgressBar)
        progressProtein.max = 100.toFloat()
        progressProtein.progress = 80.toFloat()
        progressProtein.secondaryProgress = 90.toFloat()

        progressFats =
            view.findViewById(R.id.fatsProgressBar)
        progressFats.max = 100.toFloat()
        progressFats.progress = 80.toFloat()
        progressFats.secondaryProgress = 90.toFloat()

        progressSugar =
            view.findViewById(R.id.sugarProgressBar)
        progressSugar.max = 100.toFloat()
        progressSugar.progress = 80.toFloat()
        progressSugar.secondaryProgress = 90.toFloat()

        progressSodium =
            view.findViewById(R.id.sodiumProgressBar)
        progressSodium.max = 100.toFloat()
        progressSodium.progress = 80.toFloat()
        progressSodium.secondaryProgress = 90.toFloat()




        goalArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
        goalArrowBtn.setOnClickListener(View.OnClickListener {
            if (goalExpandableView.getVisibility() == View.GONE) {
//                TransitionManager.beginDelayedTransition(goalCardView, AutoTransition())
                goalExpandableView.setVisibility(View.VISIBLE)
                goalArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)
            } else {
//                TransitionManager.beginDelayedTransition(goalCardView, AutoTransition())
                goalExpandableView.setVisibility(View.GONE)
                goalArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
            }
        })

        pointsExpandableView = view.findViewById<ConstraintLayout>(R.id.pointsExpandableView)
        pointsArrowBtn = view.findViewById<Button>(R.id.pointsArrowBtn)
        pointsCardView = view.findViewById<CardView>(R.id.pointsCardView)

        pointsArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
        pointsArrowBtn.setOnClickListener(View.OnClickListener {
            if (pointsExpandableView.getVisibility() == View.GONE) {
//                TransitionManager.beginDelayedTransition(pointsCardView, AutoTransition())
                pointsExpandableView.setVisibility(View.VISIBLE)
                pointsArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)
            } else {
//                TransitionManager.beginDelayedTransition(pointsCardView, AutoTransition())
                pointsExpandableView.setVisibility(View.GONE)
                pointsArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
            }
        })

        foodExpandableView = view.findViewById<ConstraintLayout>(R.id.foodExpandableView)
        foodArrowBtn = view.findViewById<Button>(R.id.foodArrowBtn)
        foodCardView = view.findViewById<CardView>(R.id.foodCardView)

        foodArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
        foodArrowBtn.setOnClickListener(View.OnClickListener {
            if (foodExpandableView.getVisibility() == View.GONE) {
//                TransitionManager.beginDelayedTransition(pointsCardView, AutoTransition())
                foodExpandableView.setVisibility(View.VISIBLE)
                foodArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)
            } else {
//                TransitionManager.beginDelayedTransition(pointsCardView, AutoTransition())
                foodExpandableView.setVisibility(View.GONE)
                foodArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
            }
        })

        return view
    }


}
