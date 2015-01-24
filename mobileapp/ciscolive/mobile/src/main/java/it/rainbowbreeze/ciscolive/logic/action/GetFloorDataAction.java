package it.rainbowbreeze.ciscolive.logic.action;

import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.logic.CmxManagerss;

/**
 * Created by alfredomorresi on 24/01/15.
 */
public class GetFloorDataAction extends ActionsManager.BaseAction {
    private static final String LOG_TAG = GetFloorDataAction.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final CmxManagerss mCmxManager;

    public GetFloorDataAction(ILogFacility logFacility, ActionsManager actionManager, CmxManagerss cmxManager) {
        super(logFacility, actionManager);
        mLogFacility = logFacility;
        mCmxManager = cmxManager;
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    protected void doYourStuff() {

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
