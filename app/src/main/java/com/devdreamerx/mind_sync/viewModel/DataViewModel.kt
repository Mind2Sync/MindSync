package com.devdreamerx.mind_sync.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _file = MutableLiveData<String>()
    val fileType: LiveData<String> get() = _file

    fun setNameAndFileName(name: String, fileType: Uri?) {
        _name.value = name
        _file.value = fileType.toString()
    }
}
