package io.cosgrove.nowplaying_history.utils;
/*
  Created by Tyler Cosgrove (vivalldi) on 10/27/17.
  
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


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.cosgrove.nowplaying_history.database.SongHistoryCursorWrapper;
import io.cosgrove.nowplaying_history.database.SongHistoryDbHelper;
import io.cosgrove.nowplaying_history.database.SongHistoryDbSchema.SongHistoryTable;
import io.cosgrove.nowplaying_history.models.SongHistory;

public class SongHistoryStore {
    private static SongHistoryStore sSongHistoryStore;

    private SQLiteDatabase mDatabase;

    public static SongHistoryStore get(Context context) {
        if (sSongHistoryStore == null) {
            sSongHistoryStore = new SongHistoryStore(context);
        }

        return sSongHistoryStore;
    }

    private SongHistoryStore(Context context) {
        Context c = context.getApplicationContext();
        try {
            mDatabase = new SongHistoryDbHelper(c).getWritableDatabase();
        } catch (SQLException e) {
            Log.wtf("DB_OPEN_ERROR", "Unable to open the database for read/write: " + e.getMessage());
            Toast.makeText(c, "ERROR! Unable to store app settings because there is no free space! Please create more free space!", Toast.LENGTH_LONG).show();
        }
    }

    public List<SongHistory> getSongHistory() {
        List<SongHistory> songHistories = new ArrayList<>();

        try (SongHistoryCursorWrapper cursor = querySongHistories(null, null, SongHistoryTable.Cols.SONG_HEARD_DATE + " DESC")) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                SongHistory songHistory = cursor.getSongHistory();
                songHistories.add(songHistory);
                cursor.moveToNext();
            }
        }

        Collections.sort(songHistories, new Comparator<SongHistory>() {
            @Override
            public int compare(SongHistory o1, SongHistory o2) {
                return 0;
            }
        });

        return songHistories;
    }

    public void addSongToHistory(SongHistory songHistory) throws SQLException {
        ContentValues values = getContentValues(songHistory);

        mDatabase.insertOrThrow(SongHistoryTable.NAME, null, values);
    }



    public void deleteSongHistory(SongHistory songHistory) {
        String songTitle = songHistory.getSongTitle();
        String songHeardDate = songHistory.getSongHeardDate();

        mDatabase.delete(SongHistoryTable.NAME, SongHistoryTable.Cols.SONG_TITLE +
                " =? AND " + SongHistoryTable.Cols.SONG_HEARD_DATE + " =?", new String[]{songTitle, songHeardDate});

    }

    public Integer size() {
        String countQuery = "SELECT * FROM " + SongHistoryTable.NAME;
        Cursor cursor = mDatabase.rawQuery(countQuery, null);

        Integer count = cursor.getCount();
        cursor.close();

        return count;
    }


    private SongHistoryCursorWrapper querySongHistories(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = mDatabase.query(
                SongHistoryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );

        return new SongHistoryCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(SongHistory songHistory) {
        ContentValues values = new ContentValues();
        values.put(SongHistoryTable.Cols.SONG_TITLE, songHistory.getSongTitle());
        values.put(SongHistoryTable.Cols.SONG_ARTIST, songHistory.getSongArtist());
        values.put(SongHistoryTable.Cols.SONG_HEARD_DATE, songHistory.getSongHeardDate());

        return values;
    }
}
