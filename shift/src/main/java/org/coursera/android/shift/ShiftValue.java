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


import androidx.annotation.NonNull;

public class ShiftValue implements Comparable<ShiftValue> {
    public final String CATEGORY;
    public final String FEATURE;
    public final String AUTHOR;

    /**
     * There are cases where we want to use a ShiftValue purely for the sake of having
     * a listener to subscribe to
     * Eg. {@link ShiftManager#subscribeToUpdatesForAllShiftValues(ShiftValueListener)} (ShiftValueListener)}
     * We can use this dummy ShiftValue that will not be registered
     * in the ShiftValueManager. This is useful for internal updates in Shift
     */
    ShiftValue() {
        CATEGORY = "";
        FEATURE = "";
        AUTHOR = "";
    }

    ShiftValue(String category, String feature, String author, boolean shouldRestartApplicationOnChange){
        this.CATEGORY = category;
        this.FEATURE = feature;
        this.AUTHOR = author;
        if (shouldRestartApplicationOnChange) {
            ShiftManager.getInstance().subscribeShiftValueForRestart(this);
        }
    }


    @Override
    public String toString() {
        return ShiftValueRegistrationManagerImpl.PREFIX + "{" +
                "category='" + CATEGORY + '\'' +
                ", feature='" + FEATURE + '\'' +
                ", author='" + AUTHOR + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull ShiftValue other) {
        int diff = this.CATEGORY.compareTo(other.CATEGORY);
        if (diff != 0) {
            return diff;
        }
        diff = this.FEATURE.compareTo(other.FEATURE);
        if (diff != 0) {
            return diff;
        }
        return this.AUTHOR.compareTo(other.AUTHOR);
    }
}
