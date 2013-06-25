package com.jaeckel.direct;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.jaeckel.direct.adapters.DirectionPagerAdapter;
import com.jaeckel.direct.nfc.DistributionNfc;
import com.jaeckel.direct.nfc.DistributionNfc.NfcPayloadCallback;

public class DirectActivity extends FragmentActivity implements ActionBar.TabListener, NfcPayloadCallback, DirectionHolder
{

   /**
    * <code>MIME_TYPE</code> indicates/is used for.
    */
   private static final String NFC_MIME_TYPE = "application/vdn.com.jaeckel.direct.distribute";
   private static final String TAG = "DirectActivity";
   private static final String EXTRA_ACTIVATED = "EXTRA_ACTIVATED";
   private ViewPager viewPager;
   private DirectionPagerAdapter directionPagerAdapter;
   private boolean[] activated;

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
         activated = payload;
      }
      else if (savedInstanceState != null)
      {
         activated = savedInstanceState.getBooleanArray(EXTRA_ACTIVATED);
      }
      else
      {
         activated = new boolean[8];
      }
      directionPagerAdapter = new DirectionPagerAdapter(getSupportFragmentManager(), getResources());

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

   @Override
   protected void onSaveInstanceState(Bundle outState)
   {
      super.onSaveInstanceState(outState);
      outState.putBooleanArray(EXTRA_ACTIVATED, activated);
   }

   @Override
   public void onBackPressed()
   {
      if (viewPager.getCurrentItem() == 0)
      {
         super.onBackPressed();
      }
      else
      {
         viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
      }
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
      boolean[] payload = new boolean[activated.length];
      boolean myturn = true;
      for (int i = 0; i < activated.length; i++)
      {
         if (activated[i])
         {
            if (myturn)
            {
               payload[i] = false;
            }
            else
            {
               payload[i] = true;
            }
            myturn = !myturn;
         }
      }
      Log.d(TAG, "From my " + activated + " sharing " + payload);
      return payload;
   }

   @Override
   public void didTransferPayload(Object payload)
   {
      boolean[] given = (boolean[]) payload;
      for (int i = 0; i < given.length; i++)
      {
         if (given[i] && activated[i])
         {
            activated[i] = false;
         }
      }
      runOnUiThread(new Runnable()
         {
            @Override
            public void run()
            {
               directionPagerAdapter.notifyDataSetChanged();
            }
         });
   }

   @Override
   public boolean isActivated(String direction)
   {
      int field = directionToInt(direction);
      return activated[field];
   }

   @Override
   public void setActivated(String direction, boolean state)
   {
      int field = directionToInt(direction);
      activated[field] = state;
   }

   private int directionToInt(String direction)
   {
      if ("n".equalsIgnoreCase(direction))
      {

         return 6;

      }
      else if ("e".equalsIgnoreCase(direction))
      {

         return 0;

      }
      else if ("s".equalsIgnoreCase(direction))
      {

         return 2;

      }
      else if ("w".equalsIgnoreCase(direction))
      {

         return 4;

      }
      else if ("ne".equalsIgnoreCase(direction))
      {
         return 7;

      }
      else if ("sw".equalsIgnoreCase(direction))
      {
         return 3;

      }
      else if ("nw".equalsIgnoreCase(direction))
      {
         return 5;

      }
      else if ("se".equalsIgnoreCase(direction))
      {
         return 1;

      }
      else
      {
         Log.e(TAG, "Not a valid direction " + direction);
         return -1;

      }
   }

}