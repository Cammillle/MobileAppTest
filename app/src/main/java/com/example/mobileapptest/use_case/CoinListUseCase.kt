package com.example.mobileapptest.use_case

//class CoinListUseCase @Inject constructor(
//    private val repository: CoinRepository
//) {
//    operator fun invoke(page:String): Flow<ResponseState<List<CoinList>>> = flow {
//        try {
//            emit(ResponseState.Loading<List<CoinList>>())
//            val coins = repository.getAllUsdCoins(page)
//            emit(ResponseState.Success<List<CoinList>>(coins))
//        } catch (e: HttpException) {
//            emit(ResponseState.Error<List<CoinList>>(e.localizedMessage ?: "An Unexpected Error"))
//        } catch (e: IOException) {
//            emit(ResponseState.Error<List<CoinList>>("Internet Error"))
//        }
//    }
//}