package it.rainbowbreeze.ciscolive.logic.bus;

/**
 * Created by alfredomorresi on 24/01/15.
 */
public class CmxRegistrationResultEvent {
    private final boolean mResult;

    public CmxRegistrationResultEvent(boolean result) {
        mResult = result;
    }

    public boolean isRegistered() {
        return mResult;
    }
}
