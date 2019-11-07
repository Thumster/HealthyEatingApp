package com.example.healthyeatingapp


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.healthyeatingapp.Food.DataRecord_Food

/**
 * A simple [Fragment] subclass.
 */
class Fragment_QRCodeConfirmation : Fragment() {
    private var listener: Fragment_QRCodeConfirmation.OnFragmentInteractionListener? = null
    private lateinit var food: DataRecord_Food

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment__qrcode_confirmation, container, false)
        // Inflate the layout for this fragment
        val confirmButton = view.findViewById<Button>(R.id.qrcodeconfirmation_button_confirm)
        confirmButton.setOnClickListener { view ->
            listener?.onFragmentInteractionQRCodeConfirmation(true)
        }
        val name = view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_name)
        val price = view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_price)
        val calories = view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_calories)
        val fats = view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_fats)
        val protein = view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_protein)
        val carbohydrate =
            view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_carbohydrate)
        val sodium = view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_sodium)
        val sugar = view.findViewById<TextView>(R.id.qrcodeconfirmation_textview_sugar)

        name.text = food.name
        price.text = "S$  " + food.price.toString()
        calories.text = food.calories.toString()
        fats.text = food.fats.toString()
        protein.text = food.protein.toString()
        carbohydrate.text = food.carbohydrate.toString()
        sodium.text = food.sodium.toString()
        sugar.text = food.sugar.toString()

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(food: DataRecord_Food) = Fragment_QRCodeConfirmation().apply {
            arguments = Bundle().apply {
                putSerializable("food", food)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Fragment_QRCodeConfirmation.OnFragmentInteractionListener) {
            listener = context
            arguments?.getSerializable("food").let {
                food = (it as? DataRecord_Food)!!
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
        fun onFragmentInteractionQRCodeConfirmation(result: Boolean)
    }
}
