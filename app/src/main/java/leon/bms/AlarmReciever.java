package leon.bms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import leon.bms.activites.aufgabe.AufgabenActivity;
import leon.bms.database.dbAufgabe;

/**
 * Created by Leon E on 30.05.2016.
 */
public class AlarmReciever extends BroadcastReceiver {
    // Create a new Notification Manager called nm
    NotificationManager nm;
    // Create a new Power Manager called pm
    PowerManager pm;
    // Main function for activity
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onRecieve2", "trigger");
        // Set pm to Power Service so that we can access the power options on device
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // Set WHERE the notification is coming 'from' (You can place your app name here if you wish)
        // Check if the screen itself is off or not. If it is then this code below will FORCEFULLY wake up the device even if it is put into sleep mode by user
        if(pm.isScreenOn() == false)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
            wl_cpu.acquire(10000);
        }
        long id = intent.getLongExtra("id",100000);
        if (id != 100000){
            dbAufgabe aufgabe = new dbAufgabe().getAufgabe(id);
            createNotification(context,aufgabe);
        }

    }
    public void createNotification(Context context, dbAufgabe aufgabeLoad) {
        Log.d("onRecieve", "trigger");
        Intent resultIntent2 = new Intent(context, AufgabenActivity.class);
        resultIntent2.putExtra("id", aufgabeLoad.getId());
        TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
        stackBuilder2.addNextIntent(resultIntent2);
        resultIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent2 = stackBuilder2.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //create an Notification
        Bitmap bm = BitmapFactory.decodeResource( context.getResources(),R.drawable.logo_bms);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_assignment_white_48dp)
                .setLargeIcon(bm)
                .setColor(Color.parseColor("#60a532"))
                .setContentTitle(aufgabeLoad.beschreibung)
                .setSubText("1 neue Hausaufgabe")
                .setPriority(2)
                .setVibrate(new long[3])
                .addAction(R.drawable.ic_done_white_18dp, "Erledigt",resultPendingIntent2)
                .setTicker("Neue Hausaufgabe !")
                .setCategory(Notification.CATEGORY_CALL)
                .setContentText(aufgabeLoad.notizen);



        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String multiLines = aufgabeLoad.notizen;
        String[] text;
        String delimiter = "\n";
        text = multiLines.split(delimiter);
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle(aufgabeLoad.beschreibung);
        // Moves events into the expanded layout
        for (int i = 0; i < text.length; i++) {
            inboxStyle.addLine(text[i]);
        }
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, AufgabenActivity.class);
        resultIntent.putExtra("id", aufgabeLoad.getId());
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AufgabenActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        mNotificationManager.notify(1, mBuilder.build());
    }
}
