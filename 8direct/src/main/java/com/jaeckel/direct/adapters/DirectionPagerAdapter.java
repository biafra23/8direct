package com.jaeckel.direct.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jaeckel.direct.App;
import com.jaeckel.direct.DirectActivity;
import com.jaeckel.direct.R;
import com.jaeckel.direct.fragments.DirectionFragment;

/**
 * Created by quirijngb on 12/06/2013.
 */
public class DirectionPagerAdapter  extends FragmentPagerAdapter {

    public DirectionPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int i) {
        return getFragment(DirectActivity.mDirections[i], DirectActivity.mDirectionsLong[i]);
    }

    private Fragment getFragment(String direction, String description) {
        Fragment fragment = new DirectionFragment();
        Bundle args = new Bundle();
        args.putString(DirectionFragment.ARG_SECTION_DIRECTION, direction);
        Log.d(App.TAG, "description: " + description);
        args.putString(DirectionFragment.ARG_SECTION_DIRECTION_LONG, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "" + (DirectActivity.mDirections[position]);
    }
}
