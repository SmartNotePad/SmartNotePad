package com.tez.smartnotepad.util.ocr

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.lang.Exception

object OcrUtils {

    private var textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun convertImageToText(image: InputImage?, resultText:(text: String) -> Unit ) {
        try {
            val result: Task<Text> = textRecognizer.process(image!!).addOnSuccessListener {
                resultText.invoke(it.text)
            }.addOnFailureListener{
                Log.e(" Hata OCR",it.message.toString())
            }
        }catch (e: Exception){
            Log.e("OCR H",e.toString())
        }
    }
}