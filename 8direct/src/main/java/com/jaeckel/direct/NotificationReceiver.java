package com.jaeckel.direct;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.jaeckel.direct.event.ClearDirectionEvent;
import com.jaeckel.direct.util.DirectionHelper;
import de.greenrobot.event.EventBus;

/**
 * @author flashmop
 * @date 27.06.13 22:23
 */
public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_CLEARED = "clear_direction";

    @Override
    public void onReceive(Context context, Intent intent) {

        EventBus bus = EventBus.getDefault();

        Log.d(App.TAG, "NotificationReceiver.onReceive: " + intent);

        Log.d(App.TAG, "intent: " + intent);
        if (intent != null) {
            Bundle extras = intent.getExtras();
            Log.d(App.TAG, "extras: " + extras);
            if (extras != null) {
                String clearDirection = extras.getString(EXTRA_CLEARED);
                Log.d(App.TAG, "clearDirection: " + clearDirection);


                final int i = DirectionHelper.directionToInt(clearDirection);

                App.getInstance().getActivated()[i] = false;
                bus.post(new ClearDirectionEvent(i));
//                if (clearDirection != null) {
//                    Toast.makeText(this, "clearDirection: " + clearDirection, Toast.LENGTH_SHORT).show();
//                }
            }
        }

    }
}
