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

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Used to register {@link ShiftValue}
 * and set/retrieve their values
 */
class ShiftValueRegistrationManagerImpl implements ShiftValueRegistrationManager {

    private Map<ShiftValue, BooleanPreference> mBooleanMap = new HashMap<ShiftValue, BooleanPreference>();
    private Map<ShiftValue, IntPreference> mIntMap = new HashMap<ShiftValue, IntPreference>();
    private Map<ShiftValue, StringPreference> mStringMap = new HashMap<ShiftValue, StringPreference>();
    private Map<ShiftValue, FloatPreference> mFloatMap = new HashMap<ShiftValue, FloatPreference>();
    private Map<ShiftValue, StringArraySelectorPreference> mStringArraySelectorMap
            = new HashMap<ShiftValue, StringArraySelectorPreference>();

    private ShiftPersistenceManager mPersistence;

    public static final String PREFIX = "ShiftValue";

    public ShiftValueRegistrationManagerImpl(ShiftPersistenceManager persistenceManager) {
        mPersistence = persistenceManager;
    }

    public void register(ShiftValue key, boolean defaultValue) {
        if (mBooleanMap.containsKey(key)) {
            throw new IllegalArgumentException("This key has already been registered before");
        }
        mBooleanMap.put(key, new BooleanPreference(mPersistence, key.toString(), defaultValue));
    }

    public void register(ShiftValue key, int defaultValue) {
        if (mIntMap.containsKey(key)) {
            throw new IllegalArgumentException("This key has already been registered before");
        }
        mIntMap.put(key,
                new IntPreference(mPersistence, key.toString(), defaultValue));
    }

    public void register(ShiftValue key, String defaultValue) {
        if (mStringMap.containsKey(key)) {
            throw new IllegalArgumentException("This key has already been registered before");
        }
        mStringMap.put(key,
                new StringPreference(mPersistence, key.toString(), defaultValue));
    }

    public void register(ShiftValue key, float defaultValue) {
        if (mFloatMap.containsKey(key)) {
            throw new IllegalArgumentException("This key has already been registered before");
        }
        mFloatMap.put(key,
                new FloatPreference(mPersistence, key.toString(), defaultValue));
    }

    @Override
    public void register(ShiftValue key, StringArraySelector defaultValue) {
        if (mStringArraySelectorMap.containsKey(key)) {
            throw new IllegalArgumentException("This key has already been registered before");
        }
        mStringArraySelectorMap.put(key, new StringArraySelectorPreference(mPersistence,
                key.toString(),
                defaultValue));
    }

    public boolean getBool(ShiftValue key) {
        BooleanPreference pref = mBooleanMap.get(key);
        if (pref == null) {
            throw new IllegalArgumentException("There is no boolean value for this ShiftValue: " + key.toString());
        }
        return mBooleanMap.get(key).getValue();
    }

    public int getInt(ShiftValue key) {
        IntPreference pref = mIntMap.get(key);
        if (pref == null) {
            throw new IllegalArgumentException("There is no int value for this ShiftValue: " + key.toString());
        }
        return mIntMap.get(key).getValue();
    }

    public String getString(ShiftValue key) {
        StringPreference pref = mStringMap.get(key);
        if (pref == null) {
            throw new IllegalArgumentException("There is no String value for this ShiftValue: " + key.toString());
        }
        return mStringMap.get(key).getValue();
    }

    public float getFloat(ShiftValue key) {
        FloatPreference pref = mFloatMap.get(key);
        if (pref == null) {
            throw new IllegalArgumentException("There is no float value for this ShiftValue: " + key.toString());
        }
        return mFloatMap.get(key).getValue();
    }

    public StringArraySelectorPreference getStringArraySelectorPreference(ShiftValue key) {
        StringArraySelectorPreference pref = mStringArraySelectorMap.get(key);
        if (pref == null) {
            throw new IllegalArgumentException("There is no StringArraySelector value " +
                    "for this ShiftValue: " + key.toString());
        }
        return pref;
    }

    public String getStringArraySelectorSelectedValue(ShiftValue key) {
        StringArraySelectorPreference pref = mStringArraySelectorMap.get(key);
        if (pref == null) {
            throw new IllegalArgumentException("There is no StringArraySelector value " +
                    "for this ShiftValue: " + key.toString());
        }
        return mStringArraySelectorMap.get(key).getValue().getSelectedValue();
    }

    public String[] getStringArraySelectorValues(ShiftValue key) {
        StringArraySelectorPreference pref = mStringArraySelectorMap.get(key);
        if (pref == null) {
            throw new IllegalArgumentException("There is no StringArraySelector value " +
                    "for this ShiftValue: " + key.toString());
        }
        return mStringArraySelectorMap.get(key).getValue().getArray();
    }

    Map<ShiftValue, ShiftPref> getShiftValueToPref() {
        /*
            Invalidate Database at this point. This point in execution guarantees all ShiftValues
            will be registered in ShiftValueRegistrationManagerImpl. This will only be called
            once per application launch.
          */
        invalidatePersistedStorageWithCache();
        Map<ShiftValue, ShiftPref> map = new HashMap<ShiftValue, ShiftPref>();
        map.putAll(mBooleanMap);
        map.putAll(mIntMap);
        map.putAll(mStringMap);
        map.putAll(mFloatMap);
        map.putAll(mStringArraySelectorMap);
        return map;
    }

    List<ShiftValue> getShiftValues() {
        List<ShiftValue> features = new LinkedList<ShiftValue>();
        features.addAll(mBooleanMap.keySet());
        features.addAll(mIntMap.keySet());
        features.addAll(mStringMap.keySet());
        features.addAll(mFloatMap.keySet());
        return features;
    }

    List<Pair<String, Integer>> getCategories() {
        List<Pair<String, Integer>> categories = new ArrayList<>();
        Map<ShiftValue, ShiftPref> shiftValueToPref = getShiftValueToPref();
        List<ShiftValue> keys = new ArrayList<ShiftValue>(shiftValueToPref.keySet());
        Collections.sort(keys);
        String prevCategory = null;
        int prevIndex = 0;
        for (int i = 0; i < keys.size(); i++) {
            ShiftValue value = keys.get(i);
            if (prevCategory == null) {
                prevCategory = value.CATEGORY;
                prevIndex = i;
            } else {
                if (!prevCategory.equals(value.CATEGORY)) {
                    categories.add(new Pair<String, Integer>(prevCategory, prevIndex));
                    prevCategory = value.CATEGORY;
                    prevIndex = i;
                }
            }
        }
        // Add last category
        categories.add(new Pair<String, Integer>(prevCategory, prevIndex));
        return categories;
    }

    Set<String> getAllShiftValuesAsStrings() {
        Set<String> keys = new HashSet<>();
        Set<ShiftValue> shiftValues = new HashSet<>();
        shiftValues.addAll(mBooleanMap.keySet());
        shiftValues.addAll(mIntMap.keySet());
        shiftValues.addAll(mStringMap.keySet());
        shiftValues.addAll(mFloatMap.keySet());
        for (ShiftValue value : shiftValues) {
            keys.add(value.toString());
        }
        return keys;
    }

    /**
     * Handles the case where client removes some ShiftValues from their code and are no longer
     * using them but they are still stored in database. If this happens we should remove
     * all ShiftValues that are no longer needed.
     */
    void invalidatePersistedStorageWithCache() {
        ShiftPersistenceManager persistenceManager =
                ShiftManager.getInstance().getPersistenceManager();
        if (persistenceManager.shouldInvalidate()) {
            persistenceManager.invalidateDatabase(getAllShiftValuesAsStrings(), PREFIX);
        }
    }
}
