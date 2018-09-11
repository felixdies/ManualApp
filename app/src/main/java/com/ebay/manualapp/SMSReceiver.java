package com.ebay.manualapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BroadcastReceiver Received");

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            SmsMessage smsMessage = msgs[0];
            String message = smsMessage.getMessageBody().toString();
            Log.d(TAG, "SMS Message: " + message);
        }
    }
}
