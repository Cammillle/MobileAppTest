package com.example.mobileapptest.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapptest.api.CoinGeckoApi
import com.example.mobileapptest.CoinList.CoinList
import com.example.mobileapptest.CoinList.CoinListViewModel
import com.example.mobileapptest.CoinList.CoinListViewModelProviderFactory
import com.example.mobileapptest.CoinsAdapter
import com.example.mobileapptest.api.RetrofitInstance
import com.example.mobileapptest.databinding.ActivityMainBinding
import com.example.mobileapptest.repository.CoinRepository
import com.example.mobileapptest.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var coinListViewModel: CoinListViewModel
    private lateinit var coinAdapter: CoinsAdapter

    var isError = false
    var isLoading = false
    var isScrolling = false
    var isLastPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()

        val repository = CoinRepository()
        val viewModelProviderFactory = CoinListViewModelProviderFactory(application,repository)
        coinListViewModel = ViewModelProvider(this,viewModelProviderFactory)
            .get(CoinListViewModel::class.java)


        coinListViewModel.coinsResponse.observe(this, {responce ->
            when(responce){
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    responce.data?.let {
                        coinAdapter.submitList(it)
                    }
                    binding.chipUSD.setOnClickListener{
                        coinAdapter.submitList(responce.data)
                    }

                }
                is Resource.Error<*> -> {
                    hideProgressBar()
                    responce.message?.let {message->
                        Toast.makeText(this, "Sorry error: $message", Toast.LENGTH_SHORT).show()
                        showErrorMessage()
                    }
                }
                is Resource.Loading<*> ->{
                    showProgressBar()
                }
            }
        })

//            CoroutineScope(Dispatchers.IO).launch {
//                val listOfCoinsUSD = coinGeckoApi.getAllUsdCoins()
//                val listOfCoinsRUB = coinGeckoApi.getAllRubCoins()
//
//                runOnUiThread {
//                    adapter.submitList(listOfCoinsUSD)
//                    binding.chipRUB.setOnCheckedChangeListener{chip, isChecked ->
//                        adapter.submitList(listOfCoinsRUB)
//                    }
//                    binding.chipUSD.setOnClickListener{
//                        adapter.submitList(listOfCoinsUSD)
//                    }
//                }
//            }

        coinAdapter.setOnClickListener(object: CoinsAdapter.OnClickListener {
            override fun onClick(position: Int, model: CoinList) {
                val intent = Intent(this@MainActivity, CoinActivity::class.java)
                intent.putExtra("id",model.id)
                startActivity(intent)
            }

        })
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun hideErrorMessage(){
        binding.tvMistake.visibility = View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(){
        binding.tvMistake.visibility = View.VISIBLE
        isError = true
    }

    val scrollListener = object: RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLastPage && !isLoading
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                coinListViewModel.getCoinsByUsd()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

    }

    private fun initAdapter(){
        coinAdapter = CoinsAdapter()
        binding.rcView.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }




}