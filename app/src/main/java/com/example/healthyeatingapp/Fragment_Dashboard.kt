package com.example.healthyeatingapp


import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.example.healthyeatingapp.Points.DBHelper_Points
import com.example.healthyeatingapp.Food.DBHelper_Food
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.Points.DataRecord_Points
import com.example.healthyeatingapp.Points.PointsAdapter
import com.example.healthyeatingapp.Profile.DBHelper_Profile
import com.example.healthyeatingapp.Profile.DataRecord_Profile
import com.example.healthyeatingapp.Wallet.DBHelper_Transaction
import com.example.healthyeatingapp.Wallet.DataRecord_Transaction
import com.example.healthyeatingapp.Wallet.TransactionAdapter
import com.example.healthyeatingapp.enumeration.TransactionType
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class Fragment_Dashboard : Fragment() {
    private var listener: Fragment_Dashboard.OnFragmentInteractionListener? = null
    private val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    private lateinit var dbHelper_profile: DBHelper_Profile
    private lateinit var dbhelper_points: DBHelper_Points
    private lateinit var dbHelper_food: DBHelper_Food
    private lateinit var dbHelper_transaction: DBHelper_Transaction

    private var currentUser: DataRecord_Profile? = null
    private var toAddFood: DataRecord_Food? = null
    private var totalPoints = 0

    private var recommendedCalories = 0
    private var recommendedCarbohydrates = 0
    private var recommendedProtein = 0
    private var recommendedFats = 0
    //GRAMS
    private var recommendedSugar = 30
    //MG
    private var recommendedSodium = 1500

    private var allMeals: ArrayList<DataRecord_Food> = ArrayList<DataRecord_Food>()
    private var todaysMeals: ArrayList<DataRecord_Food> = ArrayList<DataRecord_Food>()
    private var todaysCalories = 0
    private var todaysCarbohydrates = 0
    private var todaysProtein = 0
    private var todaysFats = 0
    private var todaysSugar = 0
    private var todaysSodium = 0
    private var lastMeal: DataRecord_Food? = null
    private var pastMeals: ArrayList<DataRecord_Food> = ArrayList<DataRecord_Food>()
    private var allPoints: ArrayList<DataRecord_Points> = ArrayList<DataRecord_Points>()

    private val carbohydratesPercentage = 0.55
    private val proteinPercentage = 0.20
    private val fatsPercentage = 0.25
    private lateinit var goalNoShowCardView: CardView
    private lateinit var goalExpandableView: ConstraintLayout
    private lateinit var goalArrowBtn: Button
    private lateinit var goalCardView: CardView
    private lateinit var progressCalories: RoundCornerProgressBar
    private lateinit var caloriesText: TextView
    private lateinit var progressCarbohydrates: RoundCornerProgressBar
    private lateinit var carbohydratesText: TextView
    private lateinit var progressProtein: RoundCornerProgressBar
    private lateinit var proteinText: TextView
    private lateinit var progressFats: RoundCornerProgressBar
    private lateinit var fatsText: TextView
    private lateinit var progressSugar: RoundCornerProgressBar
    private lateinit var sugarText: TextView
    private lateinit var progressSodium: RoundCornerProgressBar
    private lateinit var sodiumText: TextView

    private lateinit var pointsClaimButton: Button
    private lateinit var pointsExpandableView: ConstraintLayout
    private lateinit var pointsScrollView: LinearLayout
    private lateinit var pointsNoShowView: TextView
    private lateinit var pointsArrowBtn: Button
    private lateinit var pointsCardView: CardView
    private lateinit var pointsText: TextView
    private lateinit var foodExpandableView: ConstraintLayout
    private lateinit var foodArrowBtn: Button
    private lateinit var foodCardView: CardView

    private lateinit var pointsRecyclerView: RecyclerView
    private lateinit var pointsViewAdapter: RecyclerView.Adapter<*>
    private lateinit var pointsViewManager: RecyclerView.LayoutManager

    companion object {

        @JvmStatic
        fun newInstance(food: DataRecord_Food) = Fragment_Dashboard().apply {
            arguments = Bundle().apply {
                putSerializable("food", food)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__dashboard, container, false)
        dbHelper_profile = DBHelper_Profile(activity!!)
        dbhelper_points = DBHelper_Points(activity!!)
        dbHelper_food = DBHelper_Food(activity!!)
        dbHelper_transaction = DBHelper_Transaction(activity!!)


        pointsText = view.findViewById<TextView>(R.id.pointsHeaderValue)
        pointsExpandableView = view.findViewById<ConstraintLayout>(R.id.pointsExpandableView)
        pointsScrollView = pointsExpandableView.findViewById(R.id.pointsScrollView)
        pointsNoShowView = pointsExpandableView.findViewById(R.id.pointsNoShowHeaderName)


        checkToAddFood()
        loadDb()

        pointsClaimButton = view.findViewById<Button>(R.id.pointsClaimButton)
        pointsClaimButton.setOnClickListener {
            claimPoints()
        }
        pointsArrowBtn = view.findViewById<Button>(R.id.pointsArrowBtn)
        pointsCardView = view.findViewById<CardView>(R.id.pointsCardView)

        pointsArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
        pointsArrowBtn.setOnClickListener {
            if (pointsExpandableView.getVisibility() == View.GONE) {
                pointsExpandableView.setVisibility(View.VISIBLE)
                pointsArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)
            } else {
                pointsExpandableView.setVisibility(View.GONE)
                pointsArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
            }
        }
        pointsViewManager = LinearLayoutManager(activity)
        pointsViewAdapter = PointsAdapter(allPoints)
        pointsRecyclerView =
            view.findViewById<RecyclerView>(R.id.pointsRecyclerView).apply {
                layoutManager = pointsViewManager
                adapter = pointsViewAdapter
            }
//        pointsViewAdapter.notifyDataSetChanged()

        goalNoShowCardView = view.findViewById<CardView>(R.id.goalNoShowCardView)
        goalCardView = view.findViewById<CardView>(R.id.goalCardView)
        goalExpandableView = view.findViewById<ConstraintLayout>(R.id.goalExpandableView)
        goalArrowBtn = view.findViewById<Button>(R.id.goalArrowBtn)
        goalCardView = view.findViewById<CardView>(R.id.goalCardView)

        if (currentUser?.name.equals("")) {
            goalNoShowCardView.visibility = View.VISIBLE
            goalNoShowCardView.setOnClickListener({
                listener?.onFragmentInteractionNavigateToProfile()
            })
            goalCardView.visibility = View.GONE
        } else {
            progressCalories =
                view.findViewById(R.id.caloriesProgressBar)
            progressCalories.max =
                if (recommendedCalories > todaysCalories) recommendedCalories.toFloat() else todaysCalories.toFloat()
            val lastMealCalories = lastMeal?.calories ?: 0
            progressCalories.progress = (todaysCalories - lastMealCalories).toFloat()
            progressCalories.secondaryProgress = (todaysCalories).toFloat()
            caloriesText = view.findViewById(R.id.goalCaloriesHeaderValue)
            caloriesText.text =
                todaysCalories.toString() + " / " + recommendedCalories.toString() + " KCAL"
            if (recommendedCalories <= todaysCalories) {
                caloriesText.setTextColor(Color.parseColor("Red"))
                progressCalories.progress = progressCalories.max
                progressCalories.progressColor = resources.getColor(R.color.colorAccent)
            }


            progressCarbohydrates =
                view.findViewById(R.id.carbohydratesProgressBar)
            progressCarbohydrates.max =
                if (recommendedCarbohydrates > todaysCarbohydrates) recommendedCarbohydrates.toFloat() else todaysCarbohydrates.toFloat()
            val lastMealCarbohydrates = lastMeal?.carbohydrate ?: 0
            progressCarbohydrates.progress = (todaysCarbohydrates - lastMealCarbohydrates).toFloat()
            progressCarbohydrates.secondaryProgress = todaysCarbohydrates.toFloat()
            carbohydratesText = view.findViewById(R.id.goalCarbohydratesHeaderValue)
            carbohydratesText.text =
                todaysCarbohydrates.toString() + " / " + recommendedCarbohydrates.toString() + " g"
            if (recommendedCarbohydrates <= todaysCarbohydrates) {
                carbohydratesText.setTextColor(Color.parseColor("Red"))
                progressCarbohydrates.progress = progressCarbohydrates.max
                progressCarbohydrates.progressColor = resources.getColor(R.color.colorAccent)
            }


            progressProtein =
                view.findViewById(R.id.proteinProgressBar)
            progressProtein.max =
                if (recommendedProtein > todaysProtein) recommendedProtein.toFloat() else todaysProtein.toFloat()
            val lastMealProtein = lastMeal?.protein ?: 0
            progressProtein.progress = (todaysProtein - lastMealProtein).toFloat()
            progressProtein.secondaryProgress = todaysProtein.toFloat()
            proteinText = view.findViewById(R.id.goalProteinHeaderValue)
            proteinText.text =
                todaysProtein.toString() + " / " + recommendedProtein.toString() + " g"
            if (recommendedProtein <= todaysProtein) {
                proteinText.setTextColor(Color.parseColor("Red"))
                progressProtein.progress = progressProtein.max
                progressProtein.progressColor = resources.getColor(R.color.colorAccent)
            }

            progressFats =
                view.findViewById(R.id.fatsProgressBar)
            progressFats.max =
                if (recommendedFats > todaysFats) recommendedFats.toFloat() else todaysFats.toFloat()
            val lastMealFats = lastMeal?.fats ?: 0
            progressFats.progress = (todaysFats - lastMealFats).toFloat()
            progressFats.secondaryProgress = todaysFats.toFloat()
            fatsText = view.findViewById(R.id.goalFatsHeaderValue)
            fatsText.text = todaysFats.toString() + " / " + recommendedFats.toString() + " g"
            if (recommendedFats <= todaysFats) {
                fatsText.setTextColor(Color.parseColor("Red"))
                progressFats.progress = progressFats.max
                progressFats.progressColor = resources.getColor(R.color.colorAccent)
            }

            progressSugar =
                view.findViewById(R.id.sugarProgressBar)
            progressSugar.max =
                if (recommendedSugar > todaysSugar) recommendedSugar.toFloat() else todaysSugar.toFloat()
            val lastMealSugar = lastMeal?.sugar ?: 0
            progressSugar.progress = (todaysSugar - lastMealSugar).toFloat()
            progressSugar.secondaryProgress = todaysSugar.toFloat()
            sugarText = view.findViewById(R.id.goalSugarHeaderValue)
            sugarText.text = todaysSugar.toString() + " / " + recommendedSugar.toString() + " g"
            if (recommendedSugar <= todaysSugar) {
                sugarText.setTextColor(Color.parseColor("Red"))
                progressSugar.progress = progressSugar.max
                progressSugar.progressColor = resources.getColor(R.color.colorAccent)
            }

            progressSodium =
                view.findViewById(R.id.sodiumProgressBar)
            progressSodium.max =
                if (recommendedSodium > todaysSodium) recommendedSodium.toFloat() else todaysSodium.toFloat()
            val lastMealSodium = lastMeal?.sodium ?: 0
            progressSodium.progress = (todaysSodium - lastMealSodium).toFloat()
            progressSodium.secondaryProgress = todaysSodium.toFloat()
            sodiumText = view.findViewById(R.id.goalSodiumHeaderValue)
            sodiumText.text = todaysSodium.toString() + " / " + recommendedSodium.toString() + " mg"
            if (recommendedSodium <= todaysSodium) {
                sodiumText.setTextColor(Color.parseColor("Red"))
                progressSodium.progress = progressSodium.max
                progressSodium.progressColor = resources.getColor(R.color.colorAccent)
            }


            goalArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
            goalArrowBtn.setOnClickListener(View.OnClickListener {
                if (goalExpandableView.getVisibility() == View.GONE) {
                    goalExpandableView.setVisibility(View.VISIBLE)
                    goalArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)
                } else {
                    goalExpandableView.setVisibility(View.GONE)
                    goalArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
                }
            })
        }


        foodExpandableView = view.findViewById<ConstraintLayout>(R.id.foodExpandableView)
        foodArrowBtn = view.findViewById<Button>(R.id.foodArrowBtn)
        foodCardView = view.findViewById<CardView>(R.id.foodCardView)

        foodArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
        foodArrowBtn.setOnClickListener(View.OnClickListener {
            if (foodExpandableView.getVisibility() == View.GONE) {
                foodExpandableView.setVisibility(View.VISIBLE)
                foodArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)
            } else {
                foodExpandableView.setVisibility(View.GONE)
                foodArrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
            }
        })

        return view
    }

    fun loadDb() {
        currentUser = dbHelper_profile.getProfile()
        recommendedCalories = DBHelper_Profile.BMR
        recommendedCarbohydrates = ((recommendedCalories * carbohydratesPercentage) / 4).toInt()
        recommendedProtein = ((recommendedCalories * proteinPercentage) / 4).toInt()
        recommendedFats = ((recommendedCalories * fatsPercentage) / 9).toInt()


        allMeals = dbHelper_food.readAllFoods()
        var localDateTime: LocalDateTime = LocalDateTime.now().toLocalDate().atTime(0, 0, 0)
        todaysMeals =
            ArrayList(allMeals.filter { dateTimeStrToLocalDateTime(selector(it)) >= localDateTime })
        todaysCalories = 0
        todaysCarbohydrates = 0
        todaysProtein = 0
        todaysFats = 0
        todaysSugar = 0
        todaysSodium = 0
        todaysMeals.forEach {
            todaysCalories += it.calories
            todaysCarbohydrates += it.carbohydrate
            todaysProtein += it.protein
            todaysFats += it.fats
            todaysSugar += it.sugar
            todaysSodium += it.sodium
        }
        if (!todaysMeals.isEmpty()) {
            lastMeal = todaysMeals.first()
        }
        pastMeals =
            ArrayList(allMeals.filter { dateTimeStrToLocalDateTime(selector(it)) < localDateTime })
//        todaysMeals.forEach { Log.e("TODAYYYYY", it.date_and_time) }
//        pastMeals.forEach { Log.e("PASTTTTT", it.date_and_time) }
        allPoints = dbhelper_points.readAllPoints()
        if (allPoints.isEmpty()) {
            pointsScrollView.visibility = View.GONE
            pointsNoShowView.visibility = View.VISIBLE
        }

        totalPoints = DBHelper_Points.totalPoints
        pointsText.text = totalPoints.toString()
    }

    fun checkToAddFood() {
        if (toAddFood != null && !currentUser?.name.equals("")) {
            var pointsToAdd = 0
            val currentFood = toAddFood!!
            if ((currentFood.carbohydrate > 0) && todaysCarbohydrates <= recommendedCarbohydrates) {
                pointsToAdd++
            }
            if ((currentFood.protein > 0) && todaysProtein <= recommendedProtein) {
                pointsToAdd++
            }
            if ((currentFood.fats > 0) && todaysFats <= recommendedFats) {
                pointsToAdd++
            }
            if ((currentFood.sugar > 0) && todaysSugar <= recommendedSugar) {
                pointsToAdd++
            }
            if ((currentFood.sodium > 0) && todaysSodium <= recommendedSodium) {
                pointsToAdd++
            }
            if (pointsToAdd > 0) {
                totalPoints += pointsToAdd
                pointsText.text = totalPoints.toString()
                val newPoint = DataRecord_Points(currentFood.name, pointsToAdd, sdf.format(Date()))
                dbhelper_points.insertPoints(newPoint)
                allPoints.add(0, newPoint)

                pointsScrollView.visibility = View.VISIBLE
                pointsNoShowView.visibility = View.GONE
            }
        }
    }

    fun claimPoints() {
        if (totalPoints > 0) {
            dbHelper_transaction.insertTransaction(
                DataRecord_Transaction(
                    TransactionType.DEBIT,
                    totalPoints.toString() + " Points Claimed",
                    totalPoints * 0.1,
                    sdf.format(Date())
                )
            )
            val newPoint = DataRecord_Points(
                "Points Claimed",
                -totalPoints,
                sdf.format(Date())
            )
            dbhelper_points.insertPoints(newPoint)
            allPoints.add(0, newPoint)
            totalPoints = 0
            pointsScrollView.visibility = View.VISIBLE
            pointsNoShowView.visibility = View.GONE
            pointsText.text = totalPoints.toString()
            pointsViewAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(activity, "No points to claim!", Toast.LENGTH_SHORT).show()
        }
    }

    fun selector(p: DataRecord_Food): String = p.date_and_time
    fun dateTimeStrToLocalDateTime(input: String): LocalDateTime =
        LocalDateTime.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Fragment_Dashboard.OnFragmentInteractionListener) {
            listener = context
            arguments?.getSerializable("food").let {
                toAddFood = (it as? DataRecord_Food)
            }

        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteractionNavigateToProfile()
    }
}
