package it.rainbowbreeze.ciscolive.logic.action;

import android.text.TextUtils;

import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.domain.Floor;
import it.rainbowbreeze.ciscolive.logic.CmxManager;

/**
 * Created by alfredomorresi on 24/01/15.
 */
public class GetFloorImageAction extends ActionsManager.BaseAction {
    private static final String LOG_TAG = GetFloorImageAction.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final CmxManager mCmxManager;
    private String mVenueId;
    private String mFloorId;

    public GetFloorImageAction(ILogFacility logFacility, ActionsManager actionManager, CmxManager cmxManager) {
        super(logFacility, actionManager);
        mLogFacility = logFacility;
        mCmxManager = cmxManager;
    }

    public GetFloorImageAction setFloorId(String newValue) {
        mFloorId = newValue;
        return this;
    }

    public GetFloorImageAction setVenueId(String newValue) {
        mVenueId = newValue;
        return this;
    }

    @Override
    protected boolean isDataValid() {
        return !TextUtils.isEmpty(mVenueId) && !TextUtils.isEmpty(mFloorId);
    }

    @Override
    protected void doYourStuff() {
        mCmxManager.getFloorImage(mVenueId, mFloorId);
    }

    @Override
    protected ConcurrencyType getConcurrencyType() {
        return ConcurrencyType.SingleInstance;
    }

    @Override
    protected String getUniqueActionId() {
        return LOG_TAG;
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }
}
