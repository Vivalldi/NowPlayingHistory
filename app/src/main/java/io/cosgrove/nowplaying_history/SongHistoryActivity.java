package io.cosgrove.nowplaying_history;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    private static final int APP_INTRO_FIRST_LAUNCH_INTENT = 1;

    private RecyclerView mRecyclerView;
    private SongHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_history);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> EnabledListenerPackagesSet =
                NotificationManagerCompat.getEnabledListenerPackages(this);

        if (mPreferences.getInt(getString(R.string.version_key), 0) < Constants.VERSION_CODE
                && mPreferences.getBoolean(getString(R.string.done_first_launch_key), false)) {
            //App has been updated

        }
        if (!mPreferences.getBoolean(getString(R.string.done_first_launch_key), false)) {
            // First launch
            startActivityForResult(
                    new Intent(SongHistoryActivity.this, MainIntroActivity.class),
                    APP_INTRO_FIRST_LAUNCH_INTENT
            );
        } else if (!EnabledListenerPackagesSet.contains(Constants.PACKAGE_NAME)) {
            startActivity(new Intent(this, MainIntroActivity.class));
        }

        mRecyclerView = findViewById(R.id.song_history_recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new SongHistoryAdapter(SongHistoryStore.get(this));
        mRecyclerView.setAdapter(mAdapter);




    }

    @Override
    public void onRefresh() {
        List<SongHistory> newSongHistoryList = SongHistoryStore.get(this).getSongHistory();
        mAdapter.setDataset(newSongHistoryList);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case APP_INTRO_FIRST_LAUNCH_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    mPreferences.edit().putBoolean(getString(R.string.done_first_launch_key), true).apply();
                } else {
                    new AlertDialog.Builder(SongHistoryActivity.this)
                            .setMessage(getString(R.string.setup_incomplete))
                            .setTitle(getString(R.string.setup_incomplete_title))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(
                                            new Intent(SongHistoryActivity.this, MainIntroActivity.class),
                                            APP_INTRO_FIRST_LAUNCH_INTENT
                                    );
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPreferences.edit().putBoolean(getString(R.string.done_first_launch_key), true).apply();
                                }
                            })
                            .create()
                            .show();
                }
                return;
        }
    }
}
