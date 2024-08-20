package com.example.mobileapptest.CoinList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapptest.repository.CoinRepository

class CoinListViewModelProviderFactoryRub(val app:Application, val repository: CoinRepository):ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass:Class<T>):T{
        return CoinListRubViewModel(app, repository) as T
    }


}