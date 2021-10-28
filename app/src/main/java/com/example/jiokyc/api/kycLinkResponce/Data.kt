package com.example.jiokyc.api.kycLinkResponce

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class Data {
    @SerializedName("url")
    @Expose
    var url=""

    @SerializedName("Request_Key")
    @Expose
    var requestKey=""
}