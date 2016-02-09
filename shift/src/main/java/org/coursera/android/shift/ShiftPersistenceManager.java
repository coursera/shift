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
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("SynchronizeOnNonFinalField")
class ShiftPersistenceManager {

    private static final String TAG = ShiftPersistenceManager.class.getCanonicalName();

    private static final String SHARED_PREF_KEY = "org.coursera.android.shift.SHIFT_PREFS_FILE";

    // Invalidate once per launch
    private boolean shouldInvalidate = true;

    private SharedPreferences sharedPreferences;

    public ShiftPersistenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
    }

    /**
     * * INVALIDATE DB HELPERS:
     * If the client decides to remove ShiftValues from their code, we will have values in the
     * database that are no longer needed.
     * We should remove the extra keys/values that are not stored in the
     * {@link ShiftValueSubscriptionManagerImpl}
     *
     * @param keysToKeep
     */
    public void invalidateDatabase(Set<String> keysToKeep) {
        if (keysToKeep == null) {
            Log.e(TAG, "Set keysToKeep are null, failed to invalidateDB");
            return;
        }
        if (shouldInvalidate) {
            Map<String, ?> sharedPreferenceKeys = sharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : sharedPreferenceKeys.entrySet()) {
                if (!keysToKeep.contains(entry.getKey()))
                    sharedPreferences.edit().remove(entry.getKey());
            }
            sharedPreferences.edit().apply();
            shouldInvalidate = false;
        }
    }

    public boolean shouldInvalidate() {
        return shouldInvalidate;
    }

    /*
        INT
     */
    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean exists(String key) {
        return sharedPreferences.contains(key);
    }

    /*
        STRING
     */
    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /*
        BOOLEAN
     */

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /*
        FLOAT
     */
    public void putFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void remove(String key, String type) {
        sharedPreferences.edit().remove(key).apply();
    }

    // Objects

    /**
     * Only put composites of primitive objects that GSON can successfully convert to JSON
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void putObject(String key, T value) {
        Gson gson = new Gson();
        sharedPreferences.edit().putString(key, gson.toJson(value)).apply();
    }

    public <T> T getObject(String key, Class<T> objectClass, T defaultValue) {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(key, defaultValue.toString()), objectClass);
    }
}
