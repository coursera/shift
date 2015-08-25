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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class ScreenShotUtils {

    private static final String SCREEN_SHOT_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator
            + "bugreportscreenshot.jpg";

    public static String getScreenShotFilePath(){
        return SCREEN_SHOT_PATH;
    }

    public static void saveImage(FragmentActivity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        new SaveScreenShotAsyncTask().execute(bitmap);
    }

    public static class SaveScreenShotAsyncTask extends WriteToFileAsyncTask<Bitmap> {

        private String mPath;

        public SaveScreenShotAsyncTask() {
            this.mPath = getScreenShotFilePath();
        }

        @Override
        public void save(FileOutputStream outputStream, Bitmap... bitmaps) throws IOException {
            Bitmap bitmap = bitmaps[0];
            outputStream = new FileOutputStream(new File(mPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
        }
    }

    public static class LoadScreenShotAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        public ImageView imageView;

        public LoadScreenShotAsyncTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeFile(getScreenShotFilePath(), options);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Log.e(LoadScreenShotAsyncTask.class.getCanonicalName(), "Image given was null");
            }
        }
    }

}
