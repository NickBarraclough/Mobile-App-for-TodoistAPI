package com.example.spotifysongsearch.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "likedSongs")
public class LikedSong implements Serializable {

    @PrimaryKey
    @NonNull
    private String name;

    @NonNull
    private String artist;


    public LikedSong(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    @NonNull
    public String getArtist() {
        return artist;
    }
}
