package com.tez.smartnotepad.ui.ocr

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.tez.smartnotepad.R
import com.tez.smartnotepad.util.ext.name
import java.lang.Exception


class OcrFragment : Fragment(R.layout.fragment_ocr) {

    private lateinit var txt: TextView
    private lateinit var inputImage: InputImage
    private lateinit var textRecognizer: TextRecognizer
    private var intentActivityResultLauncher: ActivityResultLauncher<Intent>? = null
    private val STORAGE_PERMISSION_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<Button>(R.id.buttonPermission)
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        intentActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback {
                    val data = it.data
                    val imageUri = data?.data
                    //convertImageToText(imageUri)
                })

        btn.setOnClickListener {

            val chooseIntent = Intent()
            chooseIntent.type = "image/*"
            chooseIntent.action = Intent.ACTION_GET_CONTENT
            intentActivityResultLauncher?.launch(chooseIntent)

        }
    }

    private fun convertImageToText(imageUri: Uri?, resultText:() -> String) {

        try {
            inputImage = InputImage.fromFilePath(requireContext(),imageUri!!)
            val result: Task<Text> = textRecognizer.process(inputImage).addOnSuccessListener {
                Log.e(name(),it.text)
            }.addOnFailureListener{
                Log.e(" Hata " + name(),it.message.toString())
            }
        }catch (e: Exception){
            Log.e(name(),e.toString())
        }
    }



    override fun onResume() {
        super.onResume()
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE)
    }

    private fun checkPermission(permission:String, requestCode:Int){

        if(ContextCompat.checkSelfPermission(requireContext(),permission) == PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.e(name(),"Granted")
            }else{
                Log.e(name(),"Not Granted")
            }
        }
    }

}
