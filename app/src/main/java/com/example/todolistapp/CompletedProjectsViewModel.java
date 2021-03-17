package com.example.todolistapp;
import android.app.Application;

import com.example.todolistapp.data.CompletedProject;
import com.example.todolistapp.data.CompletedProjectsRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CompletedProjectsViewModel extends AndroidViewModel {
    private CompletedProjectsRepository repository;

    public CompletedProjectsViewModel(Application application) {
        super(application);
        this.repository = new CompletedProjectsRepository(application);
    }

    public void insertCompletedProject(CompletedProject project) {
        this.repository.insertCompletedProject(project);
    }

    public void deleteCompletedProject(CompletedProject project) {
        this.repository.deleteCompletedProject(project);
    }

    public LiveData<List<CompletedProject>> getAllCompletedProjects() {
        return this.repository.getAllCompletedProjects();
    }

    public LiveData <CompletedProject> getCompletedProjectByName(String name) {
        return this.repository.getCompletedProjectByName(name);
    }
}
