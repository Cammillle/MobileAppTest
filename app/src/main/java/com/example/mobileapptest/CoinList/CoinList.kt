package com.example.mobileapptest.CoinList

import com.example.mobileapptest.Coin.Description

data class CoinList(
    val id:String,
    val symbol:String,
    val name:String,
    val image: String,
    val description: Description,
    val current_price: Float,
    val market_cap_change_percentage_24h: Float
)

