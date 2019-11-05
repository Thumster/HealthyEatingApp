package com.example.healthyeatingapp


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.Wallet.DBHelper_Transaction
import com.example.healthyeatingapp.Wallet.DataRecord_Transaction
import com.example.healthyeatingapp.Wallet.TransactionAdapter
import com.example.healthyeatingapp.enumeration.TransactionType
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Fragment_Wallet : Fragment() {
//    private var listener: Fragment_Wallet.OnFragmentInteractionListener? = null

    private lateinit var dbHelper_transaction: DBHelper_Transaction
    private lateinit var myDataset: ArrayList<DataRecord_Transaction>

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment__wallet, container, false)

        dbHelper_transaction = DBHelper_Transaction(activity!!)
        myDataset = dbHelper_transaction.readAllTransactions()

        viewManager = LinearLayoutManager(activity)

        viewAdapter = TransactionAdapter(myDataset)

        recyclerView =
            view.findViewById<RecyclerView>(R.id.wallet_recyclerview_transactions).apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }

        val addButton = view.findViewById<Button>(R.id.wallet_button_topup)
        addButton.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date_and_time = sdf.format(Date())
//            listener?.onFragmentInteractionWallet(
//            )
            dbHelper_transaction.insertTransaction(
                DataRecord_Transaction(
                    TransactionType.DEBIT,
                    20.00,
                    date_and_time
                )
            )
        }

        return view
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is Fragment_Wallet.OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }
//
//    interface OnFragmentInteractionListener {
//        fun onFragmentInteractionWallet(result: DataRecord_Transaction?)
//    }

}
