package com.example.spotifysongsearch.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results {
    @SerializedName("list")
    private ArrayList<SongData> songsDataList;

    public Results() {
        this.songsDataList = null;
    }
    public Results(String name, String artist) {
        this.songsDataList = new ArrayList<>();
        SongData fakeSongData = new SongData(name, artist, System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            this.songsDataList.add(fakeSongData);
        }
    }
    public ArrayList<SongData> getSongsDataList() {
        return songsDataList;
    }
}
