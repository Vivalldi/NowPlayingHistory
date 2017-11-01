package io.cosgrove.nowplaying_history.services;

//import android.app.NotificationManager;
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.cosgrove.nowplaying_history.models.SongHistory;
import io.cosgrove.nowplaying_history.utils.Constants;
import io.cosgrove.nowplaying_history.utils.SongHistoryStore;

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

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private SongHistoryStore mSongHistoryStore;
    private static final String NOW_PLAYING_PACKAGE = "com.google.intelligence.sense";

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "NLService Created");
        Log.i(TAG, "Number of Songs: " + SongHistoryStore.get(this).getSongHistory().size());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getPackageName().equals(NOW_PLAYING_PACKAGE)) {
            String notificationTitle = (String) sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE);
            mSongHistoryStore = SongHistoryStore.get(this);

            String date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).format(new Date());

            SongHistory song = new SongHistory(notificationTitle,date);

            SongHistory lastSong = null;

            Log.d(TAG, "******** SONG FOUND ********");
            Log.d(TAG, "Title: " + notificationTitle);


            if (!mSongHistoryStore.getSongHistory().isEmpty()) {
                lastSong = mSongHistoryStore.getSongHistory().get(0);
            }

            if (lastSong == null ||
                    !lastSong.getSongTitle().equals(song.getSongTitle()) ||
                    !lastSong.getSongArtist().equals(song.getSongArtist()) ||
                    moreThan5MinutesApart(lastSong.getSongHeardDate(),song.getSongHeardDate())) {
                mSongHistoryStore.addSongToHistory(song);
                Log.d(TAG, "Inserted song");

            } else {
                Log.d(TAG, "Song not inserted - Possible duplicate");
            }


        }

    }

    private boolean moreThan5MinutesApart(String firstDateString, String secondDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        Long minuteDif = null;
        try {
            Date firstDate = sdf.parse(firstDateString);
            Date secondDate = sdf.parse(firstDateString);
            long difference = secondDate.getTime() - firstDate.getTime();
            minuteDif = TimeUnit.MILLISECONDS.toMinutes(difference);
            Log.i(TAG, "Difference: " + minuteDif);
        } catch (ParseException e) {
            Log.wtf(TAG, "Unable to parse date from string: " + e.getMessage());
            Toast.makeText(this.getApplicationContext(), "ERROR! Unable to parse string to date. Contact developer", Toast.LENGTH_LONG).show();
        }

        return Math.abs(minuteDif) > 5;
    }
}
