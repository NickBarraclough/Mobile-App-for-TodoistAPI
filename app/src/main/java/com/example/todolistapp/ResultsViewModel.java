package com.example.todolistapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolistapp.data.Results;
import com.example.todolistapp.data.ResultsRepository;
import com.example.todolistapp.data.LoadingStatus;

public class ResultsViewModel extends ViewModel {
    private ResultsRepository repository;
    private LiveData<Results> results;
    private LiveData<LoadingStatus> loadingStatus;

    public ResultsViewModel() {
        this.repository = new ResultsRepository();
        results = repository.getResults();
        loadingStatus = repository.getLoadingStatus();
    }

    public LiveData<Results> getResults() {
        return this.results;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadResults(Boolean show_complete) {
        this.repository.loadResults(show_complete);
    }
}
