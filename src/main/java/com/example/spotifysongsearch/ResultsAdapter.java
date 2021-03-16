package com.example.spotifysongsearch;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifysongsearch.data.Results;
import com.example.spotifysongsearch.data.SongData;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsItemViewHolder> {
    private Results results;
    private OnSongItemClickListener onSongItemClickListener;

    public interface OnSongItemClickListener {
        void onSongItemClick(SongData songData);
    }

    public ResultsAdapter(OnSongItemClickListener onSongItemClickListener) {
        this.onSongItemClickListener = onSongItemClickListener;
    }

    public void updateSongsData(Results results){
        this.results = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.results_list_item, parent, false);
        return new ResultsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsItemViewHolder holder, int position) {
        SongData songData = this.results.getSongsDataList().get(position);
        holder.bind(songData);
    }

    @Override
    public int getItemCount(){
        return this.results.getSongsDataList().size();
    }

    class ResultsItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;
        private TextView artistTV;

        public ResultsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTV = itemView.findViewById(R.id.tv_name);
            this.artistTV = itemView.findViewById(R.id.tv_artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSongItemClickListener.onSongItemClick(
                            results.getSongsDataList().get(getAdapterPosition())
                    );
                }
            });
        }

        // pass in a results object for the recycler view items
        @SuppressLint("SetTextI18n")
        void bind(SongData songData) {
            this.nameTV.setText(songData.getName());
            this.artistTV.setText(" - " + songData.getArtist());
        }
    }
}
