package io.cosgrove.nowplaying_history.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import io.cosgrove.nowplaying_history.database.SongHistoryDbSchema.SongHistoryTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by vivalldi on 10/27/17.
 */

public class SongHistoryDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 7;
    private static final String DATABASE_NAME = "SongHistory.db";
    private HashMap<String, String> mDbAlterCommands;
    private Context mContext;

    public SongHistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

        mContext = context;
        mDbAlterCommands = new HashMap<>();
        mDbAlterCommands.put(
                SongHistoryTable.Cols.SONG_TITLE,
                "alter table " + SongHistoryTable.NAME + "add column " +
                SongHistoryTable.Cols.SONG_TITLE + "TEXT NOT NULL DEFAULT '';"
        );
        mDbAlterCommands.put(
                SongHistoryTable.Cols.SONG_ARTIST,
                "alter table " + SongHistoryTable.NAME + "add column " +
                SongHistoryTable.Cols.SONG_ARTIST + "TEXT NOT NULL DEFAULT '';"
        );
        mDbAlterCommands.put(
                SongHistoryTable.Cols.SONG_HEARD_DATE,
                "alter table " + SongHistoryTable.NAME + "add column " +
                SongHistoryTable.Cols.SONG_HEARD_DATE + "TEXT;"
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB_CREATE", "Creating table " + SongHistoryTable.NAME);
        db.execSQL("create table " + SongHistoryTable.NAME + "(" +
            "_id integer primary key autoincrement, " +
            SongHistoryTable.Cols.SONG_TITLE + ", " +
            SongHistoryTable.Cols.SONG_ARTIST + ", " +
            SongHistoryTable.Cols.SONG_HEARD_DATE +
            ")"
        );
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            super.onDowngrade(db, oldVersion, newVersion);
        } catch (Exception e) {
            Log.e("DB_DOWNGRADE", "Failed to downgrade DB: " + e.getMessage());
            Toast.makeText(mContext, "App update has an issue! Please send logs to developer!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB_UPGRADE", "Updating " + SongHistoryTable.NAME + " table to version " +
                newVersion +" from version " + oldVersion);

        Cursor cursor = db.query(SongHistoryTable.NAME, null, null, null, null, null, null);
        ArrayList<String> existingColumns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
        cursor.close();
        ArrayList<String> missingColumns = SongHistoryTable.Cols.NAME_LIST;
        missingColumns.removeAll(existingColumns);

        try {
            for (String columnName : missingColumns) {
                Log.d("DB_UPGRADE", "Adding column " + columnName + " to table using: " +
                        mDbAlterCommands.get(columnName));
                db.execSQL(mDbAlterCommands.get(columnName));
            }
        } catch (Exception e) {
            Log.e("DB_UPGRADE", "Failed to upgrade DB: " + e.getMessage());
            Toast.makeText(mContext, "App update has an issue! Please send logs to developer!", Toast.LENGTH_LONG).show();
        }
    }
}
