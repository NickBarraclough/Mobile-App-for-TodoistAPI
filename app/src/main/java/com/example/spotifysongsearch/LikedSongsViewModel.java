package com.example.spotifysongsearch;
import android.app.Application;

import com.example.spotifysongsearch.data.LikedSong;
import com.example.spotifysongsearch.data.LikedSongsRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class LikedSongsViewModel extends AndroidViewModel {
    private LikedSongsRepository repository;

    public LikedSongsViewModel(Application application) {
        super(application);
        this.repository = new LikedSongsRepository(application);
    }

    public void insertBookmarkedRepo(LikedSong song) {
        this.repository.insertLikedSong(song);
    }

    public void deleteBookmarkedRepo(LikedSong song) {
        this.repository.deleteLikedSong(song);
    }

    public LiveData<List<LikedSong>> getAllLikedSongs() {
        return this.repository.getAllLikedSongs();
    }

    public LiveData <LikedSong> getBookmarkedRepoByName(String name) {
        return this.repository.getLikedSongByName(name);
    }
}
