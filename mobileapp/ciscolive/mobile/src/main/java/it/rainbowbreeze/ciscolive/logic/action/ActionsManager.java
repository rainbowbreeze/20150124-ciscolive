package it.rainbowbreeze.ciscolive.logic.action;

import android.content.Context;

import com.squareup.otto.Bus;

import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.logic.CmxManagerss;
import it.rainbowbreeze.libs.logic.RainbowActionsManager;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class ActionsManager extends RainbowActionsManager {
    private static final String LOG_TAG = ActionsManager.class.getSimpleName();
    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final CmxManagerss mCmxManager;

    public ActionsManager(Context appContext, ILogFacility logFacility, CmxManagerss cmxManager, Bus bus) {
        super(logFacility, LOG_TAG);
        mAppContext = appContext;
        mLogFacility = logFacility;
        mCmxManager = cmxManager;
    }

    /*
    public SubscribeClientToGcmAction subscribeClientToGcm() {
        return new SubscribeClientToGcmAction(mLogFacility, mBackendHelper, this);
    }
    */

}
