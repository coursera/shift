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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class StringListSelector {
    ArrayList<String> list;
    int selectedIndex;

    public StringListSelector() {
        list = new ArrayList<>();
        selectedIndex = 0;
    }

    public StringListSelector(ArrayList<String> list, int index) {
        this.list = list;
        selectedIndex = index;
    }

    public StringListSelector(String[] array, int index) {
        this.list = new ArrayList<>();
        this.list.addAll(Arrays.asList(array));
        selectedIndex = index;
    }

    public String getSelectedValue() {
        return list.get(selectedIndex);
    }

    public void setSelectedIndex(int index) {
        if(index > list.size() - 1) {
            throw new IllegalArgumentException("Given index exceeds array length");
        } else {
            selectedIndex = index;
        }
    }

    public List<String> getList() {
        return list;
    }
}
