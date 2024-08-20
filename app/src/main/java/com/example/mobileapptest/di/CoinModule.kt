package com.example.mobileapptest.di

//@Module
//@InstallIn(SingletonComponent::class)
//class CoinModule {
//
//    @Provides
//    @Singleton
//    fun provideCoinGeckoRepository(api: CoinGeckoApi): CoinRepository {
//        return CoinRepositoryImplementation(api=api)
//    }
//
////    @Provides
////    @Singleton
////    fun provideJokesApi(): CoinGeckoApi {
////        return Retrofit.Builder()
////            .baseUrl("https://api.coingecko.com/")
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////            .create(CoinGeckoApi::class.java)
////    }
//
//
//}