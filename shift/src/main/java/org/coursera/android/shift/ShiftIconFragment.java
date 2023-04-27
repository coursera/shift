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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

public class ShiftIconFragment extends Fragment {

    private View mHead;
    private ShiftLauncherView mShiftLauncherView;

    public ShiftIconFragment() {
        mShiftLauncherView = new ShiftLauncherView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.shift_menu,
                container, false);

        mHead = layout.findViewById(R.id.head);
        final RelativeLayout.LayoutParams iconParams;
        iconParams = (RelativeLayout.LayoutParams) mHead.getLayoutParams();

        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());

        mHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int yLimit = layout.getMeasuredHeight() - mHead.getMeasuredHeight();
                final int xLimit = layout.getMeasuredWidth() - mHead.getMeasuredWidth();

                // single tap
                if (gestureDetector.onTouchEvent(event)) {
                    mShiftLauncherView.showShiftMenu(getActivity());
                    return true;
                }

                // drag icon
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = iconParams.leftMargin;
                        initialY = iconParams.topMargin;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        return false;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int newX = initialX + (int) (event.getRawX() - initialTouchX);
                        int newY = initialY + (int) (event.getRawY() - initialTouchY);

                        iconParams.leftMargin = newX < 0 ? 0 : Math.min(newX, xLimit);
                        iconParams.topMargin = newY < 0 ? 0 : Math.min(newY, yLimit);

                        layout.updateViewLayout(mHead, iconParams);
                        return true;
                }
                return false;
            }
        });
        return layout;
    }

    public void hide() {
        mHead.setVisibility(View.GONE);
    }

    public void show() {
        mHead.setVisibility(View.VISIBLE);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}
