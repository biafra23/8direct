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
import android.widget.Toast;

import com.jaeckel.direct.adapters.DirectionPagerAdapter;
import com.jaeckel.direct.fragments.DirectionFragment;
import com.jaeckel.direct.nfc.DistributionNfc;
import com.jaeckel.direct.nfc.DistributionNfc.NfcPayloadCallback;
import com.jaeckel.direct.util.DirectionHelper;
import com.jaeckel.direct.util.NotificationHelper;

public class DirectActivity extends FragmentActivity implements ActionBar.TabListener, NfcPayloadCallback
{

   /**
    * <code>MIME_TYPE</code> indicates/is used for.
    */
   private static final String NFC_MIME_TYPE = "application/vdn.com.jaeckel.direct.distribute";
   private static final String EXTRA_ACTIVATED = "EXTRA_ACTIVATED";
   private ViewPager viewPager;
   private DirectionPagerAdapter directionPagerAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_direct);

      Log.d(App.TAG, "DirectActivity");

      Intent intent = getIntent();
      if (intent.hasExtra(NfcAdapter.EXTRA_NDEF_MESSAGES))
      {
         boolean[] payload = (boolean[]) DistributionNfc.readNfcMessage(intent, NFC_MIME_TYPE);
         App.getInstance().setActivated(new boolean[8]);
         //            assigned = payload;
         App.getInstance().setActivated(payload);
         for (int i = 0; i < payload.length; i++)
         {

            NotificationHelper.raiseNotification(DirectionHelper.directionToString(i), payload[i]);

         }

      }
      else if (savedInstanceState != null)
      {
         App.getInstance().setActivated(savedInstanceState.getBooleanArray(EXTRA_ACTIVATED));
         //            assigned = savedInstanceState.getBooleanArray(EXTRA_ASSIGNED);

      }
      else
      {
         App.getInstance().setActivated(new boolean[8]);
      }
      Log.d(App.TAG, "onCreate: " + toString());
      directionPagerAdapter = new DirectionPagerAdapter(getSupportFragmentManager(), getResources(), App.getInstance());

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
         actionBar.addTab(actionBar.newTab().setText(directionPagerAdapter.getPageTitle(i)).setTabListener(this));
      }

      DistributionNfc.registerNfcMessageCallback(this, NFC_MIME_TYPE, this);
   }

   /**
    * Dispatch onResume() to fragments.
    */
   @Override
   protected void onResume()
   {
      super.onResume();

   }

   @Override
   protected void onSaveInstanceState(Bundle outState)
   {
      super.onSaveInstanceState(outState);
      outState.putBooleanArray(EXTRA_ACTIVATED, App.getInstance().getActivated());
   }

   @Override
   public void onBackPressed()
   {
      super.onBackPressed();
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
      boolean[] payload = App.getInstance().getActivated().clone();
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
      Log.d(App.TAG, "My shared : " + Arrays.toString(App.getInstance().getActivated()));
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
                     App.getInstance().getActivated()[i] = true;
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
      return "DirectActivity [activated=" + Arrays.toString(App.getInstance().getActivated()) + "]";
   }

}