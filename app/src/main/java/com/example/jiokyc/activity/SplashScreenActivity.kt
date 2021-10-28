package com.example.jiokyc.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jiokyc.R
import com.example.jiokyc.api.*
import com.example.jiokyc.api.kycLinkResponce.AuthResponceModel
import com.example.jiokyc.utlis.Constants.Companion.API_TOKEN
import com.example.jiokyc.utlis.Constants.Companion.KYC_LINK
import com.example.jiokyc.utlis.Constants.Companion.REQUEST_KEY
import com.example.jiokyc.utlis.ShareUtil
import retrofit2.Call
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        getKycLink()
        Timer().schedule(3000) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getKycLink() {
        try {
            val authApiHelper = ApiClient.getClient().create(AuthApiHelper::class.java)
            val kycLinkRequest = RequestAuthModel()
            kycLinkRequest.requestID="DMI202100073"
            kycLinkRequest.requestType="DMI-eKYC"
            kycLinkRequest.timeOut="3000"
            kycLinkRequest.mobileNumber="9711990148"
            kycLinkRequest.borrowerEmail="dmi202100073@example.com"
            kycLinkRequest.loanNumber="00Q5D00000345ieUAA"
            kycLinkRequest.contactID="0035D00001OZsKhQAL"
            kycLinkRequest.callbackURL="https://play.google.com/store/apps/details?id=com.google.android.apps.nbu.paisa.user&hl=en_IN&gl=US"
            kycLinkRequest.faceAuth="1"
            kycLinkRequest.borrowerImageURL=""
            kycLinkRequest.oTPAuth="0"
            kycLinkRequest.partnerName="Oppo"
            kycLinkRequest.sendLink="1"
            kycLinkRequest.consentMessage="I have read and agreed to the Terms of Use and hereby allow DMI Finance partners to receive my KYC information."
            kycLinkRequest.consentMessagePartner="I hereby agree to allow DMI Finance to process my Digilocker account and fetch my KYC information stored at Digilocker, I understand that DigiLocker is an online service provided by Ministry of Electronics and IT (MeitY), Government of India under its Digital India initiative."
            val call: Call<AuthResponceModel> = authApiHelper.getKYCLink(
                kycLinkRequest,
                "application/json",
                API_TOKEN
            )
            call.enqueue(object : CallbackManager<AuthResponceModel>() {
                override fun onSuccess(o: Any) {
                    val kycLinkResponce = o as AuthResponceModel
                    if(kycLinkResponce.status==200){
                        KYC_LINK= kycLinkResponce.data!!.url
                        REQUEST_KEY=kycLinkResponce.data!!.requestKey
                        ShareUtil.saveToPrefs(
                            this@SplashScreenActivity,
                            ShareUtil.REQUEST_KEY,
                            kycLinkResponce.data!!.requestKey
                        )

                    }else{
                        Toast.makeText(this@SplashScreenActivity,kycLinkResponce.message,Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(retroError: RetroError) {
                    Toast.makeText(this@SplashScreenActivity,retroError.errorMessage,Toast.LENGTH_LONG).show()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



}