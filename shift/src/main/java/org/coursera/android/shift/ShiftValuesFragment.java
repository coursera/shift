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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShiftValuesFragment extends ViewPagerFragment {

    private static final String TAB_TITLE_FEATURE = "Values";

    public ShiftValuesFragment() {
        super(TAB_TITLE_FEATURE);
    }

    public static ShiftValuesFragment getNewInstance() {
        return new ShiftValuesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shift_values_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int bottomMargin = (int) getResources().getDimension(R.dimen.card_margin);
        recyclerView.addItemDecoration(new VerticalMarginItemDecoration(bottomMargin));

        ShiftValueRecyclerViewAdapter adapter = new ShiftValueRecyclerViewAdapter(getActivity(),
                ShiftManager.getInstance().getValueRegistrationManager().getShiftValueToPref());

        List<ShiftValueSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<>();

        //Sections
        List<Pair<String,Integer>> categories =
                ShiftManager.getInstance().getValueRegistrationManager().getCategories();

        for (Pair<String,Integer> category : categories) {
            sections.add(new ShiftValueSectionedRecyclerViewAdapter.Section(category.first,
                    category.second));
        }

        //Add your adapter to the sectionAdapter
        ShiftValueSectionedRecyclerViewAdapter.Section[] dummy = new ShiftValueSectionedRecyclerViewAdapter.Section[sections.size()];
        ShiftValueSectionedRecyclerViewAdapter mSectionedAdapter = new
                ShiftValueSectionedRecyclerViewAdapter(getActivity(), adapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        recyclerView.setAdapter(mSectionedAdapter);
        return view;
    }
}
