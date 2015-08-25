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

/**
 * Implement this class in order to subscribe to changes in {@link ShiftValue}.
 * Should implement this either in your applications Application class, or in any Activity
 * that needs to update when a ShiftValue is updated
 */
public interface ShiftValueListener {

 /**
  *
  * @param shiftValue The ShiftValue that was updated
  */
    void onShiftValuesUpdated(ShiftValue shiftValue);
}
