package com.example.todolistapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompletedProjectsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CompletedProject project);

    @Delete
    void delete(CompletedProject project);

    @Query("SELECT * FROM completedProjects ORDER BY name DESC")
    LiveData<List<CompletedProject>> getAllProjects();

    @Query("SELECT * FROM completedProjects WHERE name = :name LIMIT 1")
    LiveData<CompletedProject> getProjectByName(String name);
}
