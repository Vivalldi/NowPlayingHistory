package io.cosgrove.nowplaying_history.database;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vivalldi on 10/27/17.
 */

public class SongHistoryDbSchema {
    public static final class SongHistoryTable {
        public static final String NAME = "songHistory";

        public static final class Cols {
            public static final String SONG_TITLE = "songTitle";
            public static final String SONG_ARTIST = "songArtist";
            public static final String SONG_HEARD_DATE = "songHeardDate";

            static final ArrayList<String> NAME_LIST = new ArrayList<>(Arrays.asList(
                    "_id",
                    SONG_TITLE,
                    SONG_ARTIST,
                    SONG_HEARD_DATE
            ));
        }
    }
}
