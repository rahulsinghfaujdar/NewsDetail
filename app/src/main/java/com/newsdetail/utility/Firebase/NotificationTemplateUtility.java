package com.newsdetail.utility.Firebase;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.newsdetail.R;
import com.newsdetail.ui.browsing.BrowsingActivity;
import com.newsdetail.constant.GlobalConstants;
import com.newsdetail.utility.CommonUtility;

import java.util.Map;
import java.util.Random;

/**
 * This Class is only use for FirebasePushNotification
 * contains Notification Template for News update
 */
public class NotificationTemplateUtility {

    /**
     * Build Notification for News update and forward on Browsing Activity
     * @param firebasePushNotification = context
     * @param data = data payload
     */
    public static void createNotification(FirebasePushNotification firebasePushNotification, Map<String, String> data) {
        //grab data from data payload map as defined KEYS
        String title = data.get(GlobalConstants.TITLE);
        String url = data.get(GlobalConstants.URL);
        String image = data.get(GlobalConstants.URLTOIMAGE);
        String description = data.get(GlobalConstants.DESCRIPTION);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(url))
        {
            //create unique notification id
            int notificationID = new Random().nextInt();
            //create Notification Builder & config settings
            NotificationCompat.Builder builder = new NotificationCompat.Builder(firebasePushNotification, firebasePushNotification.getResources().getString(R.string.channel_id));
            builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
            builder.setSmallIcon(R.drawable.ic_clock);
            builder.setAutoCancel(true);

            //set Description if not empty
            if (!TextUtils.isEmpty(description)) {
                builder.setContentText(Html.fromHtml(description));
            }

            //set Notification Style as BigPicture & create a bitmap to show big picture
            if (!TextUtils.isEmpty(image)) {
                Bitmap bitmap = CommonUtility.getBitmapfromUrl(image);
                builder.setLargeIcon(bitmap);
                if (bitmap != null) {
                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
                }
            }

            //Create pending intent for click on notification
            Intent intent = new Intent(firebasePushNotification, BrowsingActivity.class);
            intent.putExtra(GlobalConstants.NEWS_TITLE, title);
            intent.putExtra(GlobalConstants.NEWS_BROWSING_LINK, url);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(Integer.toString(notificationID));
            PendingIntent pendingIntent = TaskStackBuilder.create(firebasePushNotification).addNextIntent(intent).getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);

            //Build Notification and notify by NotificationManager
            NotificationManager notificationManager = (NotificationManager) firebasePushNotification.getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(firebasePushNotification.getResources().getString(R.string.channel_id), firebasePushNotification.getResources().getString(R.string.channel_name), NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(firebasePushNotification.getResources().getString(R.string.app_name));
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(notificationID, builder.build());
        }
    }

}
