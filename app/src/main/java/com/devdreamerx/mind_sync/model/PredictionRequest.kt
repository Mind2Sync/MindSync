package com.devdreamerx.mind_sync.model

data class PredictionRequest(
    val name: String,
    val age: Int,
    val sex: String,
    val country: String,
    val mriScan: String?,
)
