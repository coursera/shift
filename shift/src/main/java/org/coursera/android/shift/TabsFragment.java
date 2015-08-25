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

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabsFragment extends DialogFragment {

    private ShiftMenuPagerAdapter mPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private static final String TAB_POSITION = "tab_position";

    public static TabsFragment getNewInstance() {
        return new TabsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.menu_tab, container, false);
        mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout) root.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));

        mPagerAdapter = new ShiftMenuPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);

        if (savedInstanceState != null) {
            int currentPosition = savedInstanceState.getInt(TAB_POSITION);
            mViewPager.setCurrentItem(currentPosition);
        }
        return root;
    }
}
