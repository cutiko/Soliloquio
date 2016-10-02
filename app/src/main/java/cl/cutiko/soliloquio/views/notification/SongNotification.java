package cl.cutiko.soliloquio.views.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.views.main.MainActivity;


public class SongNotification {

    private static final String NOTIFICATION_TAG = "Song";

    public static void notify(final Context context, final String song) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.FLAG_AUTO_CANCEL)
                .setSmallIcon(R.mipmap.ic_stat_song_24dp)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(song)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setTicker(song)
                .setContentIntent(notificationIntent(context))
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }

    private static PendingIntent notificationIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
