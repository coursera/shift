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

interface ShiftValueSubscriptionManager {
    /**
     * Provides the client applications Launcher class to Shift so that {@link ShiftValue}
     * can be registered using {@link #subscribeShiftValueForRestart(ShiftValue)}.
     *
     * @param launcherClass
     */
    void setLauncherClassForRestart(Class launcherClass);

    /**
     * Registers a {@link ShiftValue} to trigger the app to restart.
     * Must first set launcher class for restart using
     * {@link ShiftManager#setLauncherClassForRestart(Class)} before calling this.
     *
     * @param shiftValue
     */
    void subscribeShiftValueForRestart(ShiftValue shiftValue);

    /**
     * Registers your Activity to call shiftValuesUpdated when any
     * {@link ShiftValue}  has been modified
     *
     * @param listener
     */
    void subscribeToUpdatesForAllShiftValues(ShiftValueListener listener);

    /**
     * Registers your ShiftValueListener to call shiftValuesUpdated
     * when the given {@link ShiftValue}  has been modified
     *
     * @param listener
     */
    void subscribeToUpdatesForShiftValue(ShiftValueListener listener,
                                         ShiftValue value);

    /**
     * Registers your Activity to call {@link ShiftValueListener#onShiftValuesUpdated(ShiftValue)}
     * when the given {@link ShiftValue} have been modified
     *
     * @param listener
     */
    void subscribeToUpdatesForShiftValues(ShiftValueListener listener, ShiftValue[] values);

    /**
     * Must be called on your Activity's onDestroy if you previously called
     * {@link #subscribeToUpdatesForAllShiftValues(ShiftValueListener)} in your Activity's onCreate
     * Your Activity will no longer receive updates when a {@link ShiftValue}  has been updated
     *
     * @param listener
     */
    void unsubscribeToUpdatesForAllShiftValues(ShiftValueListener listener);

    /**
     * Must be called on your Activity's onDestroy if you previously called
     * {@link #subscribeToUpdatesForShiftValue(ShiftValueListener, ShiftValue)}
     * in your Activity's onCreate. Your Activity will no longer receive
     * updates when the given {@link ShiftValue}  has been updated
     *
     * @param listener
     */
    void unsubscribeToUpdatesForShiftValue(ShiftValueListener listener, ShiftValue value);

    /**
     * Must be called on your Activity's onDestroy if you previously called
     * {@link #subscribeToUpdatesForShiftValues(ShiftValueListener, ShiftValue[])}
     * in your Activity's onCreate. Your Activity will no longer receive updates when the given
     * {@link ShiftValue}  have been updated
     *
     * @param listener
     */
    void unsubscribeToUpdatesForShiftValues(ShiftValueListener listener,
                                            ShiftValue[] values);
}
