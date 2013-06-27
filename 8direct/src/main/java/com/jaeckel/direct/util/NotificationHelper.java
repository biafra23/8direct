package com.jaeckel.direct.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.jaeckel.direct.App;
import com.jaeckel.direct.DirectActivity;
import com.jaeckel.direct.NotificationReceiver;

/**
 * @author flashmop
 * @date 27.06.13 21:19
 */
public class NotificationHelper {

    public static void raiseNotification(String direction, boolean activated) {

        NotificationCompat.Builder b = new NotificationCompat.Builder(App.getInstance());
        b.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis());
        b.setContentTitle("Please deploy")
                .setContentText("Direction: " + direction)
                .setSmallIcon(DirectionHelper.getDirectionIcon(direction))
                .setTicker("Your direction: " + direction);
        Intent outbound = new Intent(Intent.ACTION_VIEW);
//        outbound.setDataAndType(Uri.fromFile(output), inbound.getType());
        b.setContentIntent(PendingIntent.getActivity(App.getInstance(), 0, outbound, 0));

        b.setDeleteIntent(clearDirectionIntent(direction));

        NotificationManager mgr =
                (NotificationManager) App.getInstance().getSystemService(Service.NOTIFICATION_SERVICE);

        Notification notification = b.getNotification();

        mgr.notify(DirectionHelper.getDirectionIcon(direction), notification);
    }

    private static PendingIntent clearDirectionIntent(String direction) {

        Intent intent = new Intent(App.getInstance(), NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.EXTRA_CLEARED, direction);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                App.getInstance(),
                DirectionHelper.directionToInt(direction),
                intent, 0);

        return pendingIntent;
    }
}
