package com.ebay.manualapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SMSReceiver : BroadcastReceiver() {
    private val TAG = "SMSReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "BroadcastReceiver Received")

        if ("android.provider.Telephony.SMS_RECEIVED" == intent.action) {
            val smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent)[0]
            val message = smsMessage.messageBody.toString()
            Log.d(TAG, "SMS Message: $message")
        }
    }
}
