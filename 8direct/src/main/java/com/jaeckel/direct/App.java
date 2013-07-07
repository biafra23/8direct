package com.jaeckel.direct;

import android.app.Application;
import android.util.Log;

import com.jaeckel.direct.event.DirectionChangedEvent;
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

   @Override
   public void setActivated(boolean[] activated)
   {
      this.activated = activated;
      for (int i = 0; i < activated.length; i++)
      {

         NotificationHelper.raiseNotification(DirectionHelper.directionToString(i), activated[i]);
      }
   }

   @Override
   public void setActivated(String direction, boolean state)
   {
      int field = DirectionHelper.directionToInt(direction);
      setActivated(field, state);
   }

   @Override
   public void setActivated(int field, boolean state)
   {
      getActivated()[field] = state;

      String direction = DirectionHelper.directionToString(field);
      NotificationHelper.raiseNotification(direction, state);

      bus.post(new DirectionChangedEvent(field, state));
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
   public void onEvent(DirectionChangedEvent event)
   {
      Log.d(App.TAG, "event." + event.getDirection());
      activated[event.getDirection()] = event.isActivated();
   }
}
