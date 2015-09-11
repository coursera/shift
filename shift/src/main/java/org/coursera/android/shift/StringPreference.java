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

class StringPreference implements ShiftPref<String> {
    private final ShiftPersistenceManager PERSISTENCE;
    private final String KEY;
    private final String DEFAULT_VALUE;

    public StringPreference(ShiftPersistenceManager persistence, String key, String defaultValue) {
        this.PERSISTENCE = persistence;
        this.KEY = key;
        this.DEFAULT_VALUE = defaultValue;
        setValueToDefault();
    }

    @Override
    public String getValue() {
        return PERSISTENCE.getString(KEY, DEFAULT_VALUE);
    }

    @Override
    public boolean isValueSet() {
        return PERSISTENCE.exists(KEY);
    }

    @Override
    public void setValue(String value) {
        PERSISTENCE.putString(KEY, value);
    }

    @Override
    public void deleteValue() {
        PERSISTENCE.removeString(KEY);
    }

    @Override
    public void setValueToDefault() {
        setValue(DEFAULT_VALUE);
    }
}
