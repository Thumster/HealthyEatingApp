package com.example.healthyeatingapp

import android.content.Context
import android.os.AsyncTask
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONParser(
    private var c: Context,
    private var jsonData: String,
    private var myTextView: TextView
) : AsyncTask<Void, Void, Boolean>() {
    private lateinit var food : Food

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Void?): Boolean {
        return parse()
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (result!!) {
            myTextView.text = "parse successful -- " +
                    "\nfoodName: " + food.getName() +
                    "\nfoodPrice: " + food.getPrice() +
                    "\nfoodCalories " + food.getCalories() +
                    "\nfoodFats " + food.getFats() +
                    "\nfoodProtein " + food.getProtein() +
                    "\nfoodCarbohydrate " + food.getCarbohydrate() +
                    "\nfoodSodium " + food.getSodium() +
                    "\nfoodSugar " + food.getSugar()
        } else {
            Toast.makeText(c, "unable to parse", Toast.LENGTH_LONG).show()
            Toast.makeText(
                c, "this is the data we were trying to parse : " +
                        jsonData, Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun parse(): Boolean {
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

            food = Food(name, price, calories, fats, protein, carbohydrate, sodium, sugar)
            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
    }

    class Food(
        private var m_name: String,
        private var m_price: Double,
        private var m_calories: Int,
        private var m_fats: Int,
        private var m_protein: Int,
        private var m_carbohydrate: Int,
        private var m_sodium: Int,
        private var m_sugar: Int
    ) {

        fun getName(): String {
            return m_name
        }

        fun getPrice(): Double {
            return m_price
        }

        fun getCalories(): Int {
            return m_calories
        }

        fun getFats(): Int {
            return m_fats
        }

        fun getProtein(): Int {
            return m_protein
        }

        fun getCarbohydrate(): Int {
            return m_carbohydrate
        }

        fun getSodium(): Int {
            return m_sodium
        }

        fun getSugar(): Int {
            return m_sugar
        }
    }

}