package it.rainbowbreeze.ciscolive.logic;

import android.content.Context;
import android.graphics.Bitmap;

import com.cisco.cmx.model.CMXClientLocation;
import com.cisco.cmx.network.CMXClient;
import com.cisco.cmx.network.CMXClientLocationResponseHandler;
import com.cisco.cmx.network.CMXClientRegisteringResponseHandler;
import com.cisco.cmx.network.CMXImageResponseHandler;
import com.squareup.otto.Bus;

import java.net.MalformedURLException;

import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.data.AppPrefsManager;
import it.rainbowbreeze.ciscolive.domain.Floor;
import it.rainbowbreeze.ciscolive.logic.bus.CmxLocationUpdatedEvent;
import it.rainbowbreeze.ciscolive.logic.bus.CmxRegistrationResultEvent;
import it.rainbowbreeze.ciscolive.logic.bus.FloorImageEvent;

/**
 * Created by alfredomorresi on 24/01/15.
 */
public class CmxManager {
    private static final String LOG_TAG = CmxManager.class.getSimpleName();

    private final CMXClient mCmxClient;
    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final AppPrefsManager mAppPrefsManager;
    private final Bus mBus;

    public CmxManager(
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

    /**
     *
     * Venues:
     *  Id: AIR-MSE-VA-K9:V01:hackmse.cisco.com_efbb44a6-3dc8-11e4-8ec8-0050569144c8:723412731918549037
     *  Name: AvanziBuilding
     *
     * Floors:
     *  Id: 723412731918549053
     *  Hierarchy: System Campus>AvanziBuilding>AvanziFloor
     *  Height: 10
     *  Length: 75
     *  Width: 35
     *  Unit: FEET
     *
     * @param venueId
     * @param floorId
     * @return
     */
    public Floor getFloorInfo(String venueId, String floorId) {
        /**
        mCmxClient.loadVenues(new CMXVenuesResponseHandler() {
            @Override
            public void onSuccess(List<CMXVenue> venues) {
                mLogFacility.v(LOG_TAG, "Total venues: " + venues.size());
                for(CMXVenue venue : venues) {
                    mLogFacility.v(LOG_TAG, "Venue: " + venue.toString());
                }
            }

            @Override
            public void onFailure(Throwable error) {
                mLogFacility.v(LOG_TAG, "Failed to load venues: " + error.getMessage());
            }
        });
         */

        /**
        mCmxClient.loadMaps(venueId, new CMXFloorsResponseHandler() {
            @Override
            public void onSuccess(List<CMXFloor> floors) {
                mLogFacility.v(LOG_TAG, "Total floors: " + floors.size());
                for(CMXFloor floor : floors) {
                    mLogFacility.v(LOG_TAG, "Floor: " + floor.toString());
                }
            }
        });
         */
        return null;
    }

    public Bitmap getFloorImage(final String venueId, final String floorId) {
        mCmxClient.loadFloorImage(venueId, floorId, new CMXImageResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mLogFacility.v(LOG_TAG, "Start loading of map for venue " + venueId + " and floor " + floorId);
            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                super.onSuccess(bitmap);
                mLogFacility.v(LOG_TAG, "Loaded image for floor " + floorId);
                mBus.post(new FloorImageEvent(bitmap, 35, 75));
            }

            @Override
            public void onFailure(Throwable error) {
                super.onFailure(error);
                mLogFacility.e(LOG_TAG, "Failed loading of image for floor: " + error.getMessage());
            }
        });
        return null;
    };
}
