package com.example.healthyeatingapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.QRCode.JSONParser
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException


class Fragment_QRcode : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    var jsonText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
		val view: View = inflater.inflate(R.layout.fragment__qrcode, container, false)
        val cameraView = view.findViewById<SurfaceView>(R.id.qrcode_surfaceView)

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
                    if (jsonText != barcodes?.valueAt(0)?.displayValue) {
                        vibratePhone()
                        jsonText = barcodes?.valueAt(0)?.displayValue!!

                        listener?.onFragmentInteractionQRCode(
                            JSONParser(
                                activity!!,
                                jsonText
                            ).execute().get()
                        )
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
        fun onFragmentInteractionQRCode(result: DataRecord_Food?)
    }
}
