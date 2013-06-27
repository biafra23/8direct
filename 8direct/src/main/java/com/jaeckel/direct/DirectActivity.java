package com.jaeckel.direct;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.jaeckel.direct.adapters.DirectionPagerAdapter;
import com.jaeckel.direct.event.ClearDirectionEvent;
import com.jaeckel.direct.fragments.DirectionFragment;
import com.jaeckel.direct.nfc.DistributionNfc;
import com.jaeckel.direct.nfc.DistributionNfc.NfcPayloadCallback;
import com.jaeckel.direct.util.DirectionHelper;
import de.greenrobot.event.EventBus;

import java.util.Arrays;

public class DirectActivity extends FragmentActivity implements ActionBar.TabListener, NfcPayloadCallback, DirectionHolder {

    /**
     * <code>MIME_TYPE</code> indicates/is used for.
     */
    private static final String NFC_MIME_TYPE = "application/vdn.com.jaeckel.direct.distribute";
    private static final String EXTRA_ACTIVATED = "EXTRA_ACTIVATED";
    private static final String EXTRA_ASSIGNED = "EXTRA_ASSIGNED";
    private ViewPager viewPager;
    private DirectionPagerAdapter directionPagerAdapter;
    private static boolean[] assigned = new boolean[8];
    private EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bus = new EventBus().getDefault();
        bus.register(this);

        setContentView(R.layout.activity_direct);

        Log.d(App.TAG, "DirectActivity");

        Intent intent = getIntent();
        if (intent.hasExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)) {
            boolean[] payload = (boolean[]) DistributionNfc.readNfcMessage(intent, NFC_MIME_TYPE);
            App.getInstance().setActivated(new boolean[8]);
            assigned = payload;
        } else if (savedInstanceState != null) {
            App.getInstance().setActivated(savedInstanceState.getBooleanArray(EXTRA_ACTIVATED));
            assigned = savedInstanceState.getBooleanArray(EXTRA_ASSIGNED);
        } else {
            App.getInstance().setActivated(new boolean[8]);
            assigned = new boolean[8];
            for (int i = 0; i < assigned.length; i++) {
                assigned[i] = true;
            }
        }
        Log.d(App.TAG, "onCreate: " + toString());
        directionPagerAdapter = new DirectionPagerAdapter(getSupportFragmentManager(), getResources(), this);

        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_1a1a1a));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(directionPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < directionPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(directionPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

        DistributionNfc.registerNfcMessageCallback(this, NFC_MIME_TYPE, this);
    }

    /**
     * Dispatch onResume() to fragments.
     */
    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(EXTRA_ACTIVATED, App.getInstance().getActivated());
        outState.putBooleanArray(EXTRA_ASSIGNED, assigned);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean consumed;
        switch (item.getItemId()) {
            case R.id.menu_reset_activations:
                for (int i = 0; i < assigned.length; i++) {
                    if (assigned[i]) {
                        App.getInstance().getActivated()[i] = false;
                        final Fragment fragment = directionPagerAdapter.getFragment(i);
                        if (fragment != null && fragment instanceof DirectionFragment) {
                            ((DirectionFragment) fragment).notifyDataSetChanged();
                        }
                    }
                }
                consumed = true;
                break;
            case R.id.menu_reset_assignment:
                for (int i = 0; i < assigned.length; i++) {
                    assigned[i] = true;
                    final Fragment fragment = directionPagerAdapter.getFragment(i);
                    if (fragment != null && fragment instanceof DirectionFragment) {
                        ((DirectionFragment) fragment).notifyDataSetChanged();
                    }
                }
                consumed = true;
                break;
            default:
                consumed = super.onOptionsItemSelected(item);
                break;
        }
        return consumed;
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // NOOP
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // NOOP
    }

    @Override
    public Object preparePayload() {
        boolean[] payload = new boolean[assigned.length];
        boolean transfer = false;
        for (int i = 0; i < assigned.length; i++) {
            if (assigned[i]) {
                payload[i] = transfer;
                transfer = !transfer;
            }
        }
        Log.d(App.TAG, "My current: " + Arrays.toString(assigned));
        Log.d(App.TAG, "My shared : " + Arrays.toString(payload));
        return payload;
    }

    @Override
    public void didTransferPayload(Object payload) {
        final boolean[] shared = (boolean[]) payload;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int sharedTotal = 0;
                for (int i = 0; i < assigned.length; i++) {
                    if (shared[i]) {
                        sharedTotal++;
                        assigned[i] = false;
                    }

                    final Fragment fragment = directionPagerAdapter.getFragment(i);
                    if (fragment != null && fragment instanceof DirectionFragment) {
                        ((DirectionFragment) fragment).notifyDataSetChanged();
                    }
                }
                Toast.makeText(DirectActivity.this, getString(R.string.toast_shared, sharedTotal), Toast.LENGTH_LONG).show();
                Log.d(App.TAG, "didTransferPayload: " + DirectActivity.this.toString());
            }
        });
    }

    @Override
    public String toString() {
        return "DirectActivity [activated=" + Arrays.toString(App.getInstance().getActivated()) + ", assigned=" + Arrays.toString(assigned) + "]";
    }

    @Override
    public void setActivated(String direction, boolean state) {
        int field = DirectionHelper.directionToInt(direction);
        App.getInstance().getActivated()[field] = state;
    }

    @Override
    public boolean isActivated(String direction) {
        int field = DirectionHelper.directionToInt(direction);
        return isActivated(field);
    }

    @Override
    public boolean isActivated(int position) {
        return App.getInstance().getActivated()[position];
    }

    @Override
    public boolean isAssignedToMe(String direction) {
        int field = DirectionHelper.directionToInt(direction);
        return isAssignedToMe(field);
    }

    @Override
    public boolean isAssignedToMe(int position) {
        return assigned[position];
    }

    /**
     * Called by EventBus
     * @param event
     */
    public void onEvent(ClearDirectionEvent event) {

        Log.d(App.TAG, "event." + event.getDirection());

        App.getInstance().getActivated()[event.getDirection()] = false;


    }

}