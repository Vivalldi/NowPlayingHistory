package io.cosgrove.nowplaying_history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.cosgrove.nowplaying_history.models.SongHistory;

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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mTitleTextView;
        public TextView mArtistTextView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
            mTitleTextView = (TextView) v.findViewById(R.id.song_history_title);
            mArtistTextView = (TextView) v.findViewById(R.id.song_history_artist);

        }
    }
    // Constructor
    public SongHistoryAdapter(List<SongHistory> dataset) {
        mDataset = dataset;
    }

    // Create new views (called by layout manager)
    @Override
    public SongHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_history_item, parent, false);

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

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
