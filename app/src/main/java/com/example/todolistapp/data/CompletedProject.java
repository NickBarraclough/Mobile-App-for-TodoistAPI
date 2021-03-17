package com.example.todolistapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "completedProjects")
public class CompletedProject implements Serializable {

    @PrimaryKey
    @NonNull
    private String name;

    public CompletedProject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
