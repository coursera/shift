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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShiftActionsFragment extends ViewPagerFragment {

    private static final String TAB_TITLE_ACTIONS = "Actions";

    public ShiftActionsFragment() {
        super(TAB_TITLE_ACTIONS);
    }

    public static ShiftActionsFragment getNewInstance() {
        return new ShiftActionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actions_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int bottomMargin = (int) getResources().getDimension(R.dimen.card_margin);
        recyclerView.addItemDecoration(new VerticalMarginItemDecoration(bottomMargin));
        ShiftActionRecyclerViewAdapter adapter =
                new ShiftActionRecyclerViewAdapter(getActivity(),
                        ShiftManager.getInstance().getActionManager().getActionList());
        recyclerView.setAdapter(adapter);
        return view;
    }
}
