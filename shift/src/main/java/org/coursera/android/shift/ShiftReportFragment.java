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

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class ShiftReportFragment extends ViewPagerFragment {

    private static final String TAB_TITLE_REPORT = "Report";

    private ImageView mScreenShot;

    public ShiftReportFragment() {
        super(TAB_TITLE_REPORT);
    }

    public static ShiftReportFragment getNewInstance() {
        return new ShiftReportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.report, container, false);
        mScreenShot = (ImageView) layout.findViewById(R.id.report_screenshot);

        new ScreenShotUtils.LoadScreenShotAsyncTask(mScreenShot).execute();
        final EditText device = (EditText) layout.findViewById(R.id.report_device_info);
        device.setText(Build.MANUFACTURER + " " + Build.MODEL + " SDK:" + Build.VERSION.SDK_INT);

        final EditText summary = (EditText) layout.findViewById(R.id.report_summary);

        final EditText email = (EditText) layout.findViewById(R.id.report_email);

        final EditText reproductionSteps = (EditText) layout.findViewById(R.id.report_repro_steps);

        Button send = (Button) layout.findViewById(R.id.report_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] receipients = new String[]{email.getText().toString()};
                String deviceInfo = "Bug Report on " + device.getText();
                String bugSummary = "Summary: \n" + summary.getText() + "\nReproduction Steps:\n"
                        + reproductionSteps.getText();
                File[] attachments = new File[]{new File(ScreenShotUtils.getScreenShotFilePath())};

                EmailUtils.sendEmail(
                        ShiftReportFragment.this.getActivity(),
                        receipients,
                        deviceInfo,
                        bugSummary,
                        attachments);
            }
        });
        return layout;
    }
}
