package com.example.mobileapptest.CoinList

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobileapptest.Coin.Coin
import com.example.mobileapptest.repository.CoinRepository
import com.example.mobileapptest.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class CoinListViewModel ( app: Application, val repository: CoinRepository) : AndroidViewModel(app){

    val coinsResponse:MutableLiveData<Resource<List<CoinList>>> = MutableLiveData()
    var coinsPage = 1
    var coins: List<CoinList>? = null

    init {
        getCoinsByUsd()
    }

    fun getCoinsByUsd() = viewModelScope.launch {
        internetConnectionForApi()
    }

    fun getCoinsByRub() = viewModelScope.launch {
        internetConnectionForApi()
    }

    private fun handleResponse(response: Response<List<CoinList>>):Resource<List<CoinList>>{
        if(response.isSuccessful){
            response.body()?.let { resultRespones ->
                coinsPage++
                if(coins == null){
                    coins = resultRespones
                }
                return Resource.Success(coins ?: resultRespones)
            }
        }
        return Resource.Error(response.message())
    }

    fun internetConnection(context:Context) :Boolean{
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun internetConnectionForApi(){
        coinsResponse.postValue(Resource.Loading())
        try{
            if(internetConnection(this.getApplication())){
                val response = repository.getAllUsdCoins()
                coinsResponse.postValue(handleResponse(response))
            }else{
                coinsResponse.postValue(Resource.Error("No internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> coinsResponse.postValue(Resource.Error("IOException"))
                else -> coinsResponse.postValue(Resource.Error("Error"))

            }
        }
    }


}