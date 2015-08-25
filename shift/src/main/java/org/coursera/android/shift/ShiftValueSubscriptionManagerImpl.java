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
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ShiftValueSubscriptionManagerImpl implements ShiftValueSubscriptionManager {

    private Map<ShiftValue, List<ShiftValueListener>> mValuesToListeners = new HashMap<ShiftValue, List<ShiftValueListener>>();

    private Set<ShiftValue> mApplicationFeatures = new HashSet<ShiftValue>();

    private final ShiftValue ALL_FEATURE = new ShiftValue();

    private Class mLauncherClass;

    private Context mContext;

    ShiftValueSubscriptionManagerImpl(Context context) {
        mContext = context;
        for (ShiftValue entry : ShiftManager.getInstance().getValueRegistrationManager().getShiftValues()) {
            mValuesToListeners.put(entry, new LinkedList<ShiftValueListener>());
        }
        mValuesToListeners.put(ALL_FEATURE, new LinkedList<ShiftValueListener>());
    }

    public void restartApplication() {
        if (mContext != null && mLauncherClass != null) {
            Intent intent = new Intent(mContext, mLauncherClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            throw new IllegalStateException("Attempted to restart application but launcher " +
                    "class has not been provided yet by ShiftManager.setLauncherClassForRestart()");
        }
    }


    public void setLauncherClassForRestart(Class launcherClass) {
        mLauncherClass = launcherClass;
    }

    public void notifyShiftListeners(ShiftValue feature) {

        List<ShiftValueListener> listeners = mValuesToListeners.get(feature);
        if (listeners != null) {
            for (ShiftValueListener listener : listeners) {
                listener.onShiftValuesUpdated(feature);
            }
        }
        List<ShiftValueListener> allListeners = mValuesToListeners.get(ALL_FEATURE);
        if (allListeners != null) {
            for (ShiftValueListener listener : allListeners) {
                listener.onShiftValuesUpdated(feature);
            }
        }
        restartApplicationForFeature(feature);
    }

    /**
     * Restarts the application if the {@link ShiftValue}
     * has been registered using {@link #subscribeShiftValueForRestart(ShiftValue)}
     * Must first set the applications launcher class in {@link #setLauncherClassForRestart(Class)}
     *
     * @param feature
     */
    public void restartApplicationForFeature(ShiftValue feature) {
        if (mApplicationFeatures.contains(feature)) {
            restartApplication();
        }
    }


    public void subscribeShiftValueForRestart(ShiftValue shiftValue) {

        mApplicationFeatures.add(shiftValue);
    }

    public void subscribeToUpdatesForAllShiftValues(ShiftValueListener listener) {

        List<ShiftValueListener> listeners = mValuesToListeners.get(ALL_FEATURE);
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }


    public void subscribeToUpdatesForShiftValue(ShiftValueListener listener,
                                                ShiftValue value) {

        List<ShiftValueListener> listeners = mValuesToListeners.get(value);
        if (listeners != null) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        } else {
            listeners = new ArrayList<ShiftValueListener>();
            listeners.add(listener);
            mValuesToListeners.put(value, listeners);
        }
        mValuesToListeners.get(value).add(listener);
    }

    public void subscribeToUpdatesForShiftValues(ShiftValueListener listener, ShiftValue[] values) {
        for (ShiftValue value : values) {
            subscribeToUpdatesForShiftValue(listener, value);
        }
    }


    public void unsubscribeToUpdatesForAllShiftValues(ShiftValueListener listener) {
        List<ShiftValueListener> listeners = mValuesToListeners.get(ALL_FEATURE);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }


    public void unsubscribeToUpdatesForShiftValue(ShiftValueListener listener, ShiftValue value) {
        List<ShiftValueListener> listeners = mValuesToListeners.get(value);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void unsubscribeToUpdatesForShiftValues(ShiftValueListener listener,
                                                   ShiftValue[] values) {
        for (ShiftValue value : values) {
            unsubscribeToUpdatesForShiftValue(listener, value);
        }
    }
}
