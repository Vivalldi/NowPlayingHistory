package io.cosgrove.nowplaying_history;

import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Set;

import io.cosgrove.nowplaying_history.models.SongHistory;
import io.cosgrove.nowplaying_history.utils.Constants;
import io.cosgrove.nowplaying_history.utils.SongHistoryStore;

public class SongHistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SongHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_history);

        Set<String> EnabledListenerPackagesSet =
                NotificationManagerCompat.getEnabledListenerPackages(this);

        if (EnabledListenerPackagesSet == null ||
                !EnabledListenerPackagesSet.contains(Constants.PACKAGE_NAME)) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.song_history_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // get dataset
        List<SongHistory> songHistoryList = SongHistoryStore.get(this).getSongHistory();

        // specify an adapter
        mAdapter = new SongHistoryAdapter(songHistoryList);
        mRecyclerView.setAdapter(mAdapter);




    }

    @Override
    public void onRefresh() {
        List<SongHistory> newSongHistoryList = SongHistoryStore.get(this).getSongHistory();
        mAdapter.setDataset(newSongHistoryList);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
