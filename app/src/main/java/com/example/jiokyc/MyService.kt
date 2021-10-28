package com.example.jiokyc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.jiokyc.activity.KycSuccessActivity
import com.example.jiokyc.api.*
import com.example.jiokyc.api.kycLinkResponce.AuthResponceModel
import com.example.jiokyc.utlis.Constants.Companion.REQUEST_KEY
import com.example.jiokyc.utlis.ShareUtil
import retrofit2.Call

class MyService : Service() {
    private val TAG = "MyService"
    private val CHANNEL_ID = "NOTIFICATION_CHANNEL"
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate called")
        createNotificationChannel()
        isServiceRunning = true
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called : $REQUEST_KEY")
        var request_key=ShareUtil.getFromPrefs(this@MyService, ShareUtil.REQUEST_KEY)
        request_key?.let { getKycStatus(it) }

        return START_STICKY
    }

    private fun createNotificationChannel() {
            val appName = getString(R.string.app_name)
            val serviceChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    CHANNEL_ID,
                    appName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        isServiceRunning = false
        stopForeground(true)

        // call MyReceiver which will restart this service via a worker
        val broadcastIntent = Intent(this, MyReceiver::class.java)
        sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    companion object {
        @JvmField
        var isServiceRunning: Boolean = false
    }

    init {
        Log.d(TAG, "constructor called")
        isServiceRunning = false
    }

    private fun getKycStatus(REQUEST_KEY: String) {
        try {
            val authApiHelper = ApiClient.getClient().create(AuthApiHelper::class.java)
            val kycStatusModel = RequestKycStatusModel()
            kycStatusModel.request_Key = REQUEST_KEY

            val call: Call<AuthResponceModel> = authApiHelper.getKYCStatus(
                kycStatusModel,"application/json"
            )
            call.enqueue(object : CallbackManager<AuthResponceModel>() {
                override fun onSuccess(o: Any) {
                    val kycLinkResponce = o as AuthResponceModel
                    if (kycLinkResponce.status == 200) {
                        val notificationIntent = Intent(this@MyService, KycSuccessActivity::class.java)
                        val pendingIntent = PendingIntent.getActivity(
                            this@MyService,
                            0, notificationIntent, 0
                        )


                        val notification = NotificationCompat.Builder(this@MyService, CHANNEL_ID)
                            .setContentTitle("Service is Running")
                            .setContentText("Your Kyc is done")
                            .setSmallIcon(R.drawable.dim)
                            .setContentIntent(pendingIntent)
                            .setColor(resources.getColor(R.color.cardview_dark_background))
                            .build()
                        startForeground(1, notification)
                        Toast.makeText(this@MyService,"Kyc update successfully", Toast.LENGTH_LONG).show()

                    } else {
                        Log.d(TAG, kycLinkResponce.message.toString())
                    }
                }

                override fun onError(retroError: RetroError) {
                    Log.d(TAG, retroError.errorMessage)

                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}