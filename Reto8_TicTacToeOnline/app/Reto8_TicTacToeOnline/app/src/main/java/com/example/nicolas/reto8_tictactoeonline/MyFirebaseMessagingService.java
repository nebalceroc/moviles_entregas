package com.example.nicolas.reto8_tictactoeonline;


import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    public MyFirebaseMessagingService(){

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG,"NOTIF");
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        int id = 0;
        if (remoteMessage.getData().get("nkind")!=null) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            Log.e(TAG, remoteMessage.getData().get("nkind"));
            switch(remoteMessage.getData().get("nkind")){
                case "JOIN":
                    id = Constants.USER_JOINED;
                    break;

                case "ACTION":
                    id = Constants.USER_MOVED;
                    break;

            }
            sendNotification(remoteMessage,id);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),0);

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */
    private void sendNotification(RemoteMessage remoteMessage, int id) {
        Intent pushNotification;
        switch (remoteMessage.getData().get("nkind")){
            case "JOIN":
                pushNotification = new Intent(Constants.JOIN_GAME_NOTIFICATION);
                pushNotification.putExtra("guest", remoteMessage.getData().get("guest"));
                pushNotification.putExtra("kind", "join");
                //Log.e(TAG,"TS:"+remoteMessage.getData().get("time_sent")+"Sender:"+remoteMessage.getData().get("sender")+" msg:"+remoteMessage.getData().get("message"));
                break;
            case "ACTION":
                pushNotification = new Intent(Constants.JOIN_GAME_NOTIFICATION);
                pushNotification.putExtra("board", remoteMessage.getData().get("board"));
                pushNotification.putExtra("turn", remoteMessage.getData().get("turn"));
                pushNotification.putExtra("kind", "action");
                break;
            default:
                pushNotification = new Intent(Constants.BROADCAST_DEFAULT_NOTIFICATION);
                break;
        }
        //Adding notification data to the intent
        pushNotification.putExtra("body", remoteMessage.getNotification().getBody());
        pushNotification.putExtra("title", remoteMessage.getNotification().getTitle());


        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            //If app is in foreground displaying push notification
            notificationHandler.showNotificationMessage(remoteMessage, id);
        }
    }

}
