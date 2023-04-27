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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Responsible for displaying the floating icon and ShiftMenu that allows
 * a user to modify ShiftValues and use other parts of Shift
 */
public class ShiftLauncherView {

    private final String FLOATING_ICON_TAG = "SM_TAG";
    private final String TABS_TAG = "TABS_TAG";
    private final String TABS_BACK_STACK = "TABS_BACK_STACK";

    /**
     * We hide the constructor for ShiftLauncherView to ensure the user has called ShiftManager.initialize()
     * before being able to get a ShiftLauncherView
     */
    ShiftLauncherView() {
    }

    /**
     * Display floating icon
     * Clicking on this icon will display the Shift Menu.
     * If you do not want to use the floating icon and link up your own button,
     * use {@link #showShiftMenu(FragmentActivity)}
     */
    public void showFloatingIcon(FragmentActivity activity) {
        if (ShiftManager.getInstance().getVisibilityClient().isVisible()) {
            addFragment(activity, new ShiftIconFragment(), FLOATING_ICON_TAG, true);
        }
    }

    /**
     * Displays the Shift Menu
     * User can call this if they want to show the Shift Menu without
     * using {@link #showFloatingIcon(FragmentActivity)}.
     */
    public void showShiftMenu(FragmentActivity activity) {
        if (ShiftManager.getInstance().getVisibilityClient().isVisible()) {
            FragmentManager manager = activity.getSupportFragmentManager();
            Fragment tabsFragment = manager.findFragmentByTag(TABS_TAG);
            //Don't allow user to open another menu if menu is currently open
            if (tabsFragment != null && tabsFragment.isVisible()) {
                return;
            }
            showTabsFragment(activity);
        }
    }

    private void addFragment(FragmentActivity activity, Fragment fragment, String tag, boolean isVisible) {
        FragmentManager manager = activity.getSupportFragmentManager();
        Fragment oldInstance = manager.findFragmentByTag(tag);
        if (oldInstance == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(android.R.id.content, fragment, tag);
            if (!isVisible) {
                transaction.hide(fragment);
            } else if (tag.equals(TABS_TAG)) {
                manager.popBackStack(TABS_BACK_STACK, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.addToBackStack(TABS_BACK_STACK);
            }
            transaction.commit();
        }
    }

    private void showTabsFragment(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment tabsFragment = manager.findFragmentByTag(TABS_TAG);
        if (tabsFragment == null || !tabsFragment.isAdded()) {
            TabsFragment tabs = TabsFragment.getNewInstance();
            addFragment(activity, tabs, TABS_TAG, true);
        } else {
            transaction.remove(manager.findFragmentByTag(TABS_TAG));
            transaction.commit();
            TabsFragment tabs = TabsFragment.getNewInstance();
            addFragment(activity, tabs, TABS_TAG, true);
        }
    }

    private ShiftIconFragment getIconFragment(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        return (ShiftIconFragment) manager.findFragmentByTag(FLOATING_ICON_TAG);
    }

    private void captureScreen(FragmentActivity activity) {
        hideFragments(activity);
        ScreenShotUtils.saveImage(activity);
        showFragments(activity);
    }

    private void hideFragments(FragmentActivity activity) {
        ShiftIconFragment shiftIconFragment = getIconFragment(activity);
        if (shiftIconFragment != null) {
            shiftIconFragment.hide();
        }
    }

    private void showFragments(FragmentActivity activity) {
        ShiftIconFragment shiftIconFragment = getIconFragment(activity);
        if (shiftIconFragment != null) {
            shiftIconFragment.show();
        }
    }
}
