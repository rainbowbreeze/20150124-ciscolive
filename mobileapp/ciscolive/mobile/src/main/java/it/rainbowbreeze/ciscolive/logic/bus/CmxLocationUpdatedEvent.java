package it.rainbowbreeze.ciscolive.logic.bus;

import com.cisco.cmx.model.CMXClientLocation;

/**
 * Created by alfredomorresi on 24/01/15.
 */
public class CmxLocationUpdatedEvent {
    private final CMXClientLocation mLocation;

    public CmxLocationUpdatedEvent(CMXClientLocation location) {
        this.mLocation = location;
    }

    public double getLat() {
        return mLocation.getGeoCoordinate().getLatitude();
    }

    public double getLong() {
        return mLocation.getGeoCoordinate().getLongitude();
    }

    @Override
    public String toString() {
        return "X: " + mLocation.getMapCoordinate().getX()
                + ", Y: " + mLocation.getMapCoordinate().getY()
                + ", Unit: " + mLocation.getMapCoordinate().getUnit()
                + ", Time: " + mLocation.getLastLocationUpdateTime()
                + ", Venue: " + mLocation.getVenueId()
                + ", Floor: " + mLocation.getFloorId()
                + ", Zone: " + mLocation.getZoneName();
    }
}
