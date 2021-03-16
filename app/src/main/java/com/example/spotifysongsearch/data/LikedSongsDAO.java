package com.example.spotifysongsearch.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LikedSongsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LikedSong song);

    @Delete
    void delete(LikedSong song);

    @Query("SELECT * FROM likedSongs ORDER BY name DESC")
    LiveData<List<LikedSong>> getAllSongs();

    @Query("SELECT * FROM likedSongs WHERE name = :name LIMIT 1")
    LiveData<LikedSong> getSongByName(String name);
}
