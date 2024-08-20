package com.example.mobileapptest.CoinList

data class CoinListState(
    val isLoading : Boolean = false,
    val coinsList : List<CoinList> = emptyList(),
    val error : String = ""
)
