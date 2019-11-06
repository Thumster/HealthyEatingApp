package com.example.healthyeatingapp


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment_Profile : Fragment() {
    private var listener: Fragment_Profile.OnFragmentInteractionListener? = null

    private lateinit var deleteButton: Button
    private lateinit var editButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__profile, container, false)
        editButton = view.findViewById<FloatingActionButton>(R.id.profile_floatingbutton_edit)
        editButton.setOnClickListener {
            MaterialAlertDialogBuilder(
                activity,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setMessage("Edit Profile")
                .setPositiveButton("Confirm") { dialog, which ->

                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .show()
        }
        deleteButton = view.findViewById<Button>(R.id.profile_button_delete)
        deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(
                activity,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setMessage("Are you sure? Your actions can't be reverted.")
                .setPositiveButton("Confirm") { dialog, which ->
                    listener?.onFragmentInteractionProfileDeleteDatabase(
                        true
                    )
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteractionProfileDeleteDatabase(boo: Boolean)
    }
}
