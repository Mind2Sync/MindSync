package com.devdreamerx.mind_sync.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _imageBase64 = MutableLiveData<String>()
    val imageBase64: LiveData<String> get() = _imageBase64

    fun setNameAndImage(name: String, imageBase64: String) {
        _name.value = name
        _imageBase64.value = imageBase64
    }
}
