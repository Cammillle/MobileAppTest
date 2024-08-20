package com.example.mobileapptest

import com.example.mobileapptest.Coin.Coin
import retrofit2.http.GET
import retrofit2.http.Path


interface CoinGeckoApi {

    @GET("/api/v3/coins/{id}")
    suspend fun getCoinById(@Path("id")id:String): Coin

    @GET("/api/v3/coins/markets?vs_currency=usd&per_page=20")
    suspend fun getAllUsdCoins(): List<CoinList>

    @GET("/api/v3/coins/markets?vs_currency=rub&per_page=20")
    suspend fun getAllRubCoins(): List<CoinList>



}