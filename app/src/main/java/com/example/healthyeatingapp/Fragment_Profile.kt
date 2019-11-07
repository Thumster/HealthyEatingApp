package com.example.healthyeatingapp


import android.content.Context
import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.Profile.DBHelper_Profile
import com.example.healthyeatingapp.Profile.DataRecord_Profile
import com.example.healthyeatingapp.Wallet.DBHelper_Transaction
import com.example.healthyeatingapp.enumeration.Gender
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment_Profile : Fragment() {
    private var listener: Fragment_Profile.OnFragmentInteractionListener? = null
    private lateinit var dbHelper_profile: DBHelper_Profile
    private lateinit var deleteButton: Button
    private lateinit var editButton: FloatingActionButton
    private lateinit var mainDisplay: LinearLayout
    private lateinit var nodataDisplay: LinearLayout
    private lateinit var nameText: TextView
    private lateinit var genderText: TextView
    private lateinit var ageText: TextView
    private lateinit var heightText: TextView
    private lateinit var weightText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__profile, container, false)

        dbHelper_profile = DBHelper_Profile(activity!!)

        editButton = view.findViewById<FloatingActionButton>(R.id.profile_floatingbutton_edit)
        editButton.setOnClickListener {
            val form = LayoutInflater.from(activity)
                .inflate(
                    R.layout.profile_form_profileedit,
                    container,
                    false
                ) as LinearLayout
            loadForm(form)
            MaterialAlertDialogBuilder(
                activity,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setView(form)
                .setPositiveButton("Confirm") { dialog, which ->
                    checkForm(form)
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
                    listener?.onFragmentInteractionProfileDeleteDatabase(true)
                    loadProfile()
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
        }
        mainDisplay = view.findViewById<LinearLayout>(R.id.profile_maindisplay)
        nodataDisplay = view.findViewById<LinearLayout>(R.id.profile_nodatadisplay)

        nameText = view.findViewById<TextView>(R.id.profile_textview_name)
        genderText = view.findViewById<TextView>(R.id.profile_textview_gender)
        ageText = view.findViewById<TextView>(R.id.profile_textview_age)
        heightText = view.findViewById<TextView>(R.id.profile_textview_height)
        weightText = view.findViewById<TextView>(R.id.profile_textview_weight)

        loadProfile()
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

    fun loadProfile() {
        dbHelper_profile.getProfile()
        val profile = DBHelper_Profile.user
        if (!profile.name.equals("")) {
            nodataDisplay.visibility = View.GONE
            mainDisplay.visibility = View.VISIBLE
            nameText.text = profile.name
            ageText.text = profile.age.toString()
            genderText.text = profile.gender.toString()
            heightText.text = String.format("%.0f", profile.height) + "  CM"
            weightText.text = String.format("%.1f", profile.weight) + "  KG"
        } else {
            nodataDisplay.visibility = View.VISIBLE
            mainDisplay.visibility = View.GONE
        }
    }

    fun loadForm(form: LinearLayout) {
        dbHelper_profile.getProfile()
        val profile = DBHelper_Profile.user
        if (!profile.name.equals("")) {
            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_name).text =
                SpannableStringBuilder(profile.name)
            if (profile.gender == Gender.MALE) {
                val genderRadioMale =
                    form.findViewById<RadioButton>(R.id.profile_editprofile_gender_male)
                genderRadioMale.isChecked = true;
            } else {
                val genderRadioFemale =
                    form.findViewById<RadioButton>(R.id.profile_editprofile_gender_female)
                genderRadioFemale.isChecked = true;
            }

            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_age).text =
                SpannableStringBuilder(profile.age.toString())
            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_height).text =
                SpannableStringBuilder(String.format("%.0f", profile.height))
            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_weight).text =
                SpannableStringBuilder(String.format("%.1f", profile.weight))
        }
    }

    fun checkForm(form: LinearLayout) {
        val inputName: String =
            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_name).text.toString()
        val genderRadioMale = form.findViewById<RadioButton>(R.id.profile_editprofile_gender_male)
        var genderSelected = if (genderRadioMale.isChecked) Gender.MALE else Gender.FEMALE
        val age =
            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_age).text.toString()
                .toIntOrNull()
        val height =
            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_height).text.toString()
                .toDoubleOrNull()
        val heightValue = if (height != null) String.format("%.0f", height).toDouble() else null
        var weight =
            form.findViewById<EditText>(R.id.profile_editprofile_inputfield_weight).text.toString()
                .toDoubleOrNull()
        val weightValue = if (weight != null) String.format("%.1f", weight).toDouble() else null
        var strResult = ""
        if (inputName.equals("") || age == null || age == 0 || height == null || height == 0.0 || weight == null || weight == 0.0) {
            strResult = "INVALID INPUT! Please fill in your"
            if (inputName.equals("")) {
                strResult += "\n-Name"
            }
            if (age == null || age == 0) {
                strResult += "\n-Age"
            }
            if (height == null || height == 0.0) {
                strResult += "\n-Height"
            }
            if (weight == null || weight == 0.0) {
                strResult += "\n-Weight"
            }
        } else {
            dbHelper_profile.createProfile(
                DataRecord_Profile(
                    inputName,
                    age,
                    genderSelected,
                    weightValue!!,
                    heightValue!!
                )
            )
            strResult = "SUCCESSFULLY updated profile!"
            loadProfile()
        }
        val toast = Toast.makeText(
            activity!!,
            strResult,
            Toast.LENGTH_LONG
        )
        val view = toast.view.findViewById<TextView>(android.R.id.message)
        view?.let {
            view.gravity = Gravity.CENTER
        }
        toast.show()
    }
}
