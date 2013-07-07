package com.jaeckel.direct;

import android.app.Application;
import android.util.Log;

import com.jaeckel.direct.event.ClearDirectionEvent;
import com.jaeckel.direct.util.DirectionHelper;
import com.jaeckel.direct.util.NotificationHelper;

import de.greenrobot.event.EventBus;

/**
 * @author flashmop
 * @date 02.03.13 12:06
 */
public class App extends Application implements DirectionHolder
{

   public final static String TAG = "8DIRECT";

   private static App instance;
   private boolean[] activated = new boolean[8];

   EventBus bus;

   @Override
   public void onCreate()
   {
      super.onCreate();
      instance = this;
      bus = EventBus.getDefault();
      bus.register(this);
   }

   public static App getInstance()
   {
      return instance;
   }

   public boolean[] getActivated()
   {
      return activated;
   }

   public void setActivated(boolean[] activated)
   {
      this.activated = activated;
   }

   @Override
   public void setActivated(String direction, boolean state)
   {
      int field = DirectionHelper.directionToInt(direction);
      getActivated()[field] = state;

      NotificationHelper.raiseNotification(direction, state);
   }

   @Override
   public boolean isActivated(String direction)
   {
      int field = DirectionHelper.directionToInt(direction);
      return isActivated(field);
   }

   @Override
   public boolean isActivated(int position)
   {
      return getActivated()[position];
   }

   /**
    * Called by EventBus
    * 
    * @param event
    */
   public void onEvent(ClearDirectionEvent event)
   {

      Log.d(App.TAG, "event." + event.getDirection());

      getActivated()[event.getDirection()] = false;

   }
}
