package it.rainbowbreeze.libs.logic;

import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class RainbowActionsManager {
    private final IRainbowLogFacility mLogFacility;
    private final Map<String, Object> mExecutionQueue;
    private final String mLogTag;

    public RainbowActionsManager(IRainbowLogFacility logFacility, String logTag) {
        mLogFacility = logFacility;
        mLogTag = logTag;
        mExecutionQueue = new ConcurrentHashMap<>();
    }

    protected void addNewActionIntoQueue(BaseAction action) {
        mExecutionQueue.put(action.getUniqueActionId(), action);
    }

    protected void removeActionFromQueue(BaseAction action) {
        mExecutionQueue.remove(action.getUniqueActionId());
    }

    public boolean isActionAlreadyInTheQueue(BaseAction action) {
        mLogFacility.v(mLogTag, "Checking for action " + action.getUniqueActionId());
        mLogFacility.v(mLogTag, "queue size is " + mExecutionQueue.size());
        return (mExecutionQueue.containsKey(action.getUniqueActionId()));
    }

    /**
     * Base interface for an action
     */
    public static abstract class BaseAction {
        protected static enum ConcurrencyType {
            SingleInstance,    // Only one actions a time
            MultipleInstances  // Actions can be execute multiple times in contemporary
        }
        private final RainbowActionsManager mActionsManager;
        protected final IRainbowLogFacility mLogFacility;

        protected BaseAction(IRainbowLogFacility logFacility, RainbowActionsManager actionManager) {
            mLogFacility = logFacility;
            mActionsManager = actionManager;
        }

        /**
         * Checks validity of extended action data
         * @return
         */
        protected abstract boolean isDataValid();

        /**
         * Executes the commands of the action
         */
        protected abstract void doYourStuff();

        /**
         * Identifies it the action can be executed together with similar actions of the same kind
         * or only one action per time is allowed
         * @return
         */
        protected abstract ConcurrencyType getConcurrencyType();

        /**
         * Returns a unique action identifiers. If {@link it.rainbowbreeze.libs.logic.RainbowActionsManager.BaseAction.ConcurrencyType}
         * is set to {@link it.rainbowbreeze.libs.logic.RainbowActionsManager.BaseAction.ConcurrencyType#MultipleInstances},
         * only one action with this identifier is allowed in the execution queue
         *
         * @return
         */
        protected abstract String getUniqueActionId();

        protected abstract String getLogTag();

        /**
         * Call this method to fire the action and return
         */
        public void executeAsync() {
            checkForDataValidity();

            new Thread(new Runnable() {
                @Override
                public void run() {
                // Checks if there are other actions with same id in the execution queue
                    if (ConcurrencyType.SingleInstance == getConcurrencyType()) {
                        if (mActionsManager.isActionAlreadyInTheQueue(BaseAction.this)) {
                            mLogFacility.v(getLogTag(), "Action has been refused because another is already on the queue - " + getUniqueActionId());
                            return;
                        }
                    }
                    mActionsManager.addNewActionIntoQueue(BaseAction.this);
                    doYourStuff();
                    mActionsManager.removeActionFromQueue(BaseAction.this);
                }
            }).start();
        }

        /**
         * Call this method to fire the action and wait
         */
        public void execute() {
            checkForDataValidity();

            doYourStuff();
        }

        /**
         * Checks if the action data is valid
         */
        private void checkForDataValidity() {
            if (!isCoreDataValid() || !isDataValid()) {
                throw new IllegalArgumentException("Some data is missing in your action, aborting");
            }
        }

        /**
         * Checks validity of core action data
         * @return
         */
        protected boolean isCoreDataValid() {
            boolean uniqueIdIsCorrect;
            if (ConcurrencyType.SingleInstance == getConcurrencyType()) {
                uniqueIdIsCorrect = !TextUtils.isEmpty(getUniqueActionId());
            } else {
                // null to getConcurrencyType() means multiple instances possible
                uniqueIdIsCorrect = true;
            }
            return
                    null != mLogFacility &&
                    null != mActionsManager &&
                    uniqueIdIsCorrect;
        }
    }
}
