package com.example.mobileapptest.repository

import com.example.mobileapptest.Coin.Coin
import com.example.mobileapptest.CoinList.CoinList
import com.example.mobileapptest.api.RetrofitInstance

class CoinRepository {

    suspend fun getAllUsdCoins() = RetrofitInstance.api.getAllUsdCoins()

    suspend fun getAllRubCoins() = RetrofitInstance.api.getAllRubCoins()

    suspend fun getCoinById(id:String) = RetrofitInstance.api.getCoinById(id)

}