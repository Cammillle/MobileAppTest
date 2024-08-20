package com.example.mobileapptest.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapptest.CoinList.CoinList
import com.example.mobileapptest.CoinList.CoinListRubViewModel
import com.example.mobileapptest.CoinList.CoinListUsdViewModel
import com.example.mobileapptest.CoinList.CoinListViewModelProviderFactoryRub
import com.example.mobileapptest.CoinList.CoinListViewModelProviderFactoryUsd
import com.example.mobileapptest.CoinList.CoinsAdapter
import com.example.mobileapptest.databinding.ActivityMainBinding
import com.example.mobileapptest.repository.CoinRepository
import com.example.mobileapptest.util.Resource


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var coinListViewModelUsd: CoinListUsdViewModel
     lateinit var coinListViewModelRub: CoinListRubViewModel

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
        val viewModelProviderFactoryUsd = CoinListViewModelProviderFactoryUsd(application,repository)
        coinListViewModelUsd = ViewModelProvider(this,viewModelProviderFactoryUsd)
            .get(CoinListUsdViewModel::class.java)

        val viewModelProviderFactoryRub = CoinListViewModelProviderFactoryRub(application,repository)
        coinListViewModelRub = ViewModelProvider(this,viewModelProviderFactoryRub).get(CoinListRubViewModel::class.java)


        binding.chipUSD.setOnClickListener {
            coinListViewModelUsd.coinsResponse.observe(this) { response ->
                when (response) {
                    is Resource.Success<*> -> {
                        hideProgressBar()
                        hideErrorMessage()
                        response.data?.let {
                            coinAdapter.submitList(response.data)
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

        binding.chipRUB.setOnClickListener {
            coinListViewModelRub.coinsResponse.observe(this) { response ->
                when (response) {
                    is Resource.Success<*> -> {
                        hideProgressBar()
                        hideErrorMessage()
                        response.data?.let {
                            coinAdapter.submitList(response.data)
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
                coinListViewModelUsd.getCoinsByUsd()
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
        binding.rcView.adapter = coinAdapter
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.addOnScrollListener(this@MainActivity.scrollListener)
    }




}