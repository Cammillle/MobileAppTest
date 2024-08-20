package com.example.mobileapptest.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapptest.Coin.CoinViewModel
import com.example.mobileapptest.Coin.CoinViewModelProviderFactory
import com.example.mobileapptest.CoinList.CoinListUsdViewModel
import com.example.mobileapptest.CoinList.CoinListViewModelProviderFactoryUsd
import com.example.mobileapptest.api.CoinGeckoApi
import com.example.mobileapptest.databinding.ActivityCoinBinding
import com.example.mobileapptest.repository.CoinRepository
import com.example.mobileapptest.util.Resource
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
     lateinit var viewModel: CoinViewModel

    var isError = false
    var isLoading = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bBack.setOnClickListener{
            onBackPressed()
        }

        val repository = CoinRepository()
        val viewModelProviderFactory = CoinViewModelProviderFactory(application,repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)
            .get(CoinViewModel::class.java)

        intent?.let {
            val id = intent.getStringExtra("id")?:""
            if(id.isNotBlank()){
                viewModel.getCoinById(id)
                viewModel.coinsResponse.observe(this) { response ->
                    when (response) {
                        is Resource.Success<*> -> {
                            hideProgressBar()
                            hideErrorMessage()
                            response.data?.let {
                                binding.tvNameOfCoin.text = response.data.name
                                binding.tvDesc.text = response.data.description.en
                                val categories = response.data.categories.joinToString(separator = ",")
                                binding.tvCategories.text = categories
                                Picasso.get().load(response.data.image.large).into(binding.imageOfCoin)
                            }
                        }
                        is Resource.Error<*> -> {
                            hideProgressBar()
                            showErrorMessage()
                            response.message?.let { message ->
                                Toast.makeText(this, "Sorry error: $message", Toast.LENGTH_SHORT).show()
                                showErrorMessage()
                            }
                        }
                        is Resource.Loading<*> -> {
                            showProgressBar()
                        }
                    }
                }
            }

        }

//        initRetrofitBuilder()
//        val retrofit = initRetrofitBuilder()
//        val coinGeckoApi = retrofit.create(CoinGeckoApi::class.java)



//        if(intent.hasExtra("id")){
//            CoroutineScope(Dispatchers.IO).launch {
//                val coin = coinGeckoApi.getCoinById(intent.getStringExtra("id").toString())
//                runOnUiThread {
//                    binding.tvNameOfCoin.text = coin.name
//                    binding.tvDesc.text = coin.description.en
//                    val categories = coin.categories.joinToString(separator = ",")
//                    binding.tvCategories.text = categories
//                    Picasso.get().load(coin.image.large).into(binding.imageOfCoin)
//                }
//            }
//
//        }

    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = true
    }
    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun hideErrorMessage(){
        binding.tvMistake.visibility = View.INVISIBLE
        binding.imgCoin.visibility = View.INVISIBLE
        binding.bTryAgain.visibility = View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(){
        binding.tvMistake.visibility = View.VISIBLE
        binding.imgCoin.visibility = View.VISIBLE
        binding.bTryAgain.visibility = View.VISIBLE
        isError = true
    }


}