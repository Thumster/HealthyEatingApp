package com.example.healthyeatingapp.QRCode

import android.content.Context
import android.os.AsyncTask
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.R
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class JSONParser(
    private var c: Context,
    private var jsonData: String
) : AsyncTask<Void, Void, DataRecord_Food>() {
    private val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    private lateinit var food: DataRecord_Food


    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Void?): DataRecord_Food? {
        return parse()
    }

    override fun onPostExecute(result: DataRecord_Food?) {
        super.onPostExecute(result)
        if (result == null) {
            val toast = Toast.makeText(
                c,
                "Invalid QRCode!\nPlease make sure it is a valid vendor." + jsonData,
                Toast.LENGTH_LONG
            )
            val view = toast.view.findViewById<TextView>(android.R.id.message)
            view?.let {
                view.gravity = Gravity.CENTER
            }
            toast.show()
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