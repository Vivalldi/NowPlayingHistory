package io.cosgrove.nowplaying_history;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.cosgrove.nowplaying_history.models.SongHistory;
import io.cosgrove.nowplaying_history.utils.DateHelper;
import io.cosgrove.nowplaying_history.utils.SongHistoryStore;

/*
  Created by Tyler Cosgrove (vivalldi) on 10/31/17.
  
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


public class SongHistoryAdapter extends RecyclerView.Adapter<SongHistoryAdapter.ViewHolder> {
    private List<SongHistory> mDataset;
    private SongHistoryStore mSongHistoryStore;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        public View mView;
        public TextView mTitleTextView;
        public TextView mArtistTextView;
        public TextView mTimestampTextView;
        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            mView = v;
            mTitleTextView = (TextView) v.findViewById(R.id.song_title);
            mArtistTextView = (TextView) v.findViewById(R.id.song_artist);
            mTimestampTextView = (TextView) v.findViewById(R.id.song_timestamp);

        }
        @Override
        public void onClick(View view) {
            Log.d("SongHistoryAdapter", "onClick " + mTitleTextView.getText());
            String artist = (String) mArtistTextView.getText();
            String title = (String) mTitleTextView.getText();
            Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
            intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                    "vnd.android.cursor.item/audio");
            intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artist);
            intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, title);
            intent.putExtra(SearchManager.QUERY, title);
            if(intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
        @Override
        public boolean onLongClick(View view) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            removeItem(getAdapterPosition());
            return true;
        }
    }
    // Constructor
    public SongHistoryAdapter(SongHistoryStore songHistoryStore) {
        mSongHistoryStore = songHistoryStore;
        mDataset = songHistoryStore.getSongHistory();
    }

    public void setDataset(List<SongHistory> updatedDataset) {
        this.mDataset = updatedDataset;
    }

    public void removeItem(int position) {
        mSongHistoryStore.deleteSongHistory(mDataset.get(position));
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (called by layout manager)
    @Override
    public SongHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_history_item_relative, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        SongHistory currentSong = mDataset.get(position);
        holder.mTitleTextView.setText(currentSong.getSongTitle());
        holder.mArtistTextView.setText(currentSong.getSongArtist());
        holder.mTimestampTextView.setText(DateHelper.generateUITimestamp(currentSong.getSongHeardDate()));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
