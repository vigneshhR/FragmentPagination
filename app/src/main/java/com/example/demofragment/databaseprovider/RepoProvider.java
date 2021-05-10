package com.example.demofragment.databaseprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class RepoProvider {
    public static final String TABLE = "Repo";

    public static final String REPO_ID = "RepoID";
    public static final String NODE_ID = "NodeID";
    public static final String NAME = "Name";
    public static final String FULL_NAME = "FullName";
    public static final String TYPE = "Type";
    public static final String AVATAR_URL = "AvatarURL";
    public static final String DESCRIPTION = "Description";

    public static final int REPO_ID_COLUMN = 1;
    public static final int NODE_ID_COLUMN = 2;
    public static final int NAME_COLUMN = 3;
    public static final int FULL_NAME_COLUMN = 4;
    public static final int TYPE_COLUMN = 5;
    public static final int AVATAR_COLUMN = 6;
    public static final int DESCRIPTION_COLUMN = 7;


    public static final String CREATE_TABLE = "create table " + TABLE
            + "(_id Integer primary key autoincrement," + REPO_ID
            + " numeric," + NODE_ID + " text," + NAME + " text,"
            + FULL_NAME + " text," + TYPE + " text,"
            + AVATAR_URL + " text," + DESCRIPTION + " text)";

    private DatabaseProvider mProvider = null;
    private SQLiteDatabase mDatabase = null;

    public RepoProvider(Context context) {
        mProvider = new DatabaseProvider(context);
    }

    public void open() {
        try {
            mDatabase = mProvider.getWritableDatabase();
        } catch (Exception e) {
            mDatabase = mProvider.getReadableDatabase();
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (mDatabase.isOpen()) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insert(ContentValues cv) {
        boolean isInserted = false;
        try {
            if (mDatabase.insert(TABLE, null, cv) > 0) {
                isInserted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInserted;
    }

    public boolean delete(String whereClause, String[] whereArgs) {
        boolean isDeleted = false;
        try {
            if (mDatabase.delete(TABLE, whereClause, whereArgs) > 0) {
                isDeleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public Cursor query(String selection, String[] selectionArgs) {
        Cursor cr = null;
        try {
            cr = mDatabase.query(TABLE, null, selection, selectionArgs, null,
                    null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr;
    }


    public boolean update(ContentValues values, String whereClause,
                          String[] whereArgs) {
        boolean isUpdated = false;
        try {
            if (mDatabase.update(TABLE, values, whereClause, whereArgs) > 0) {
                isUpdated = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

}
