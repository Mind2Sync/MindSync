package com.devdreamerx.mind_sync.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devdreamerx.mind_sync.model.PredictionResponse

//class SharedViewModel : ViewModel() {
//    var predictionResponse: PredictionResponse? = null
//}

class SharedViewModel : ViewModel() {
    private val _predictionResponse = MutableLiveData<PredictionResponse?>()
    val predictionResponse: LiveData<PredictionResponse?> = _predictionResponse

    fun setPredictionResponse(response: PredictionResponse?) {
        _predictionResponse.value = response
    }
}