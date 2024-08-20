package com.example.mobileapptest.api

import com.example.mobileapptest.Coin.Coin
import com.example.mobileapptest.CoinList.CoinList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CoinGeckoApi {

    @GET("/api/v3/coins/{id}")
    suspend fun getCoinById(@Path("id")id:String): Response<Coin>

    @GET("/api/v3/coins/markets?vs_currency=usd&per_page=20")
    suspend fun getAllUsdCoins(): Response<List<CoinList>>

    @GET("/api/v3/coins/markets?vs_currency=rub&per_page=20")
    suspend fun getAllRubCoins(): Response<List<CoinList>>

}

