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

public class ShiftString extends ShiftValue {

    /**
     * @param category                         ShiftValueEntries with the same category will be grouped into the same section
     * @param feature                          Name of the variable or feature
     * @param author                           Name of the person who is responsible for this
     * @param shouldRestartApplicationOnChange If true, the clients application will restart when
     *                                         this ShiftValue is modified.
     * @param defaultValue
     */
    public ShiftString(String category, String feature, String author, boolean shouldRestartApplicationOnChange, String defaultValue) {
        super(category,feature, author, shouldRestartApplicationOnChange);
        ShiftManager.getInstance().getValueRegistrationManager().register(this, defaultValue);
    }

    public String getStringValue() {
        return ShiftManager.getInstance().getValueRegistrationManager().getString(this);
    }
}
