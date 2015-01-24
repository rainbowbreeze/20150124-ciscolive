package it.rainbowbreeze.libs.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;

/**
 *
 * Remember: Key names have to be equal to the ones in the xml file, otherwise two different
 *  settings are managed
 *
 * Created by alfredomorresi on 05/12/14.
 */
public abstract class RainbowAppPrefsManager {
    private static final String LOG_TAG = RainbowAppPrefsManager.class.getSimpleName();

    private final IRainbowLogFacility mLogFacility;

    public final String mPrefsFileName;  // File name where values are saved
    private final int mDefaultValueResId;  // XML resource for dafault values
    private final Context mAppContext;
    protected final SharedPreferences mAppPreferences;
    protected SharedPreferences.Editor mSharedEditor;
    private boolean mSaveInBatch;

    public RainbowAppPrefsManager(
            Context appContext,
            String prefsFileName,
            int defaultValueResId,
            IRainbowLogFacility logFacility) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mPrefsFileName = prefsFileName;
        mDefaultValueResId = defaultValueResId;
        mAppPreferences = appContext.getSharedPreferences(mPrefsFileName, Context.MODE_PRIVATE);
    }

    private boolean hasDefaultValuesBeenSet() {
        // See http://developer.android.com/reference/android/preference/PreferenceManager.html#KEY_HAS_SET_DEFAULT_VALUES
        SharedPreferences appPreferences = mAppContext.getSharedPreferences(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, Context.MODE_PRIVATE);
        boolean defaultValuesSet = appPreferences.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false);
        return defaultValuesSet;
    }

    /**
     * Sets default values for the preferences, given the XML file
     *
     * @param force if true, forces to set again default values
     */
    public void setDefaultValues(boolean force) {
        if (!hasDefaultValuesBeenSet() || force) {
            mLogFacility.v(LOG_TAG, "Setting default preference values");
            // This call sets also the system flag
            if (mDefaultValueResId > 0) {
                PreferenceManager.setDefaultValues(
                        mAppContext,
                        mPrefsFileName,
                        Context.MODE_PRIVATE,
                        mDefaultValueResId,
                        force);
            }
            // Adds customized values
            setBatchSave();
            setDefaultValuesInternal();
            save();
        }
    }

    /**
     * Manually sets all the values that aren't present in the default values XML
     *  files. Called when default values are set.
     *
     * @return
     */
    protected abstract void setDefaultValuesInternal();

    /**
     * Set batch save mode. When set, remember to call {@link #save()} at the end of your changes.
     * @return
     */
    public RainbowAppPrefsManager setBatchSave() {
        mSaveInBatch = true;
        return this;
    }
    public boolean save() {
        boolean result = mSharedEditor.commit();
        if (result) {
            mSaveInBatch = false;
            mSharedEditor = null;
        }
        return result;
    }
    public RainbowAppPrefsManager cancelBatchSave() {
        mSaveInBatch = false;
        mSharedEditor = null;
        return this;
    }


    protected void openSharedEditor() {
        if (null == mSharedEditor) {
            mSharedEditor = mAppPreferences.edit();
        }
    }

    /**
     * Do not save while in batch edit mode, otherwise saves preferences
     * @return
     */
    protected boolean saveIfNeeded() {
        return mSaveInBatch
                ? false
                : save();
    }

}
