package com.example.mobileapptest

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapptest.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter:CoinsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = initRetrofitBuilder()
        val coinGeckoApi = retrofit.create(CoinGeckoApi::class.java)
        initAdapter()



            CoroutineScope(Dispatchers.IO).launch {
                //val coin = coinGeckoApi.getCoinById("bitcoin")
                val listOfCoinsUSD = coinGeckoApi.getAllUsdCoins()
                val listOfCoinsRUB = coinGeckoApi.getAllRubCoins()
                runOnUiThread {
                    adapter.submitList(listOfCoinsUSD)

                    binding.chipRub.setOnCheckedChangeListener{chip, isChecked ->
                        chip.setTextColor(Color.YELLOW)
                        chip.setBackgroundColor(Color.BLACK)
                        adapter.submitList(listOfCoinsRUB)
                    }



                    binding.chipUsd.setOnClickListener{
                        adapter.submitList(listOfCoinsUSD)
                    }




                }

            }
    }

    private fun initAdapter(){
        binding.rcView.layoutManager =LinearLayoutManager(this)
        adapter = CoinsAdapter()
        binding.rcView.adapter = adapter
    }

    private fun initRetrofitBuilder():Retrofit{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder().baseUrl("https://api.coingecko.com")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }


}