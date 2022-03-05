package com.raflisalam.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.hide()

        val searchView = findViewById<SearchView>(R.id.searching)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val string = query.toString()
                getWeather(string)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun getWeather(query: String) {
        ApiClient.instance.getWeather(query).enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                val tvTemp = findViewById<TextView>(R.id.tvTemp)
                val tvCondition = findViewById<TextView>(R.id.tvCondition)

                tvCondition.text = response.body()?.current?.condition?.text.toString()
                tvTemp.text = response.body()?.current?.tempC.toString()+ "°"
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                t.message?.let { Log.d("Fail Load!", it) }
            }

        })
    }
}