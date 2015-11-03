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
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.Set;

/**
 * Simple Persistence storage using https://github.com/nhachicha/SnappyDB.
 * Chose to use SnappyDB due to its simple NoSQL approach.
 * Allows us to store primitives easily like SharedPreferences, but also gives
 * flexibility to store more complicated objects in the future (Eg. Arrays with a selected index).
 */

@SuppressWarnings("SynchronizeOnNonFinalField")
class ShiftPersistenceManager {

    private static final String TAG = ShiftPersistenceManager.class.getCanonicalName();

    private static String TABLE_NAME = "shift.persistence.";

    // Invalidate once per launch
    private boolean shouldInvalidate = true;

    private DB db;

    /*
        SnappydbException is thrown if we are unable to create the file (out of memory)
        or the existing DB file somehow becomes inaccessible:
        https://github.com/nhachicha/SnappyDB/blob/4e0c0805bf5c9c157042f61d9c11842c784cf91c/library/src/main/java/com/snappydb/SnappyDB.java
        On out of memory client's app wouldn't be able to run either and the existing DB file
        should never have it's permissions changed by another application unless the client chooses to change it themselves.

        If for some reason db is null, all GET calls will still be safe because of the ability to
        return the default value. Setting the values will not do anything.
     */
    public ShiftPersistenceManager(Context context) {
        TABLE_NAME = TABLE_NAME + context.getPackageName();
        try {
            db = new SnappyDB.Builder(context)
                    .name(TABLE_NAME)
                    .build();
        } catch (SnappydbException e) {
            Log.e(TAG, "Failed to create SnappyDB! This should never happen unless out of memory. " + e.toString());
        }
    }

    /**
     * * INVALIDATE DB HELPERS:
     * If the client decides to remove ShiftValues from their code, we will have values in the
     * database that are no longer needed.
     * We should remove the extra keys/values that are not stored in the
     * {@link ShiftValueSubscriptionManagerImpl}
     *
     * @param keys
     * @param prefix SnappyDB can only count with a prefix so we prepend this in
     *               {@link ShiftValue#toString()}. Would prefer if there was simple a db.countAllKeys() but
     *               aside from that SnappyDB seems to fit our needs with out too much boilerplate
     */
    public void invalidateDatabase(Set<String> keys, String prefix) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }
        synchronized (db) {
            if (shouldInvalidate) {
                try {
                    int dbKeysCount = db.countKeys(prefix);
                    if (dbKeysCount > keys.size()) {
                        String[] dbKeys = db.findKeys(prefix);
                        for (String dbKey : dbKeys) {
                            if (!keys.contains(dbKey)) {
                                db.del(dbKey);
                            }
                        }
                    }
                    shouldInvalidate = false;
                } catch (SnappydbException e) {
                    Log.e(TAG, "Error for invalidateDatabase with error: " + e.toString());
                }
            }
        }
    }

    public boolean shouldInvalidate() {
        return shouldInvalidate;
    }

    /*
        INT
     */
    public void putInt(String key, int value) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }
        synchronized (db) {
            try {
                db.putInt(key, value);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            }
        }
    }

    public int getInt(String key, int defaultValue) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return defaultValue;
        }
        synchronized (db) {
            try {
                return db.getInt(key);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            }
        }
        return defaultValue;
    }

    public boolean exists(String key) {
        boolean exists = false;
        if (db == null) {
            Log.e(TAG, "DB is null");
            return false;
        }
        synchronized (db) {
            try {
                exists = db.exists(key);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            }
        }
        return exists;
    }

    /*
        STRING
     */
    public void putString(String key, String value) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }
        synchronized (db) {
            try {
                db.put(key, value);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            }
        }
    }

    public String getString(String key, String defaultValue) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return defaultValue;
        }
        synchronized (db) {
            try {
                return db.get(key);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            }
        }
        return defaultValue;
    }
    
    /*
        BOOLEAN
     */

    public void putBoolean(String key, boolean value) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }
        synchronized (db) {
            try {
                db.putBoolean(key, value);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            }
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return defaultValue;
        }
        synchronized (db) {
            try {
                return db.getBoolean(key);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            }
        }
        return defaultValue;
    }

    /*
        FLOAT
     */
    public void putFloat(String key, float value) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }
        synchronized (db) {
            try {
                db.putFloat(key, value);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            }
        }
    }

    public float getFloat(String key, float defaultValue) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return defaultValue;
        }
        synchronized (db) {
            try {
                return db.getFloat(key);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            }
        }
        return defaultValue;
    }

    public void remove(String key, String type) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }
        synchronized (db) {
            try {
                db.del(key);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error removing key: " + key + ", for type: " +
                        type + " with error" + e.toString());
            }
        }
    }

    // Objects

    public <T> void putObject(String key, T value) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }
        synchronized (db) {
            try {
                db.put(key, value);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            }
        }
    }

    public <T> T getObject(String key, Class<T> objectClass, T defaultValue) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return defaultValue;
        }
        synchronized (db) {
            try {
                return db.getObject(key, objectClass);
            } catch (SnappydbException e) {
                Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            }
        }
        return defaultValue;
    }
}
