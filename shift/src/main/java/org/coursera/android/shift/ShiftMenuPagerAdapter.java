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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ShiftMenuPagerAdapter extends FragmentPagerAdapter {

    private List<ViewPagerFragment> mFragments = new ArrayList<>();
    private List<String> mPageTitles = new ArrayList<>();

    public ShiftMenuPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments.add(ShiftValuesFragment.getNewInstance());
        mFragments.add(ShiftActionsFragment.getNewInstance());
        mFragments.add(ShiftReportFragment.getNewInstance());
        for (ViewPagerFragment viewPagerFragment : mFragments) {
            mPageTitles.add(viewPagerFragment.TAB_TITLE);
        }
    }

    @Override
    public int getCount() {
        if (mFragments == null) {
            return 0;
        }
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= mFragments.size()) {
            throw new IndexOutOfBoundsException("Position is out of bound: " + position);
        } else {
            return mFragments.get(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= mPageTitles.size()) {
            throw new IndexOutOfBoundsException("Position is out of bound: " + position);
        } else {
            return mPageTitles.get(position);
        }
    }
}
