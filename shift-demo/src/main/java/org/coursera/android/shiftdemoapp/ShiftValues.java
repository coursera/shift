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

package org.coursera.android.shiftdemoapp;

import org.coursera.android.shift.ShiftBoolean;
import org.coursera.android.shift.ShiftFloat;
import org.coursera.android.shift.ShiftInteger;
import org.coursera.android.shift.ShiftString;
import org.coursera.android.shift.ShiftStringArraySelector;

/**
 * Class to hold all of our ShiftValues
 */
public class ShiftValues {

    private static final String CATEGORY_FEATURES = "Features";
    private static final String CATEGORY_STRINGS = "Strings";
    private static final String CATEGORY_NUMS = "Nums";

    // You can register any of the ShiftValueEntries with a value of type:
    // String, int, float, boolean
    public static final ShiftString WELCOME_MESSAGE = new ShiftString(CATEGORY_STRINGS, "String Example", "Michelle", false, "HELLO WORLD");
    public static final ShiftString END_POINT = new ShiftString(CATEGORY_STRINGS, "Endpoint", "Rajat", false, "coursera.org");
    public static final ShiftString MEANING_OF_LIFE = new ShiftString(CATEGORY_STRINGS, "Meaning of Life", "Brice", false, "42");

    public static final ShiftInteger POWER_LEVEL = new ShiftInteger(CATEGORY_NUMS, "Int Example", "Stanley", false, 9001);
    public static final ShiftFloat SPEED = new ShiftFloat(CATEGORY_NUMS, "Float Example", "Brandon", false, 0.5f);

    // ShiftValues with the same Category String will be grouped under the same section in the ShiftMenu

    public static final ShiftBoolean IS_B_ENABLED = new ShiftBoolean(CATEGORY_FEATURES, "Enable/Disable b", "Yixin", false, true);
    public static final ShiftBoolean IS_C_ENABLED = new ShiftBoolean(CATEGORY_FEATURES, "Enable/Disable c", "Jingyu", false, true);
    public static final ShiftBoolean IS_D_ENABLED = new ShiftBoolean(CATEGORY_FEATURES, "Enable/Disable d", "Mike", false, true);
    public static final ShiftBoolean IS_E_ENABLED = new ShiftBoolean(CATEGORY_FEATURES, "Enable/Disable e", "Sanyam", false, true);

    // We want this particular ShiftValue to restart the application when it's value is changed
    // so we set the 4th parameter boolean to be true
    public static final ShiftBoolean IS_A_ENABLED = new ShiftBoolean(CATEGORY_FEATURES, "Enable/Disable a and Restart app", "Paul", true, true);

    //Selectors allow you to provide an Array of values and a preferred index for the value you want
    //We set the default index here to 0 so Verbose will be selected as default
    public static final ShiftStringArraySelector LOG_LEVEL_SELECTOR =
            new ShiftStringArraySelector(CATEGORY_FEATURES, "Log Level", "Stanley",
                    new String[]{"Verbose", "Debug", "Info", "Warn", "Error", "Assert"}, 0, false);
}
