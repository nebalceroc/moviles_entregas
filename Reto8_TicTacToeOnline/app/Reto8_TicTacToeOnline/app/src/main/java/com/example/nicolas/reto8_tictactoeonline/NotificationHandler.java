package com.example.nicolas.reto8_tictactoeonline;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class NotificationHandler {

    private Context mContext;

    public NotificationHandler(Context mContext) {
        this.mContext = mContext;
    }


    //This method would display the notification
    public void showNotificationMessage(RemoteMessage remoteMessage, int id) {
        Intent pushNotification;
        switch (remoteMessage.getData().get("nkind")){
            case "JOIN":
                pushNotification = new Intent(mContext, MainActivity.class);
                pushNotification.putExtra("msg_id", remoteMessage.getData().get("id"));
                pushNotification.putExtra("sender_id", remoteMessage.getData().get("sender"));
                pushNotification.putExtra("time_sent", remoteMessage.getData().get("time_sent"));
                pushNotification.putExtra("message", remoteMessage.getData().get("message"));
                break;

            default:
                pushNotification = new Intent(mContext, MainActivity.class);
                break;
        }

        pushNotification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, pushNotification,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.nb_tic)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
    }


    //This method will check whether the app is in background or not
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}