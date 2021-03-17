package com.example.todolistapp.data;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CompletedProjectsRepository {
    private CompletedProjectsDAO dao;

    public CompletedProjectsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.completedProjectsDAO();
    }

    public void insertCompletedProject(CompletedProject project) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(project);
            }
        });
    }

    public void deleteCompletedProject(CompletedProject project) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(project);
            }
        });
    }

    public LiveData<List<CompletedProject>> getAllCompletedProjects(){
        return this.dao.getAllProjects();
    }

    public LiveData<CompletedProject> getCompletedProjectByName(String name) {
        return this.dao.getProjectByName(name);
    }
}
