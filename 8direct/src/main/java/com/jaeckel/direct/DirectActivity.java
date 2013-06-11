package com.jaeckel.direct;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by flashmop on 16.05.13.
 */
public class DirectActivity extends FragmentActivity implements ActionBar.TabListener {


    public final static String[] directions = new String[]{"E", "SE", "S", "SW", "W", "NW", "N", "NE"};
    public final static String[] directionsLong = new String[]{"East", "Southeast", "South", "Southwest", "West", "Northwest", "North", "Northeast"};
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mViewPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.direct_main);

        Log.d(App.TAG, "DirectActivity");

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_1a1a1a));
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     * @param ft  A {@link android.app.FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. The previous tab's unselect and this tab's select will be
     *            executed in a single transaction. This FragmentTransaction does not support
     *            being added to the back stack.
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        Log.d(App.TAG, "Direction: " + directions[tab.getPosition()] + " selected");


        mViewPager.setCurrentItem(tab.getPosition());

    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     * @param ft  A {@link android.app.FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. This tab's unselect and the newly selected tab's select
     *            will be executed in a single transaction. This FragmentTransaction does not
     *            support being added to the back stack.
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Called when a tab that is already selected is chosen again by the user.
     * Some applications may use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     * @param ft  A {@link android.app.FragmentTransaction} for queuing fragment operations to execute
     *            once this method returns. This FragmentTransaction does not support
     *            being added to the back stack.
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new DirectSlideFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return getFragment(directions[i], directionsLong[i]);
        }

        private Fragment getFragment(String direction, String description) {
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putString(DummySectionFragment.ARG_SECTION_DIRECTION, direction);
            Log.d(App.TAG, "description: " + description);
            args.putString(DummySectionFragment.ARG_SECTION_DIRECTION_LONG, description);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "" + (directions[position]);
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.demo_collection_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), DirectActivity.class);
                            startActivity(intent);
                        }
                    });

            // Demonstration of navigating to external activities.
            rootView.findViewById(R.id.demo_external_activity)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create an intent that asks the user to pick a photo, but using
                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                            // the application from the device home screen does not return
                            // to the external activity.
                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                            externalActivityIntent.setType("image/*");
                            externalActivityIntent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            startActivity(externalActivityIntent);
                        }
                    });

            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_DIRECTION = "section_code";
        public static final String ARG_SECTION_DIRECTION_LONG = "section_description";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            final String string = args.getString(ARG_SECTION_DIRECTION);
            Log.d(App.TAG, "String: " + string);
            ((TextView) rootView.findViewById(R.id.direction_short)).setText(string);

            ((TextView) rootView.findViewById(R.id.direction_description)).setText(args.getString(ARG_SECTION_DIRECTION_LONG));

            ImageView portals = (ImageView) rootView.findViewById(R.id.portals);
            if ("sw".equalsIgnoreCase(string)) {
                   portals.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Log.d(App.TAG, "sw clicked");




                       }
                   });
            }
            portals.setImageResource(getDirectionImage(string));
            return rootView;
        }

        private int getDirectionImage(String direction) {

            if ("n".equalsIgnoreCase(direction)) {

                return R.drawable.portal_n;

            } else if ("e".equalsIgnoreCase(direction)) {

                return R.drawable.portal_e;

            } else if ("s".equalsIgnoreCase(direction)) {

                return R.drawable.portal_s;

            } else if ("w".equalsIgnoreCase(direction)) {

                return R.drawable.portal_w;

            } else if ("ne".equalsIgnoreCase(direction)) {
                return R.drawable.portal_ne;

            } else if ("sw".equalsIgnoreCase(direction)) {
                return R.drawable.portal_sw;

            } else if ("nw".equalsIgnoreCase(direction)) {
                return R.drawable.portal_nw;

            } else if ("se".equalsIgnoreCase(direction)) {
                return R.drawable.portal_se;

            } else {
                return R.drawable.portal_n;

            }
        }
    }
}