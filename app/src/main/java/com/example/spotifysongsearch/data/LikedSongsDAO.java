package com.example.spotifysongsearch.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LikedSongsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LikedSongs song);

    @Query("SELECT * FROM likedSongs ORDER BY name DESC")
    LiveData<List<LikedSongs>> getAllSongs();
}
