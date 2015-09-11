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

class ArraySelector<T> {
    final T[] ARRAY;
    int selectedIndex;

    public ArraySelector(T[] array, int index) {
        ARRAY = array;
        selectedIndex = index;
    }

    public T getSelectedValue() {
        return ARRAY[selectedIndex];
    }

    public void setSelectedIndex(int index) {
        if(index > ARRAY.length - 1) {
            throw new IllegalArgumentException("Given index exceeds array length");
        } else {
            selectedIndex = index;
        }
    }

    public T[] getArray() {
        return ARRAY;
    }
}
