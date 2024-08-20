package com.example.mobileapptest.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapptest.api.CoinGeckoApi
import com.example.mobileapptest.databinding.ActivityCoinBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bBack.setOnClickListener{
            onBackPressed()
        }

        initRetrofitBuilder()
        val retrofit = initRetrofitBuilder()
        val coinGeckoApi = retrofit.create(CoinGeckoApi::class.java)

        if(intent.hasExtra("id")){
            CoroutineScope(Dispatchers.IO).launch {
                val coin = coinGeckoApi.getCoinById(intent.getStringExtra("id").toString())
                runOnUiThread {
                    binding.tvNameOfCoin.text = coin.name
                    binding.tvDesc.text = coin.description.en
                    val categories = coin.categories.joinToString(separator = ",")
                    binding.tvCategories.text = categories
                    Picasso.get().load(coin.image.large).into(binding.imageOfCoin)
                }
            }

        }

    }

    private fun initRetrofitBuilder(): Retrofit {
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