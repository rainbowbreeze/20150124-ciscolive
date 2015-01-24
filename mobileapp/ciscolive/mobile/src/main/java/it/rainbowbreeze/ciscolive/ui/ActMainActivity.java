package it.rainbowbreeze.ciscolive.ui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import it.rainbowbreeze.ciscolive.R;
import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.common.MyApp;
import it.rainbowbreeze.ciscolive.logic.CmxManager;
import it.rainbowbreeze.ciscolive.logic.bus.CmxRegistrationResultEvent;
import it.rainbowbreeze.ciscolive.logic.action.ActionsManager;


public class ActMainActivity extends ActionBarActivity implements ActionBar.TabListener {
    private static final String LOG_TAG = ActMainActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Inject ILogFacility mLogFacility;
    @Inject CmxManager mCmxManager;
    @Inject ActionsManager mActionsManager;
    @Inject Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplicationContext()).inject(this);
        mLogFacility.logStartOfActivity(LOG_TAG, getClass(), savedInstanceState);

        setContentView(R.layout.act_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        if (mCmxManager.registrationRequired()) {
            mLogFacility.v(LOG_TAG, "Registering the CMX server");
            mCmxManager.register();
        } else {
            mLogFacility.v(LOG_TAG, "CMX server already registered, start location updates");
            mActionsManager.getFloorDataAction()
                    .setVenueId("ssss")
                    .setFloorId("alll")
                    .executeAsync();
            mCmxManager.startLocationUpdate();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCmxManager.stopLocationUpdate();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    @Subscribe
    public void onCmxRegistrationResult(CmxRegistrationResultEvent event) {
        mLogFacility.v(LOG_TAG, "CMX registration result: " + event.isRegistered());
        if (event.isRegistered()) {
            mLogFacility.v(LOG_TAG, "Starting location updates");
            mCmxManager.startLocationUpdate();
        } else {
            mLogFacility.e(LOG_TAG, "Cannot start location updates because the registration failed");
        }
    }

}
