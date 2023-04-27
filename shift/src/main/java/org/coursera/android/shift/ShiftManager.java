/*
 Copyright 2015 Coursera Inc.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.android.shift;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

/**
 * ShiftManager the singleton that the client will use in order to use Shift.
 * Handles the creation of a ShiftLauncherView {@link #createShiftView()}, the registration of ShiftActions
 * {@link #registerAction(ShiftAction)}, and subscribing to changes in ShiftValues.
 * In order to use ShiftManager, you must first call
 * {@link ShiftManager#initialize(Context, ShiftVisibilityClient)} or
 * {@link ShiftManager#initialize(Context, ShiftVisibilityClient, Class)}
 * before calling {@link ShiftManager#getInstance()}
 */
public class ShiftManager implements ShiftValueSubscriptionManager, ShiftActionsManager, ShiftVisibilityManager {

    private ShiftActionsManagerImpl mActionManager = new ShiftActionsManagerImpl();
    private ShiftValueSubscriptionManagerImpl mValueSubscriptionManager;
    private ShiftValueRegistrationManagerImpl mValueRegistrationManager;
    private ShiftVisibilityManager mVisibilityManager;
    private ShiftPersistenceManager mPersistenceManager;

    private static ShiftManager INSTANCE;

    /**
     * Private constructor
     */
    private ShiftManager() {
        //Do nothing
    }

    /**
     * Returns an instance of ShiftManager.
     * You must call {@link #initialize(Context, ShiftVisibilityClient)} or
     * {@link #initialize(Context, ShiftVisibilityClient, Class)}
     * before calling this method
     *
     * @return
     */
    public static ShiftManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Need to call ShiftManager.initialize() first");
        }
        return INSTANCE;
    }

    /**
     * Initializes ShiftManager.
     * Must call either this method or {@link #initialize(Context, ShiftVisibilityClient, Class)}
     * before calling {@link #getInstance()}
     *
     * @param context
     * @param shiftVisibilityClient Determines whether or not a user can view the ShiftLauncherView
     */
    public static void initialize(Context context, ShiftVisibilityClient shiftVisibilityClient) {
        if (INSTANCE == null) {
            INSTANCE = new ShiftManager();
            INSTANCE.mVisibilityManager = new ShiftVisibilityManagerImpl(shiftVisibilityClient);
            INSTANCE.mPersistenceManager = new ShiftPersistenceManager(context);
            INSTANCE.mValueRegistrationManager = new ShiftValueRegistrationManagerImpl(INSTANCE.mPersistenceManager);
            // SubscriptionManager relies on value registration manager so must initialize
            // mValueRegistrationManager first
            INSTANCE.mValueSubscriptionManager = new ShiftValueSubscriptionManagerImpl(context);

        }
    }

    /**
     * Initializes ShiftManager
     * Use this if you want the ability to restart your application when ShiftValues are updated
     * Must call either this method or {@link #initialize(Context, ShiftVisibilityClient, Class)}
     * before calling {@link #getInstance()}
     *
     * @param context
     * @param shiftVisibilityClient Determines whether or not a user can view the ShiftLauncherView
     * @param launcherClass         Your Applications LAUNCHER {@link android.app.Activity} class.
     * @return
     */
    public static void initialize(Context context, ShiftVisibilityClient shiftVisibilityClient, Class launcherClass) {
        if (INSTANCE == null) {
            initialize(context, shiftVisibilityClient);
            if (INSTANCE.mValueSubscriptionManager != null) {
                INSTANCE.mValueSubscriptionManager.setLauncherClassForRestart(launcherClass);
            }
        }
    }

    ShiftActionsManagerImpl getActionManager() {
        return mActionManager;
    }

    ShiftValueSubscriptionManagerImpl getValueSubscriptionManager() {
        return mValueSubscriptionManager;
    }

    ShiftValueRegistrationManagerImpl getValueRegistrationManager() {
        return mValueRegistrationManager;
    }

    ShiftPersistenceManager getPersistenceManager() {
        return mPersistenceManager;
    }

    /**
     * Returns a new ShiftLauncherView.
     * Client can then call {@link ShiftLauncherView#showFloatingIcon(FragmentActivity)} to make the
     * floating icon appear, or they can hook up their own button to show the ShiftMenu
     * using {@link ShiftLauncherView#showShiftMenu(FragmentActivity)}
     *
     * @return
     */
    public ShiftLauncherView createShiftView() {
        return new ShiftLauncherView();
    }

    /**
     * Returns a new ShiftLauncherView and automatically calls
     * {@link ShiftLauncherView#showFloatingIcon(FragmentActivity)} for you
     *
     * @param activity
     * @return
     */
    public ShiftLauncherView createShiftViewAndShowFloatingIcon(FragmentActivity activity) {
        ShiftLauncherView shiftLauncherView = createShiftView();
        shiftLauncherView.showFloatingIcon(activity);
        return shiftLauncherView;
    }

    @Override
    public ShiftVisibilityClient getVisibilityClient() {
        return mVisibilityManager.getVisibilityClient();
    }

    @Override
    public void setVisibilityClient(ShiftVisibilityClient visibilityClient) {
        mVisibilityManager.setVisibilityClient(visibilityClient);
    }

    // Shift Value Subscription Manager Interface

    void notifyShiftListeners(ShiftValue feature) {
        mValueSubscriptionManager.notifyShiftListeners(feature);
    }

    @Override
    public void setLauncherClassForRestart(Class launcherClass) {
        mValueSubscriptionManager.setLauncherClassForRestart(launcherClass);
    }

    @Override
    public void subscribeShiftValueForRestart(ShiftValue shiftValue) {
        mValueSubscriptionManager.subscribeShiftValueForRestart(shiftValue);
    }

    @Override
    public void subscribeToUpdatesForAllShiftValues(ShiftValueListener listener) {
        mValueSubscriptionManager.subscribeToUpdatesForAllShiftValues(listener);
    }

    @Override
    public void subscribeToUpdatesForShiftValue(ShiftValueListener listener, ShiftValue value) {
        mValueSubscriptionManager.subscribeToUpdatesForShiftValue(listener, value);
    }

    @Override
    public void subscribeToUpdatesForShiftValues(ShiftValueListener listener, ShiftValue[] values) {
        mValueSubscriptionManager.subscribeToUpdatesForShiftValues(listener, values);
    }

    @Override
    public void unsubscribeToUpdatesForAllShiftValues(ShiftValueListener listener) {
        mValueSubscriptionManager.unsubscribeToUpdatesForAllShiftValues(listener);
    }

    @Override
    public void unsubscribeToUpdatesForShiftValue(ShiftValueListener listener, ShiftValue value) {
        mValueSubscriptionManager.unsubscribeToUpdatesForShiftValue(listener, value);
    }

    @Override
    public void unsubscribeToUpdatesForShiftValues(ShiftValueListener listener, ShiftValue[] values) {
        mValueSubscriptionManager.unsubscribeToUpdatesForShiftValues(listener, values);
    }

    // Shift Actions Manager Interface

    @Override
    public void registerAction(ShiftAction action) {
        mActionManager.registerAction(action);
    }
}
