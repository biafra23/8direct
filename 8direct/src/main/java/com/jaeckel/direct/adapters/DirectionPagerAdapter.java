package com.jaeckel.direct.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jaeckel.direct.DirectionHolder;
import com.jaeckel.direct.R;
import com.jaeckel.direct.fragments.DirectionFragment;

/**
 * Created by quirijngb on 12/06/2013.
 */
public class DirectionPagerAdapter extends FragmentStatePagerAdapter
{

   private static String[] directions;
   private static String[] directionsLong;
   private SparseArray<Fragment> pageReferenceMap;

   public DirectionPagerAdapter(FragmentManager fm, Resources resources, DirectionHolder holder)
   {
      super(fm);
      directions = resources.getStringArray(R.array.short_directions);
      directionsLong = resources.getStringArray(R.array.long_directions);
      pageReferenceMap = new SparseArray<Fragment>(8);
   }

   @Override
   public Fragment getItem(int index)
   {
      Fragment fragment = DirectionFragment.newInstance(directions[index], directionsLong[index]);
      pageReferenceMap.put(index, fragment);
      return fragment;
   }

   @Override
   public void destroyItem(ViewGroup container, int position, Object object)
   {
      super.destroyItem(container, position, object);
      pageReferenceMap.remove(position);
   }

   public Fragment getFragment(int key)
   {
      return pageReferenceMap.get(key);
   }

   @Override
   public int getCount()
   {
      return 8;
   }

   @Override
   public CharSequence getPageTitle(int position)
   {
      return "" + (directions[position]);
   }
}
