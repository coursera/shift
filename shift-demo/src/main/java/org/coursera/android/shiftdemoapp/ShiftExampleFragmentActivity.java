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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.coursera.android.shift.ShiftManager;

/**
 * Example of instantiating a ShiftLauncherView in a FragmentActivity and then
 * showing your own Fragment. The ShiftFloatingIcon will always appear above all of your
 * fragments in the activity so there is no need to worry about managing the order of your
 * fragment transactions.
 */
public class ShiftExampleFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a ShiftLauncherView and show the floating icon
        ShiftManager.getInstance().createShiftViewAndShowFloatingIcon(this);

        // We can add a fragment after showing the floating icon and it will
        // still be underneath the floating icon
        getFragmentManager().beginTransaction()
                .add(R.id.container, new ShiftExampleFragment()).commit();

    }
}
