package com.example.bigsy_previsodotempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {
    private var progressbarcirc = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.titlebar);



        val button = findViewById<Button>(R.id.button)
        val input = findViewById<EditText>(R.id.editTextText)
        val progressValue = findViewById<TextView>(R.id.progress_value)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        val max = 100
        progressBar.max = max


       button.setOnClickListener(){
            progressValue.text = input.text.toString()
        }



        val value = progressValue.text.toString().toInt()
        progressBar.animate().setDuration(1000).setUpdateListener {
            progressBar.progress = progressbarcirc
            progressValue.text = progressbarcirc.toString()
        }

    }
}

