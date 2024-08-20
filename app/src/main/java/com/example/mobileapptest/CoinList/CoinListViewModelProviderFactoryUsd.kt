package com.example.mobileapptest.CoinList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapptest.repository.CoinRepository

class CoinListViewModelProviderFactoryUsd(val app:Application, val repository: CoinRepository):ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass:Class<T>):T{
        return CoinListUsdViewModel(app, repository) as T
    }


}