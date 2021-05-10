package com.example.demofragment.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.demofragment.databaseprovider.RepoProvider;

import java.util.ArrayList;
import java.util.List;

public class RepoStorage {

    private static RepoStorage sRepoStorage = null;
    private RepoProvider mRepoProvider = null;
    private ArrayList<Repo> mRepoList = null;


    private RepoStorage(Context context) {
        mRepoList = new ArrayList<Repo>();
        mRepoProvider = new RepoProvider(context);
    }

    public static RepoStorage getInstance(Context context) {
        if (sRepoStorage == null) {
            sRepoStorage = new RepoStorage(context);
        }
        return sRepoStorage;
    }

    public ArrayList<Repo> getRepoList() {
        readRecords();
        return mRepoList;
    }

    public void readRecords() {
        try {
            mRepoList.clear();
            mRepoProvider.open();
            Cursor cr = null;
            cr = mRepoProvider.query(null, null);

            if (cr != null) {
                if (cr.moveToFirst()) {
                    do {
                        Repo allocation = getRepo(cr);
                        mRepoList.add(allocation);
                    } while (cr.moveToNext());
                    cr.close();
                }
            }

            mRepoProvider.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Repo getRepo(Cursor cr) {
        Repo allocation = new Repo();
        try {
            allocation.RepoId = cr
                    .getInt(RepoProvider.REPO_ID_COLUMN);
            allocation.NodeId = cr
                    .getString(RepoProvider.NODE_ID_COLUMN);
            allocation.Name = (cr
                    .getString(RepoProvider.NAME_COLUMN));
            allocation.FullName = cr
                    .getString(RepoProvider.FULL_NAME_COLUMN);
            allocation.Type = cr
                    .getString(RepoProvider.TYPE_COLUMN);
            allocation.AvatarURL = (cr
                    .getString(RepoProvider.AVATAR_COLUMN));
            allocation.Description = (cr
                    .getString(RepoProvider.DESCRIPTION_COLUMN));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allocation;
    }

    public boolean insertRepo(List<Repo> repoList) {
        boolean isInserted = false;
        try {
            List<Repo> mRepoList = repoList;

            mRepoProvider.open();

            for (Repo allocation : mRepoList) {

                ContentValues cv = new ContentValues();

                cv.put(RepoProvider.REPO_ID,
                        allocation.RepoId);
                cv.put(RepoProvider.NODE_ID, allocation.NodeId);
                cv.put(RepoProvider.NAME,
                        (allocation.Name));
                cv.put(RepoProvider.FULL_NAME, allocation.FullName);
                cv.put(RepoProvider.TYPE,
                        allocation.Type);
                cv.put(RepoProvider.AVATAR_URL,
                        (allocation.AvatarURL));
                cv.put(RepoProvider.DESCRIPTION,
                        (allocation.Description));

                if (!mRepoProvider.update(cv,
                        RepoProvider.REPO_ID + "=?", new String[]{""
                                + allocation.RepoId})) {
                    mRepoProvider.insert(cv);
                }
                isInserted = true;
            }
            mRepoProvider.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInserted;
    }

    public Repo getAllocationByID(int allocationID) {
        Repo allocation = new Repo();

        try {
            mRepoProvider.open();

            Cursor cr = mRepoProvider.query(RepoProvider.REPO_ID
                    + "=?", new String[]{"" + allocationID});
            if (cr != null) {
                if (cr.moveToFirst()) {
                    do {
                        allocation = getRepo(cr);
                    } while (cr.moveToNext());
                    cr.close();
                }
            }
            mRepoProvider.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allocation;
    }

    public boolean deleteAllocation(String accountNo) {

        boolean isDeleted = false;
        try {
            mRepoProvider.open();
            if (mRepoProvider.delete(RepoProvider.REPO_ID
                    + "=?", new String[]{"" + accountNo})) {

                isDeleted = true;
            }

            mRepoProvider.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

}