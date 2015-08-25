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

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;

abstract class WriteToFileAsyncTask<T> extends AsyncTask<T, Void, Void> {

    public abstract void save(FileOutputStream outputStream, T... objects) throws IOException;

    @Override
    protected Void doInBackground(T... objects) {
        FileOutputStream outputStream = null;
        try {
            save(outputStream, objects);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
