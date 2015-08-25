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
import android.content.SharedPreferences;

/**
 * A simple implementation of ShiftVisibilityClient using SharedPreferences.
 *
 */
public class SimpleVisibilityClient implements ShiftVisibilityClient {

    private final String KEY_IS_VISIBLE = "shift_is_visible";
    private final String SHARED_PREF_KEY = "shift_shared_pref_key";
    private SharedPreferences mSharedPreferences;

    public SimpleVisibilityClient(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY,
                Context.MODE_PRIVATE);
    }

    public SimpleVisibilityClient(Context context, boolean isVisible) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY,
                Context.MODE_PRIVATE);
        setIsVisible(isVisible);
    }

    @Override
    public boolean isVisible() {
        return mSharedPreferences != null && mSharedPreferences.getBoolean(KEY_IS_VISIBLE, false);
    }

    @Override
    public void setIsVisible(boolean isVisible) {
        mSharedPreferences.edit().putBoolean(KEY_IS_VISIBLE, isVisible)
                .apply();
    }
}
