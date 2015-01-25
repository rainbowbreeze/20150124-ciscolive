package it.rainbowbreeze.ciscolive.logic.action;

import android.content.Context;

import com.squareup.otto.Bus;

import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.logic.CmxManager;
import it.rainbowbreeze.libs.logic.RainbowActionsManager;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class ActionsManager extends RainbowActionsManager {
    private static final String LOG_TAG = ActionsManager.class.getSimpleName();
    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final CmxManager mCmxManager;

    public ActionsManager(Context appContext, ILogFacility logFacility, CmxManager cmxManager, Bus bus) {
        super(logFacility, LOG_TAG);
        mAppContext = appContext;
        mLogFacility = logFacility;
        mCmxManager = cmxManager;
    }

    public GetFloorDataAction getFloorDataAction() {
        return new GetFloorDataAction(mLogFacility, this, mCmxManager);
    }

}
