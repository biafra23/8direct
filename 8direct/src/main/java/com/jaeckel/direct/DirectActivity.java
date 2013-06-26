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

public class DirectActivity extends FragmentActivity implements ActionBar.TabListener, NfcPayloadCallback, DirectionHolder
{

   /**
    * <code>MIME_TYPE</code> indicates/is used for.
    */
   private static final String NFC_MIME_TYPE = "application/vdn.com.jaeckel.direct.distribute";
   private static final String TAG = "DirectActivity";
   private static final String EXTRA_ACTIVATED = "EXTRA_ACTIVATED";
   private static final String EXTRA_ASSIGNED = "EXTRA_ASSIGNED";
   private ViewPager viewPager;
   private DirectionPagerAdapter directionPagerAdapter;
   private boolean[] activated = new boolean[8];;
   private boolean[] assigned = new boolean[8];;

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
         activated = new boolean[8];
         assigned = payload;
      }
      else if (savedInstanceState != null)
      {
         activated = savedInstanceState.getBooleanArray(EXTRA_ACTIVATED);
         assigned = savedInstanceState.getBooleanArray(EXTRA_ASSIGNED);
      }
      else
      {
         activated = new boolean[8];
         assigned = new boolean[8];
         for (int i = 0; i < assigned.length; i++)
         {
            assigned[i] = true;
         }
      }
      Log.d(TAG, "onCreate: " + toString());
      directionPagerAdapter = new DirectionPagerAdapter(getSupportFragmentManager(), getResources(), this);

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
      outState.putBooleanArray(EXTRA_ASSIGNED, assigned);
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
      boolean[] payload = new boolean[assigned.length];
      boolean transfer = false;
      for (int i = 0; i < assigned.length; i++)
      {
         if (assigned[i])
         {
            payload[i] = transfer;
            transfer = !transfer;
         }
      }
      Log.d(TAG, "My current: " + Arrays.toString(assigned));
      Log.d(TAG, "My shared : " + Arrays.toString(payload));
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
               for (int i = 0; i < assigned.length; i++)
               {
                  if (shared[i])
                  {
                     sharedTotal++;
                     assigned[i] = false;
                  }

                  final Fragment fragment = directionPagerAdapter.getFragment(i);
                  if (fragment != null && fragment instanceof DirectionFragment)
                  {
                     ((DirectionFragment) fragment).notifyDataSetChanged();
                  }
               }
               Toast.makeText(DirectActivity.this, getString(R.string.toast_shared, sharedTotal), Toast.LENGTH_LONG).show();
               Log.d(TAG, "didTransferPayload: " + DirectActivity.this.toString());
            }
         });
   }

   @Override
   public String toString()
   {
      return "DirectActivity [activated=" + Arrays.toString(activated) + ", assigned=" + Arrays.toString(assigned) + "]";
   }

   @Override
   public void setActivated(String direction, boolean state)
   {
      int field = directionToInt(direction);
      activated[field] = state;
   }

   @Override
   public boolean isActivated(String direction)
   {
      int field = directionToInt(direction);
      return isActivated(field);
   }

   @Override
   public boolean isActivated(int position)
   {
      return activated[position];
   }

   @Override
   public boolean isAssignedToMe(String direction)
   {
      int field = directionToInt(direction);
      return isAssignedToMe(field);
   }

   @Override
   public boolean isAssignedToMe(int position)
   {
      return assigned[position];
   }

   private int directionToInt(String direction)
   {
      if ("e".equalsIgnoreCase(direction))
      {
         return 0;
      }
      else if ("se".equalsIgnoreCase(direction))
      {
         return 1;
      }
      else if ("s".equalsIgnoreCase(direction))
      {
         return 2;
      }
      else if ("sw".equalsIgnoreCase(direction))
      {
         return 3;
      }
      else if ("w".equalsIgnoreCase(direction))
      {
         return 4;
      }
      else if ("nw".equalsIgnoreCase(direction))
      {
         return 5;
      }
      else if ("n".equalsIgnoreCase(direction))
      {
         return 6;

      }
      else if ("ne".equalsIgnoreCase(direction))
      {
         return 7;
      }
      else
      {
         Log.e(TAG, "Not a valid direction " + direction);
         return -1;
      }
   }

}