package com.example.healthyeatingapp.QRCode

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.example.healthyeatingapp.Food.DataRecord_Food
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class JSONParser(
    private var c: Context,
    private var jsonData: String
) : AsyncTask<Void, Void, DataRecord_Food>() {
    //    private lateinit var food: Food
    private lateinit var food: DataRecord_Food


    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Void?): DataRecord_Food? {
        return parse()
    }

    override fun onPostExecute(result: DataRecord_Food?) {
        super.onPostExecute(result)
        if (result != null) {
//            myTextView.text = "parse successful -- " +
//                    "\nfoodName: " + food.getName() +
//                    "\nfoodPrice: " + food.getPrice() +
//                    "\nfoodCalories " + food.getCalories() +
//                    "\nfoodFats " + food.getFats() +
//                    "\nfoodProtein " + food.getProtein() +
//                    "\nfoodCarbohydrate " + food.getCarbohydrate() +
//                    "\nfoodSodium " + food.getSodium() +
//                    "\nfoodSugar " + food.getSugar()
        } else {
            Toast.makeText(
                c,
                "Unable to parse! This is the data we were trying to parse : " + jsonData,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun parse(): DataRecord_Food? {
        try {
            val jo = JSONObject(jsonData)

            val name = jo.getString("food_name")
            val price = jo.getDouble("food_price")
            val calories = jo.getInt("food_calories")
            val fats = jo.getInt("food_fats")
            val protein = jo.getInt("food_protein")
            val carbohydrate = jo.getInt("food_carbohydrate")
            val sodium = jo.getInt("food_sodium")
            val sugar = jo.getInt("food_sugar")

            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date_and_time = sdf.format(Date())

            food = DataRecord_Food(
                name,
                price,
                calories,
                fats,
                protein,
                carbohydrate,
                sodium,
                sugar,
                date_and_time
            )
            return food
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

}