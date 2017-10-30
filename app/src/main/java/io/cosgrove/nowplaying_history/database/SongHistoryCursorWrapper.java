package io.cosgrove.nowplaying_history.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import io.cosgrove.nowplaying_history.database.SongHistoryDbSchema.SongHistoryTable;
import io.cosgrove.nowplaying_history.models.SongHistory;

/**
 * Created by vivalldi on 10/27/17.
 */

public class SongHistoryCursorWrapper extends CursorWrapper {

    public SongHistoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public SongHistory getSongHistory() {
        String songTitle = getString(getColumnIndex(SongHistoryTable.Cols.SONG_TITLE));
        String songArtist = getString(getColumnIndex(SongHistoryTable.Cols.SONG_ARTIST));
        String songHeardDate = getString(getColumnIndex(SongHistoryTable.Cols.SONG_HEARD_DATE));

        return new SongHistory(songTitle, songArtist, songHeardDate);
    };
}
