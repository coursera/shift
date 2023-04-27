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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ShiftValueRecyclerViewAdapter extends RecyclerView.Adapter<ShiftValueRecyclerViewAdapter.ShiftValueViewHolder> {

    public static final int TYPE_BOOLEAN = 0;
    public static final int TYPE_STRING = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_STRING_SELECTOR = 4;

    private final Context mContext;
    private Map<ShiftValue, ShiftPref> mShiftValueToPref;
    private List<ShiftValue> mData;
    private Toast numberOverflowErrorToast;

    public void add(ShiftValue s, int position) {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class ShiftValueViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final SwitchCompat onOrOff;
        public final EditText editString;
        public final EditText editInt;
        public final EditText editFloat;
        public final Spinner spinnerStringSelector;
        public TextWatcher textWatcher;

        public ShiftValueViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.feature_title);
            onOrOff = (SwitchCompat) view.findViewById(R.id.feature_boolean);
            editString = (EditText) view.findViewById(R.id.feature_string);
            editInt = (EditText) view.findViewById(R.id.feature_int);
            editFloat = (EditText) view.findViewById(R.id.feature_float);
            spinnerStringSelector = (Spinner) view.findViewById(R.id.feature_string_selector);
        }
    }

    public ShiftValueRecyclerViewAdapter(Context context, Map<ShiftValue, ShiftPref> shiftValueToPref) {
        mContext = context;
        if (shiftValueToPref != null) {
            mShiftValueToPref = shiftValueToPref;
            List<ShiftValue> keys = new ArrayList<ShiftValue>(shiftValueToPref.keySet());
            Collections.sort(keys);
            mData = keys;
        } else {
            mData = new ArrayList<ShiftValue>();
        }
    }

    @Override
    public int getItemViewType(int position) {
        ShiftValue key = mData.get(position);
        ShiftPref type = mShiftValueToPref.get(key);
        if (type instanceof BooleanPreference) {
            return TYPE_BOOLEAN;
        } else if (type instanceof StringPreference) {
            return TYPE_STRING;
        } else if (type instanceof IntPreference) {
            return TYPE_INT;
        } else if (type instanceof StringListSelectorPreference) {
            return TYPE_STRING_SELECTOR;
        } else {
            return TYPE_FLOAT;
        }
    }

    public ShiftValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case (TYPE_BOOLEAN):
                view = LayoutInflater.from(mContext).inflate(R.layout.boolean_feature, parent, false);
                break;
            case (TYPE_STRING):
                view = LayoutInflater.from(mContext).inflate(R.layout.string_feature, parent, false);
                break;
            case (TYPE_INT):
                view = LayoutInflater.from(mContext).inflate(R.layout.int_feature, parent, false);
                break;
            case (TYPE_FLOAT):
                view = LayoutInflater.from(mContext).inflate(R.layout.float_feature, parent, false);
                break;
            case (TYPE_STRING_SELECTOR):
                view = LayoutInflater.from(mContext).inflate(R.layout.string_selector, parent, false);
                break;
            default:
                view = new View(mContext);
        }
        return new ShiftValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShiftValueViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        final ShiftValue key = mData.get(position);
        holder.title.setText(key.FEATURE);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Author: " + key.AUTHOR, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        switch (viewType) {
            case TYPE_BOOLEAN:
                final BooleanPreference boolPref = (BooleanPreference) mShiftValueToPref.get(key);
                holder.onOrOff.setOnCheckedChangeListener(null);
                holder.onOrOff.setChecked(boolPref.getValue());
                holder.onOrOff.setOnCheckedChangeListener(
                        new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                boolPref.setValue(!boolPref.getValue());
                                ShiftManager.getInstance().notifyShiftListeners(key);
                            }
                        });
                break;
            case TYPE_STRING:
                final StringPreference stringPref = (StringPreference) mShiftValueToPref.get(key);
                holder.editString.removeTextChangedListener(holder.textWatcher);
                holder.editString.setText(stringPref.getValue());
                holder.textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        stringPref.setValue(editable.toString());
                        ShiftManager.getInstance().notifyShiftListeners(key);
                    }
                };
                holder.editString.addTextChangedListener(holder.textWatcher);
                break;
            case TYPE_INT:
                final IntPreference intPref = (IntPreference) mShiftValueToPref.get(key);
                holder.editInt.removeTextChangedListener(holder.textWatcher);
                holder.editInt.setText(intPref.getValue() + "");
                holder.textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().isEmpty()) {
                            Integer value;
                            try {
                                value = Integer.valueOf(editable.toString());
                                intPref.setValue(value);
                                ShiftManager.getInstance().notifyShiftListeners(key);
                            } catch (NumberFormatException error) {
                                showNumberOverFlowErrorToast();
                            }
                        }
                    }
                };
                holder.editInt.addTextChangedListener(holder.textWatcher);
                break;
            case TYPE_FLOAT:
                final FloatPreference floatPref = (FloatPreference) mShiftValueToPref.get(key);
                holder.editFloat.removeTextChangedListener(holder.textWatcher);
                holder.editFloat.setText(floatPref.getValue() + "");
                holder.textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().isEmpty()) {
                            try {
                                Float value = Float.valueOf(editable.toString());
                                floatPref.setValue(value);
                                ShiftManager.getInstance().notifyShiftListeners(key);
                            } catch (NumberFormatException error) {
                                showNumberOverFlowErrorToast();
                            }
                        }
                    }
                };
                holder.editFloat.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                holder.editFloat.addTextChangedListener(holder.textWatcher);
                break;
            case TYPE_STRING_SELECTOR:
                final StringListSelectorPreference stringSelectorPref = (StringListSelectorPreference) mShiftValueToPref.get(key);
                List<String> values = stringSelectorPref.getValue().getList();
                int selectedIndex = stringSelectorPref.getValue().selectedIndex;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,values);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerStringSelector.setAdapter(adapter);
                holder.spinnerStringSelector.setSelection(selectedIndex);
                holder.spinnerStringSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            stringSelectorPref.setSelectedIndex(position);
                            ShiftManager.getInstance().notifyShiftListeners(key);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //Do nothing
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void showNumberOverFlowErrorToast() {
        if (numberOverflowErrorToast == null || !numberOverflowErrorToast.getView().isShown()) {
            numberOverflowErrorToast = Toast.makeText(mContext, "Number Overflow!", Toast.LENGTH_SHORT);
            numberOverflowErrorToast.show();
        }
    }
}
