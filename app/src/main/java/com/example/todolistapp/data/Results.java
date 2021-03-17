package com.example.todolistapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results {

    private ArrayList<ProjectData> projectsDataList;

    public Results() {
        this.projectsDataList = null;
    }

    public Results(String name) {
        this.projectsDataList = new ArrayList<>();
        ProjectData fakeProjectData = new ProjectData(name);
        for (int i = 0; i < 5; i++) {
            this.projectsDataList.add(fakeProjectData);
        }
    }

    public ArrayList<ProjectData> getProjectsDataList() {
        return projectsDataList;
    }
}
