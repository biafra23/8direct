package com.jaeckel.direct;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * @author flashmop
 * @date 27.06.13 22:23
 */
public class NotificationReceiver extends BroadcastReceiver
{

   public static final String EXTRA_CLEARED = "clear_direction";

   @Override
   public void onReceive(Context context, Intent intent)
   {
      Log.d(App.TAG, "NotificationReceiver.onReceive: " + intent);

      Log.d(App.TAG, "intent: " + intent);
      if (intent != null)
      {
         Bundle extras = intent.getExtras();
         Log.d(App.TAG, "extras: " + extras);
         if (extras != null)
         {
            String clearDirection = extras.getString(EXTRA_CLEARED);
            Log.d(App.TAG, "clearDirection: " + clearDirection);

            App.getInstance().setActivated(clearDirection, false);
         }
      }

   }
}
