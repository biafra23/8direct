package com.jaeckel.direct.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jaeckel.direct.R;
import com.jaeckel.direct.fragments.DirectionFragment;

/**
 * Created by quirijngb on 12/06/2013.
 */
public class DirectionPagerAdapter extends FragmentPagerAdapter
{

   private static String[] mDirections;
   private static String[] mDirectionsLong;

   public DirectionPagerAdapter(FragmentManager fm, Resources resources)
   {
      super(fm);

      mDirections = resources.getStringArray(R.array.short_directions);
      mDirectionsLong = resources.getStringArray(R.array.long_directions);

   }

   @Override
   public int getItemPosition(Object object)
   {
      return POSITION_NONE;
   }

   @Override
   public Fragment getItem(int i)
   {
      Fragment fragment = DirectionFragment.newInstance(mDirections[i], mDirectionsLong[i]);
      return fragment;
   }

   @Override
   public int getCount()
   {
      return 8;
   }

   @Override
   public CharSequence getPageTitle(int position)
   {
      return "" + (mDirections[position]);
   }
}
