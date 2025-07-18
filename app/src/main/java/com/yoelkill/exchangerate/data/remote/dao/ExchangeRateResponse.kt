package com.yoelkill.exchangerate.data.remote.dao

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("purchaseRate")
    val purchaseRate: Double,

    @SerializedName("saleRate")
    val saleRate: Double,

    @SerializedName("type")
    val type: String
)