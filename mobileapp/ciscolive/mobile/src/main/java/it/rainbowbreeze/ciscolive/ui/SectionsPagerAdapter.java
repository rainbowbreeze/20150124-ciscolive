package it.rainbowbreeze.ciscolive.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import it.rainbowbreeze.ciscolive.R;

/**
 * Created by alfredomorresi on 24/01/15.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mAppContext;

    public SectionsPagerAdapter(FragmentManager fm, Context appContext) {
        super(fm);
        mAppContext = appContext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment finalFragment;
        switch (position) {
            case 0:
                finalFragment = new LocationFragment();
                break;
            default:
                // getItem is called to instantiate the fragment for the given page.
                // Return a PlaceholderFragment (defined as a static inner class below).
                finalFragment = PlaceholderFragment.newInstance(position + 1);
        }
        return finalFragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mAppContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mAppContext.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return mAppContext.getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }
}
