package com.newsdetail.utility.Firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Firebase Utility to show Notification
 */
public class FirebasePushNotification extends FirebaseMessagingService {

    private final String TAG = FirebasePushNotification.class.getSimpleName();

    /**
     * Receive Firebase Notification
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //check data payload not empty and build notification
        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            NotificationTemplateUtility.createNotification(this, remoteMessage.getData());
        }
    }

}
