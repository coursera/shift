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

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.coursera.android.shift.ShiftManager;
import org.coursera.android.shift.ShiftValue;
import org.coursera.android.shift.ShiftValueListener;

/**
 * Example of how to subscribe to updates to ALL ShiftValues.
 * We implement ShiftValueListener and add our refresh logic in
 * {@link #onShiftValuesUpdated(ShiftValue)}
 */
public class ShiftExampleFragment extends Fragment implements ShiftValueListener {

    private Button buttonA, buttonB, buttonC, buttonD, buttonE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_layout, null);
        buttonA = (Button) rootView.findViewById(R.id.a);
        buttonB = (Button) rootView.findViewById(R.id.b);
        buttonC = (Button) rootView.findViewById(R.id.c);
        buttonD = (Button) rootView.findViewById(R.id.d);
        buttonE = (Button) rootView.findViewById(R.id.e);

        Button start = (Button) rootView.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShiftExampleActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * We will refresh this Fragment whenever ANY of our registered
         ShiftValueEntries are changed in {@link ShiftValues}.
         To do this we will use subscribeToAllEntries()
         */
        ShiftManager.getInstance().subscribeToUpdatesForAllShiftValues(this);
        refreshButtonVisibility();
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * We must unsubscribe to these updates when the Fragment is no longer being presented
         * to avoid changing values that no longer exist
         */
        ShiftManager.getInstance().unsubscribeToUpdatesForAllShiftValues(this);
    }

    public void refreshButtonVisibility() {
        if (ShiftValues.IS_A_ENABLED.getBooleanValue()) {
            buttonA.setVisibility(View.VISIBLE);
        } else {
            buttonA.setVisibility(View.GONE);
        }
        if (ShiftValues.IS_B_ENABLED.getBooleanValue()) {
            buttonB.setVisibility(View.VISIBLE);
        } else {
            buttonB.setVisibility(View.GONE);
        }
        if (ShiftValues.IS_C_ENABLED.getBooleanValue()) {
            buttonC.setVisibility(View.VISIBLE);
        } else {
            buttonC.setVisibility(View.GONE);
        }
        if (ShiftValues.IS_D_ENABLED.getBooleanValue()) {
            buttonD.setVisibility(View.VISIBLE);
        } else {
            buttonD.setVisibility(View.GONE);
        }
        if (ShiftValues.IS_E_ENABLED.getBooleanValue()) {
            buttonE.setVisibility(View.VISIBLE);
        } else {
            buttonE.setVisibility(View.GONE);
        }
    }

    /** ShiftListenerInterface */

    /**
     * We want to refresh all of our button's visibility when any of their ShiftValueEntries
     * have changed
     */
    @Override
    public void onShiftValuesUpdated(ShiftValue shiftValue) {
        // We do not care which ShiftValue was changed in this case
        // So we can ignore the method parameter value
        refreshButtonVisibility();
    }
}
