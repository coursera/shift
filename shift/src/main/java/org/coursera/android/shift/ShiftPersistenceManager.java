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
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.Set;

/**
 * Simple Persistence storage using https://github.com/nhachicha/SnappyDB.
 * Chose to use SnappyDB due to its simple NoSQL approach.
 * Allows us to store primitives easily like SharedPreferences, but also gives
 * flexibility to store more complicated objects in the future (Eg. Arrays with a selected index).
 */
class ShiftPersistenceManager {

    private static final String TAG = ShiftPersistenceManager.class.getCanonicalName();

    private static String TABLE_NAME = "shift.persistence.";

    // Invalidate once per launch
    private boolean shouldInvalidate = true;

    private Context mContext;

    public ShiftPersistenceManager(Context context) {
        mContext = context;
        TABLE_NAME = TABLE_NAME + mContext.getPackageName();
    }

    /**
     *  * INVALIDATE DB HELPERS:
     * If the client decides to remove ShiftValues from their code, we will have values in the
     * database that are no longer needed.
     * We should remove the extra keys/values that are not stored in the
     * {@link ShiftValueSubscriptionManagerImpl}
     * @param keys
     * @param prefix SnappyDB can only count with a prefix so we prepend this in
     * {@link ShiftValue#toString()}. Would prefer if there was simple a db.countAllKeys() but
     * aside from that SnappyDB seems to fit our needs with out too much boilerplate
     */
    public void invalidateDatabase(Set<String> keys, String prefix) {
        if (shouldInvalidate) {
            DB db = null;
            try {
                db = DBFactory.open(mContext, TABLE_NAME);
                int dbKeysCount = db.countKeys(prefix);
                if (dbKeysCount > keys.size()) {
                    String[] dbKeys = db.findKeys(prefix);
                    for (String dbKey : dbKeys) {
                        if (!keys.contains(dbKey)) {
                            db.del(dbKey);
                        }
                    }
                }
                db.close();
            } catch (SnappydbException e) {
                Log.e(TAG, "invalidateDatabase " + e.toString());
                if (db != null) {
                    try {
                        db.close();
                    } catch (SnappydbException e1) {
                        Log.e(TAG, "invalidateDatabase " + e.toString());
                    }
                }
            }
            shouldInvalidate = false;
        }
    }

    public boolean shouldInvalidate() {
        return shouldInvalidate;
    }

    /*
        INT
     */
    public void putInt(String key, int value) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.putInt(key, value);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "putInt " + e.toString());
                }
            }
        }
    }

    public int getInt(String key, int defaultValue) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            int value = db.getInt(key);
            db.close();
            return value;
        } catch (SnappydbException e) {
            Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "getInt " + e.toString());
                }
            }
        }
        return defaultValue;
    }

    public boolean exists(String key) {
        DB db = null;
        boolean exists = false;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            exists = db.exists(key);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "exists " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "exists " + e.toString());
                }
            }
        }
        return exists;
    }

    public void removeInt(String key) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.del(key);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "removeInt " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "removeInt " + e.toString());
                }
            }
        }
    }

    /*
        STRING
     */
    public void putString(String key, String value) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.put(key, value);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "putString " + e.toString());
                }
            }
        }
    }

    public String getString(String key, String defaultValue) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            String value = db.get(key);
            db.close();
            return value;
        } catch (SnappydbException e) {
            Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "getString " + e.toString());
                }
            }
        }
        return defaultValue;
    }

    public void removeString(String key) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.del(key);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Error removing key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "removeString " + e.toString());
                }
            }
        }
    }
    
    /*
        BOOLEAN
     */

    public void putBoolean(String key, boolean value) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.putBoolean(key, value);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "putBoolean " + e.toString());
                }
            }
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            boolean value = db.getBoolean(key);
            db.close();
            return value;
        } catch (SnappydbException e) {
            Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "getBoolean " + e.toString());
                }
            }
        }
        return defaultValue;
    }

    public void removeBoolean(String key) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.del(key);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Error removing key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "removeBoolean " + e.toString());
                }
            }
        }
    }

    /*
        FLOAT
     */
    public void putFloat(String key, float value) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.putFloat(key, value);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Error saving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "putFloat " + e.toString());
                }
            }
        }
    }

    public float getFloat(String key, float defaultValue) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            float value = db.getFloat(key);
            db.close();
            return value;
        } catch (SnappydbException e) {
            Log.e(TAG, "Error retrieving key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "getFloat " + e.toString());
                }
            }
        }
        return defaultValue;
    }

    public void removeFloat(String key) {
        DB db = null;
        try {
            db = DBFactory.open(mContext, TABLE_NAME);
            db.del(key);
            db.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Error removing key: " + key + ", with error " + e.toString());
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e1) {
                    Log.e(TAG, "removeFloat " + e.toString());
                }
            }
        }
    }
}
