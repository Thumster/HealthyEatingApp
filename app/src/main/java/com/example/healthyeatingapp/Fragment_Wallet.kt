package com.example.healthyeatingapp


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.Wallet.DBHelper_Transaction
import com.example.healthyeatingapp.Wallet.DataRecord_Transaction
import com.example.healthyeatingapp.Wallet.TransactionAdapter
import com.example.healthyeatingapp.enumeration.TransactionType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class Fragment_Wallet : Fragment() {

    private lateinit var dbHelper_transaction: DBHelper_Transaction
    private lateinit var myDataset: ArrayList<DataRecord_Transaction>

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var balanceView: TextView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment__wallet, container, false)

        dbHelper_transaction = DBHelper_Transaction(activity!!)
        myDataset = dbHelper_transaction.readAllTransactions()

        emptyView = view.findViewById<TextView>(R.id.wallet_textview_notransactions)

        viewManager = LinearLayoutManager(activity)

        viewAdapter = TransactionAdapter(myDataset)

        recyclerView =
            view.findViewById<RecyclerView>(R.id.wallet_recyclerview_transactions).apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }

        if (myDataset.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        balanceView = view.findViewById(R.id.wallet_textview_balance)
        balanceView.text = "S$ " + String.format("%.2f", DBHelper_Transaction.balance)

        val addButton = view.findViewById<Button>(R.id.wallet_button_topup)
        addButton.setOnClickListener {
            val editText = LayoutInflater.from(activity)
                .inflate(
                    R.layout.transaction_inputfield_amount,
                    container,
                    false
                ) as LinearLayout
            MaterialAlertDialogBuilder(
                activity,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setView(editText)
                .setMessage("How much would you like to TOP UP?")
                .setPositiveButton("Confirm") { dialog, which ->
                    val amountString: String =
                        editText.findViewById<EditText>(R.id.wallet_transaction_inputfield_amount)
                            .text.toString()
                    addTransaction(amountString)
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
        }

        return view
    }

    fun addTransaction(amountString: String) {
        var amount: Double? = amountString.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(
                activity,
                "Transaction not completed...\nPlease enter a valid amount. Try Again!",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date_and_time = sdf.format(Date())
        amount = String.format("%.2f", amount).toDouble()

        emptyView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        val transactionToAdd = DataRecord_Transaction(
            TransactionType.DEBIT,
            amount,
            date_and_time
        )

        dbHelper_transaction.insertTransaction(transactionToAdd)
        myDataset.add(0, transactionToAdd)
        balanceView.text = "S$ " + String.format("%.2f", DBHelper_Transaction.balance)
        viewAdapter.notifyDataSetChanged()
    }
}
