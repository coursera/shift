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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class ShiftActionRecyclerViewAdapter extends RecyclerView.Adapter<ShiftActionRecyclerViewAdapter.ShiftActionViewHolder> {

    private List<ShiftAction> mData;
    private final Context mContext;

    public ShiftActionRecyclerViewAdapter(Context context, List<ShiftAction> actions) {
        mContext = context;
        mData = actions;
    }

    @Override
    public ShiftActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.actions_card, parent, false);
        return new ShiftActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShiftActionViewHolder holder, int position) {
        final ShiftAction action = mData.get(position);
        holder.title.setText(action.getActionName());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.getAction().run();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ShiftActionViewHolder extends RecyclerView.ViewHolder {
        public final View container;
        public final TextView title;

        public ShiftActionViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.action_title);
            container = view.findViewById(R.id.card_view);
        }
    }
}
