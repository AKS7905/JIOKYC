package com.example.jiokyc.api
import com.example.jiokyc.api.kycLinkResponce.AuthResponceModel
import com.example.jiokyc.utlis.Constants.Companion.KYC_REQUEST_LINK
import com.example.jiokyc.utlis.Constants.Companion.KYC_STATUS_CHECK
import retrofit2.Call
import retrofit2.http.*


interface AuthApiHelper {


    @POST(KYC_REQUEST_LINK)
    fun getKYCLink(
        @Body modelRequest: RequestAuthModel, @Header("content-type")
        contentType: String, @Header("Authentication") Authentication: String
    ): Call<AuthResponceModel>

    @POST(KYC_STATUS_CHECK)
    fun getKYCStatus(
        @Body kycStatusModel: RequestKycStatusModel, @Header("content-type")
        contentType: String
    ): Call<AuthResponceModel>

}




