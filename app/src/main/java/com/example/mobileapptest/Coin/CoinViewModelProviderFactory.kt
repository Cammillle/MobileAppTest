package com.example.mobileapptest.Coin

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapptest.repository.CoinRepository

class CoinViewModelProviderFactory(val app:Application, val repository: CoinRepository):ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass:Class<T>):T{
        return CoinViewModel(app, repository) as T
    }


}