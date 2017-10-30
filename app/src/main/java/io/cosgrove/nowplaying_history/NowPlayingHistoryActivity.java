package io.cosgrove.nowplaying_history;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

import io.cosgrove.nowplaying_history.models.SongHistory;
import io.cosgrove.nowplaying_history.utils.Constants;
import io.cosgrove.nowplaying_history.utils.SongHistoryStore;

public class NowPlayingHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Set<String> EnabledListenerPackagesSet = NotificationManagerCompat.getEnabledListenerPackages(this);

        if(EnabledListenerPackagesSet == null || !EnabledListenerPackagesSet.contains(Constants.PACKAGE_NAME)) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

//        ListView songListView = (ListView) findViewById(R.id.song_history_list_view);
//        SongHistoryStore mSongHistoryStore = SongHistoryStore.get(this);
//        ArrayList<SongHistory> songHistories = mSongHistoryStore.getSongHistory();
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, songHistories);
//        songListView.setAdapter(arrayAdapter);


    }

}
