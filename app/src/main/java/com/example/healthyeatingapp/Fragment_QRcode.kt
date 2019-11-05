package com.example.healthyeatingapp


import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class Fragment_QRcode : Fragment() {
    var allPermissionsGrantedFlag: Int = 1
    var jsonURL = ""

    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__qrcode, container, false)
        // Inflate the layout for this fragment

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (allPermissionsEnabled()) {
                allPermissionsGrantedFlag = 1
            } else {
                setupMultiplePermissions()
            }
        } else {
            allPermissionsGrantedFlag = 1
        }

        val cameraView = view.findViewById<SurfaceView>(R.id.qrcode_surfaceView)
        val tvCodeInfo = view.findViewById<TextView>(R.id.qrcode_textView)

        val barcodeDetector = BarcodeDetector.Builder(activity)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        val cameraSource = CameraSource.Builder(activity, barcodeDetector)
            .setRequestedPreviewSize(640, 480)
            .build()

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                try {
                    cameraSource.start(cameraView.holder)
                } catch (ie: IOException) {
                    Log.e("CAMERA SOURCE ", ie.message)
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int, width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes = detections?.detectedItems

                if (barcodes?.size() != 0) {
                    if (jsonURL != barcodes?.valueAt(0)?.displayValue) {
                        vibratePhone()
                        jsonURL = barcodes?.valueAt(0)?.displayValue!!
                        JSONParser(activity!!, jsonURL, tvCodeInfo).execute()
//                        InternetJSON(this@MainActivity, jsonURL, tvCodeInfo).execute()
                        barcodeDetector.release()
                    }

                }
            }
        })
        return view
    }

    fun vibratePhone() {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun allPermissionsEnabled(): Boolean {
        return permissionList.none {
            activity?.checkSelfPermission(it) !=
                    PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupMultiplePermissions() {
        val remainingPermissions = permissionList.filter {
            activity?.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
        requestPermissions(remainingPermissions.toTypedArray(), 101)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissionList: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissionList, grantResults)
        if (requestCode == 101) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                @TargetApi(Build.VERSION_CODES.M)
                if (permissionList.any { shouldShowRequestPermissionRationale(it) }) {
                    AlertDialog.Builder(activity)
                        .setMessage("Press Permissions to Decide Permission Again")
                        .setPositiveButton("Permissions") { dialog, which -> setupMultiplePermissions() }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }
        }
    }
}
