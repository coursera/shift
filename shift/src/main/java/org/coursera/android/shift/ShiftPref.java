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

abstract class ShiftPref <T> {
    protected final ShiftPersistenceManager PERSISTENCE;
    protected final String KEY;
    protected final T DEFAULT_VALUE;
    protected final Class<T> CLASS;

    public ShiftPref(ShiftPersistenceManager persistenceManager,
                     String key,
                     T defaultValue,
                     Class<T> myClass) {
        PERSISTENCE = persistenceManager;
        KEY = key;
        DEFAULT_VALUE = defaultValue;
        CLASS = myClass;
        setValueToDefault();
    }

    abstract T getValue();

    abstract void setValue(T value);

    boolean isValueSet() {
        return PERSISTENCE.exists(KEY);
    }

    void deleteValue() {
        PERSISTENCE.remove(KEY, CLASS.getName());
    }

    void setValueToDefault() {
        setValue(DEFAULT_VALUE);
    }
}
