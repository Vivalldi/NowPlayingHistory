package io.cosgrove.nowplaying_history.services;

//import android.app.NotificationManager;
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

//    private NotificationManager mNotificationManager;
    private String TAG = this.getClass().getSimpleName();
    private SongHistoryStore mSongHistoryStore;
    private static final String NOW_PLAYING_PACKAGE = "com.google.intelligence.sense";

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "NLService Created");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if ( sbn.getPackageName().toString().equals(NOW_PLAYING_PACKAGE)) {
            Log.i(TAG, "******** SONG FOUND ********");
            String notificationTitle = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();
            Log.i(TAG, "Title: " + notificationTitle);
            mSongHistoryStore = SongHistoryStore.get(this);
            Log.i(TAG, "SONG HISTORY: " + mSongHistoryStore.getSongHistory());

            String date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).format(new Date());
            Log.i(TAG, "DATE STAMP: " + date);

            SongHistory song = new SongHistory(notificationTitle,date);
            mSongHistoryStore.addSongToHistory(song);


        } else {
            Log.i(TAG, "Some notification");
        }

    }
}
