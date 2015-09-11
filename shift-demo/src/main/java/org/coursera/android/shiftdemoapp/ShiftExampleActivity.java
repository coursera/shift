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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.coursera.android.shift.ShiftActionBarActivity;
import org.coursera.android.shift.ShiftManager;
import org.coursera.android.shift.ShiftValue;
import org.coursera.android.shift.ShiftValueListener;

/**
 * Here is an example of extending ShiftActionBarActivity for convenience so that the creation
 * of a ShiftLauncherView and the showing of the floating icon is handled for us.
 * We also show an example of showing the ShiftMenu using our own button
 * and subscribing to changes in specific ShiftValues
 *
 */
public class ShiftExampleActivity extends ShiftActionBarActivity implements ShiftValueListener {

    private Button shiftMenuButton;
    private TextView endpoint, token, helloWorld, logLevel;
    private ShiftValue[] features = {ShiftValues.END_POINT, ShiftValues.MEANING_OF_LIFE, ShiftValues.WELCOME_MESSAGE, ShiftValues.LOG_LEVEL_SELECTOR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mainLayout = (LinearLayout)
                inflater.inflate(R.layout.activity_secondary, null);
        setContentView(mainLayout);
        endpoint = (TextView) findViewById(R.id.endpoint);
        token = (TextView) findViewById(R.id.token);
        helloWorld = (TextView) findViewById(R.id.hello_world);
        shiftMenuButton = (Button) findViewById(R.id.shift_menu);
        logLevel = (TextView) findViewById(R.id.log_level);

        // We set up another button that allows us to launch the ShiftMenu ourselves without
        // tapping the floating icon
        shiftMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShiftLauncherView.showShiftMenu(ShiftExampleActivity.this);
            }
        });
        populateTextViews();
    }

    private void populateTextViews() {
        endpoint.setText(ShiftValues.END_POINT.getStringValue());
        token.setText(ShiftValues.MEANING_OF_LIFE.getStringValue());
        helloWorld.setText(ShiftValues.WELCOME_MESSAGE.getStringValue());
        logLevel.setText(ShiftValues.LOG_LEVEL_SELECTOR.getSelectedValue());
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * This time we will only subscribe to specific ShiftValues
         */
        subscribeToShiftValues();
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * We must unsubscribe to these updates when the Activity is no longer being presented
         * to avoid changing values that no longer exist
         */
        unsubscribeToShiftValues();
    }

    private void subscribeToShiftValues() {
        ShiftManager.getInstance().subscribeToUpdatesForShiftValues(this, features);
    }

    private void unsubscribeToShiftValues() {
        ShiftManager.getInstance().unsubscribeToUpdatesForShiftValues(this, features);
    }

    @Override
    public void onShiftValuesUpdated(ShiftValue shiftValue) {
        // We can listen for specific changes to ShiftValues using the method parameter
        if (shiftValue == ShiftValues.END_POINT) {
            endpoint.setText(ShiftValues.END_POINT.getStringValue());
        }
        if (shiftValue == ShiftValues.MEANING_OF_LIFE) {
            token.setText(ShiftValues.MEANING_OF_LIFE.getStringValue());
        }
        if (shiftValue == ShiftValues.WELCOME_MESSAGE) {
            helloWorld.setText(ShiftValues.WELCOME_MESSAGE.getStringValue());
        }
        if (shiftValue == ShiftValues.LOG_LEVEL_SELECTOR) {
            logLevel.setText(ShiftValues.LOG_LEVEL_SELECTOR.getSelectedValue());
        }
    }
}
