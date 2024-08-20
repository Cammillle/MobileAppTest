package com.example.mobileapptest.Coin

data class Coin(
    val id:String,
    val symbol:String,
    val name:String,
    val image: Image,
    val description: Description,
    val current_price: Float
    )
