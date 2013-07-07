package com.jaeckel.direct;

import java.util.Arrays;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeckel.direct.adapters.DirectionPagerAdapter;
import com.jaeckel.direct.event.DirectionChangedEvent;
import com.jaeckel.direct.fragments.DirectionFragment;
import com.jaeckel.direct.nfc.DistributionNfc;
import com.jaeckel.direct.nfc.DistributionNfc.NfcPayloadCallback;
import com.jaeckel.direct.util.DirectionHelper;
import com.jaeckel.direct.util.NotificationHelper;

import de.greenrobot.event.EventBus;

public class DirectActivity extends FragmentActivity implements ActionBar.TabListener, NfcPayloadCallback
{

   /**
    * <code>MIME_TYPE</code> indicates/is used for.
    */
   private static final String NFC_MIME_TYPE = "application/vdn.com.jaeckel.direct.distribute";
   private static final String EXTRA_ACTIVATED = "EXTRA_ACTIVATED";
   private ViewPager viewPager;
   private DirectionPagerAdapter directionPagerAdapter;
   private DirectionHolder holder = null;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      EventBus.getDefault().register(this);

      setContentView(R.layout.activity_direct);

      holder = App.getInstance();

      Log.d(App.TAG, "DirectActivity");

      Intent intent = getIntent();
      if (intent.hasExtra(NfcAdapter.EXTRA_NDEF_MESSAGES))
      {
         boolean[] payload = (boolean[]) DistributionNfc.readNfcMessage(intent, NFC_MIME_TYPE);
         holder.setActivated(payload);
      }
      else if (savedInstanceState != null)
      {
         holder.setActivated(savedInstanceState.getBooleanArray(EXTRA_ACTIVATED));
      }
      else
      {
         holder.setActivated(new boolean[8]);
      }
      Log.d(App.TAG, "onCreate: " + toString());
      directionPagerAdapter = new DirectionPagerAdapter(getSupportFragmentManager(), getResources(), holder);

      final ActionBar actionBar = getActionBar();
      actionBar.setHomeButtonEnabled(false);
      actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_1a1a1a));
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

      viewPager = (ViewPager) findViewById(R.id.pager);
      viewPager.setAdapter(directionPagerAdapter);
      viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
         {
            @Override
            public void onPageSelected(int position)
            {
               actionBar.setSelectedNavigationItem(position);
            }
         });
      for (int i = 0; i < directionPagerAdapter.getCount(); i++)
      {

         Tab tab = buildTab(i);
         actionBar.addTab(tab);
      }

      DistributionNfc.registerNfcMessageCallback(this, NFC_MIME_TYPE, this);
   }

   private Tab buildTab(int direction)
   {
      Tab tab = getActionBar().newTab();
      CharSequence title = directionPagerAdapter.getPageTitle(direction);
      tab.setText(title).setTabListener(this);
      if (holder.isActivated(direction))
      {
         tab.setCustomView(R.layout.tab_activated);
      }
      else
      {
         tab.setCustomView(R.layout.tab_deactivated);
      }
      ((TextView) tab.getCustomView().findViewById(R.id.tab_title)).setText(tab.getText());
      return tab;
   }

   @Override
   protected void onSaveInstanceState(Bundle outState)
   {
      super.onSaveInstanceState(outState);
      outState.putBooleanArray(EXTRA_ACTIVATED, holder.getActivated());
   }

   public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
   {
      viewPager.setCurrentItem(tab.getPosition());
   }

   @Override
   public void onTabReselected(Tab tab, FragmentTransaction ft)
   {
      // NOOP
   }

   @Override
   public void onTabUnselected(Tab tab, FragmentTransaction ft)
   {
      // NOOP
   }

   @Override
   public Object preparePayload()
   {
      boolean[] payload = holder.getActivated().clone();
      boolean transfer = false;
      for (int i = 0; i < 8; i++)
      {
         if (payload[i])
         {
            payload[i] = transfer;
            transfer = !transfer;
         }
      }
      Log.d(App.TAG, "My current: " + Arrays.toString(payload));
      Log.d(App.TAG, "My shared : " + Arrays.toString(holder.getActivated()));
      return payload;
   }

   @Override
   public void didTransferPayload(Object payload)
   {
      final boolean[] shared = (boolean[]) payload;
      runOnUiThread(new Runnable()
         {
            @Override
            public void run()
            {
               int sharedTotal = 0;
               for (int i = 0; i < shared.length; i++)
               {
                  if (shared[i])
                  {
                     sharedTotal++;
                  }
                  else
                  {
                     holder.getActivated()[i] = true;
                     NotificationHelper.raiseNotification(DirectionHelper.directionToString(i), true);
                  }

                  final Fragment fragment = directionPagerAdapter.getFragment(i);
                  if (fragment != null && fragment instanceof DirectionFragment)
                  {
                     ((DirectionFragment) fragment).notifyDataSetChanged();
                  }
               }
               Toast.makeText(DirectActivity.this, getString(R.string.toast_shared, sharedTotal), Toast.LENGTH_LONG).show();
               Log.d(App.TAG, "didTransferPayload: " + DirectActivity.this.toString());
            }
         });
   }

   @Override
   public String toString()
   {
      return "DirectActivity [activated=" + Arrays.toString(holder.getActivated()) + "]";
   }

   /**
    * Called by EventBus
    * 
    * @param event
    */
   public void onEvent(DirectionChangedEvent event)
   {
      Log.d(App.TAG, "event." + event.getDirection());
      Tab tab = getActionBar().getTabAt(event.getDirection());
      getActionBar().addTab(buildTab(event.getDirection()), event.getDirection());
      getActionBar().removeTab(tab);

   }
}