package it.rainbowbreeze.ciscolive.data;

import android.content.Context;

import it.rainbowbreeze.ciscolive.common.Bag;
import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.libs.data.RainbowAppPrefsManager;

/**
 *
 * Remember: Key names have to be equal to the ones in the xml file, otherwise two different
 *  settings are managed
 *
 * Created by alfredomorresi on 05/12/14.
 */
public class AppPrefsManager extends RainbowAppPrefsManager {
    private static final String LOG_TAG = AppPrefsManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    public static final String PREFS_FILE_NAME = "PlayTogetherPrefs";
    private static final String NULL_STRING = "null";

    public AppPrefsManager(Context appContext, ILogFacility logFacility) {
        super(appContext, PREFS_FILE_NAME, 0, logFacility);
        mLogFacility = logFacility;
    }

    @Override
    protected void setDefaultValuesInternal() {
        mLogFacility.v(LOG_TAG, "Setting default values of preferences");
    }

    private static final String PREF_GPLUS_LOGIN_DONE = "pref_gpluslogindone";


    /**
     * Google Cloud Message registration id. It should be generated once for each installed app,
     * so even if the user unregisters from the backend, the next time she registers the previous
     * registration id should be user.
     * <p/>
     * Look at GoogleCloudMessaging.html#unregister() reference for detailed instructions
     */
    private static final String PREF_GCM_REGID = "pref_gcmRegId";

    public AppPrefsManager setGCMRegId(String newValue) {
        openSharedEditor();
        mSharedEditor.putString(PREF_GCM_REGID, newValue);
        saveIfNeeded();
        return this;
    }

    /**
     * Google Cloud Message registration id. It should be generated once for each installed app,
     * so even if the user unregisters from the backend, the next time she registers the previous
     * registration id should be user.
     * <p/>
     * Look at GoogleCloudMessaging.html#unregister() reference for detailed instructions
     */
    private static final String PREF_CURRENT_GAME_ID = "pref_currentGameId";

    public long getCurrentGameId() {
        return mAppPreferences.getLong(PREF_CURRENT_GAME_ID, Bag.ID_NOT_SET);
    }

    public AppPrefsManager setCurrentGameId(long newValue) {
        openSharedEditor();
        mSharedEditor.putLong(PREF_CURRENT_GAME_ID, newValue);
        saveIfNeeded();
        return this;
    }
}