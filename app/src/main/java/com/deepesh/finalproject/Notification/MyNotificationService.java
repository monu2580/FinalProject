package com.deepesh.finalproject.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.deepesh.finalproject.Activity.FingerPrintActivity;
import com.deepesh.finalproject.Activity.Fragment.AllTeachersFragment;
import com.deepesh.finalproject.Activity.Fragment.DetailsFragment;
import com.deepesh.finalproject.Activity.Fragment.FeedbackActivity;
import com.deepesh.finalproject.Activity.Fragment.SearchFragment;
import com.deepesh.finalproject.Activity.Fragment.StudentsFragment;
import com.deepesh.finalproject.Activity.MainActivity;
import com.deepesh.finalproject.Activity.SplashActivity;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Deepesh on 17-12-2017.
 */

public class MyNotificationService extends FirebaseMessagingService {
    String notification_title;
    String notification_message;
    String click_action;
    String name;
    String tid;
    String imageUrl;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        click_action=remoteMessage.getNotification().getClickAction();
        notification_message=remoteMessage.getNotification().getBody();
        notification_title=remoteMessage.getNotification().getTitle();
        name=remoteMessage.getData().get("name");
        tid=remoteMessage.getData().get("uid");
        imageUrl=remoteMessage.getData().get("imageUrl");

        //set Click Action
        Intent intent=new Intent(click_action);
        intent.putExtra("cuName",name);
        intent.putExtra("tName",name);
        intent.putExtra("tId",tid);
        intent.putExtra("tpic",imageUrl);
        passDataForTeacher();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId)
                                                    .setSmallIcon(R.drawable.appicon)
                                                            .setContentTitle(notification_title)
                                                            .setContentText(notification_message)
                                                            .setAutoCancel(true)
                                                            .setSound(defaultSoundUri)
                                                            .setContentIntent(pendingIntent);

        int notificationId=(int)System.currentTimeMillis();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId , builder.build());


    }
    public void passDataForTeacher(){
        Intent intent1=new Intent(this,MainActivity.class);
        intent1.putExtra(Util.KEY_T$S,"teacher");
        Intent intent2=new Intent(this,AllTeachersFragment.class);
        intent2.putExtra(Util.KEY_T$S,"teacher");
        Intent intent3=new Intent(this,DetailsFragment.class);
        intent3.putExtra(Util.KEY_T$S,"teacher");
        Intent intent4=new Intent(this,StudentsFragment.class);
        intent4.putExtra(Util.KEY_T$S,"teacher");
        Intent intent5=new Intent(this,SearchFragment.class);
        intent5.putExtra(Util.KEY_T$S,"teacher");
        /*Intent intent=new Intent(this,FeedbackActivity.class);
        intent.putExtra("cuName",name);
        intent.putExtra("tName",name);
        intent.putExtra("tId",tid);
        intent.putExtra("tpic",imageUrl);*/

    }

}
