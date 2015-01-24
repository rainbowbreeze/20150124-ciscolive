package it.rainbowbreeze.ciscolive.logic;

import android.content.Context;

import com.cisco.cmx.model.CMXClientLocation;
import com.cisco.cmx.network.CMXClient;
import com.cisco.cmx.network.CMXClientLocationResponseHandler;
import com.cisco.cmx.network.CMXClientRegisteringResponseHandler;
import com.squareup.otto.Bus;

import java.net.MalformedURLException;

import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.data.AppPrefsManager;
import it.rainbowbreeze.ciscolive.logic.bus.CmxLocationUpdatedEvent;
import it.rainbowbreeze.ciscolive.logic.bus.CmxRegistrationResultEvent;

/**
 * Created by alfredomorresi on 24/01/15.
 */
public class CmxManagerss {
    private static final String LOG_TAG = CmxManagerss.class.getSimpleName();

    private final CMXClient mCmxClient;
    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final AppPrefsManager mAppPrefsManager;
    private final Bus mBus;

    public CmxManagerss(
            Context appContext,
            ILogFacility logFacility,
            AppPrefsManager appPrefsManager,
            Bus bus) {
        mLogFacility = logFacility;
        mAppContext = appContext;
        mAppPrefsManager = appPrefsManager;
        mBus = bus;
        mCmxClient = CMXClient.getInstance();
    }

    public void initialize() {
        mLogFacility.v(LOG_TAG, "Initializing CMX Client");
        mCmxClient.initialize(mAppContext);
        mCmxClient.setConfiguration(getConfiguration());
    }

    public boolean registrationRequired() {
        return !mCmxClient.isRegistered();
    }

    public boolean register() {
        if (mCmxClient.isRegistered()) {
            mLogFacility.v(LOG_TAG, "CMX client already registered");
            return true;
        }

        // Registers the client
        mCmxClient.registerClient(new CMXClientRegisteringResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(Throwable error) {
                mLogFacility.e(LOG_TAG, "Error while registering to CMX server: " + error.getMessage());
                mBus.post(new CmxRegistrationResultEvent(false));
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                mLogFacility.v(LOG_TAG, "Registered with CMX server! :)");
                mBus.post(new CmxRegistrationResultEvent(true));
            }
        });

        return false;
    }

    /**
     * Starts receiving location updates for this client
     */
    public boolean startLocationUpdate() {
        if (!mCmxClient.isRegistered()) return false;

        mLogFacility.v(LOG_TAG, "Starting location updates");
        mCmxClient.startUserLocationPolling(4, new CMXClientLocationResponseHandler() {
            @Override
            public void onUpdate(CMXClientLocation clientLocation) {
                mBus.post(new CmxLocationUpdatedEvent(clientLocation));
            }
        });
        return true;
    }

    public void stopLocationUpdate() {
        mLogFacility.v(LOG_TAG, "Stopping location updates");
        if (mCmxClient.isRegistered()) {
            mCmxClient.stopUserLocationPolling();
        }
    }

    private CMXClient.Configuration getConfiguration() {
        try {
            CMXClient.Configuration config = new CMXClient.Configuration();
            config.setServerAddress(mAppPrefsManager.getServerAddress());
            config.setServerPort(mAppPrefsManager.getServerPort());
            config.setSenderId(mAppPrefsManager.getGCMRegId());
            mLogFacility.v(LOG_TAG, "Configuration: " + config.toString());
            return config;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
