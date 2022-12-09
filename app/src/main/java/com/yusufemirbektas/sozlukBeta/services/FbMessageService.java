package com.yusufemirbektas.sozlukBeta.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.SharedPrefs;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.activities.ContactsNotificationActivity;

public class FbMessageService extends FirebaseMessagingService {
    public static final String SP_MESSAGE_KEY="SP_MESSAGE_KEY";
    public static final String MESSAGE_CHANNEL_ID = "sohbet mesajları";
    private static final String TAG = "NotificationService";
    public static final String NEW_MSG_BI="newMessageIntentKey";
    public static final String SEEN_MSG_BI="seenMessageIntentKey";
    public static final String MSG_USER_CODE="USER_CODE";
    public static final String MSG_TEXT="text";
    public static final String MSG_TITLE="title";
    public static final String MSG_DATE="date";

    private LocalBroadcastManager broadcastManager;

    public static boolean isChatOpened=false;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.i(TAG, "onMessageReceived: ");
        String visibility=message.getData().get("visibility");
        String type=message.getData().get("type");

        //                    "user" => $user,
        //                    "chatMessage" => $chatMessage,
        //                    "chatDate" => $chatDate
        if(type.equals("m")){
            int notifications= SharedPrefs.read(SP_MESSAGE_KEY,0);
            notifications+=1;
            SharedPrefs.write(SP_MESSAGE_KEY,notifications);
            //if it is a chat message
            if(isChatOpened){
                String userCode=message.getData().get("user");
                String chatMessage=message.getData().get("chatMessage");
                String chatDate=message.getData().get("chatDate");
                String title=message.getData().get("title");
                Intent intent=new Intent(NEW_MSG_BI);
                intent.putExtra(MSG_TEXT,chatMessage);
                intent.putExtra(MSG_TITLE,title);
                intent.putExtra(MSG_USER_CODE,userCode);
                intent.putExtra(MSG_DATE,chatDate);
                broadcastManager.sendBroadcast(intent);
            }else{
                if(visibility.equals("1")){
                    String body=notifications+"mesajınız var";
                    String title="sohbet";
                    messageNotify(body, title, 1);
                }
            }

        }
        if(type.equals("f")){
            String body=message.getData().get("body");
            String title=message.getData().get("title");
            String msg=message.getData().get("message");
            if(visibility.equals("1")){
                messageNotify(body, title, 2);
            }
        }

        if(type.equals("s")){
            //user gören kişinin usercodeu
            //chatDate burada görülen mesajın numarası
            //yani 100 numaralı kullanıcı s tipinde bi notifikasyon göndermişse chatDate'e kadar chatDate dahil bütün mesajları görmüş anlamına geliyor
            //değişkenin ismi sikko sadece
            String user=message.getData().get("user");
            String chatDate=message.getData().get("chatDate");
            Intent intent=new Intent(SEEN_MSG_BI);
            intent.putExtra(MSG_USER_CODE,user);
            intent.putExtra(MSG_DATE,chatDate);
            broadcastManager.sendBroadcast(intent);
        }

    }

    private void messageNotify(String text,String title, int id) {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, ContactsNotificationActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MESSAGE_CHANNEL_ID)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("sohbet")
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "sohbet";
            String description = "sohbet mesajları";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MESSAGE_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
