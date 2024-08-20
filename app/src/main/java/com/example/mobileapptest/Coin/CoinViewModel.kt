package com.example.mobileapptest.Coin

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobileapptest.repository.CoinRepository
import com.example.mobileapptest.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class CoinViewModel (app: Application, val repository: CoinRepository) : AndroidViewModel(app){

    val coinsResponse:MutableLiveData<Resource<Coin>> = MutableLiveData()

//    init {
//        getCoinById(id = id)
//    }

    fun getCoinById(id:String) = viewModelScope.launch {
        internetConnectionForApi(id)
    }


    private fun handleResponse(response: Response<Coin>):Resource<Coin>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(data = resultResponse)
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

    private suspend fun internetConnectionForApi(id:String){
        coinsResponse.postValue(Resource.Loading())
        try{
            if(internetConnection(this.getApplication())){
                val response = repository.getCoinById(id)
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