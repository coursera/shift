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

import android.app.Application;
import android.widget.Toast;

import org.coursera.android.shift.ShiftAction;
import org.coursera.android.shift.ShiftManager;
import org.coursera.android.shift.SimpleVisibilityClient;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * ShiftManager Setup
         */

        /* Create a ShiftVisibilityClient so Shift knows whether or not to show itself.

         * You can either create your own by implementing ShiftVisibilityClient or
         * use SimpleVisibilityClient.You might want to write your own if you require logic
         * specific to your app.
         *
         * Eg, You need users to login to your app and have your own logic for
         * determining if an account belongs to a developer or employee.
         * You only want Shift to appear for those users so you set visibility based on that logic
         */

        // For this example we will allow all users to see Shift if we are launching in DEBUG mode,
        SimpleVisibilityClient visibilityClient = new SimpleVisibilityClient(this, BuildConfig.DEBUG);

        // We provide the LAUNCHER class as a third parameter so that Shift knows which
        // Activity to start if it needs to restart the application.
        // Your LAUNCHER class can be found in your AndroidManifest.xml
        ShiftManager.initialize(this, visibilityClient,
                ShiftExampleFragmentActivity.class);

        /**
         * Shift Actions
         */

        /* We create a new ShiftAction and register it using ShiftManager.
         * Examples of use cases for creating Actions could be clearing user preferences,
         * clearing database storage, sending specific API calls, dumping logs, etc.
        */

        ShiftManager.getInstance().registerAction(new ShiftAction("Show A Toast", new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "I love toast", Toast.LENGTH_SHORT).show();
            }
        }));

        ShiftManager.getInstance().registerAction(new ShiftAction("Clear Database", new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Database cleared", Toast.LENGTH_SHORT).show();
            }
        }));

        ShiftManager.getInstance().registerAction(new ShiftAction("Clear User Preference", new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Prefs cleared", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
