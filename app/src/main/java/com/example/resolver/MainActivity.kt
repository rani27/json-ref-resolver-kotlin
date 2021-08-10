package com.example.resolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Test","Input File: ${getJsonDataFromAsset()}")
        Log.d("Test","Output: ${JsonGenerator().convertToJson(getJsonDataFromAsset())}")

    }
    private fun getJsonDataFromAsset(): String {
        val jsonString: String
        try {
            jsonString =
                baseContext.assets.open("jsonFile.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }
        return jsonString
    }
}