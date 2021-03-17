package com.example.todolistapp.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ProjectData implements Serializable {

    private String name;

    public ProjectData() {
        this.name = "";
    }

    public ProjectData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * This class is a custom JSON deserializer that can be plugged into Gson to directly parse
     * parts of the Spotify API response into a ProjectData object.
     * Specifically, this class can be used to parse objects in the `list` field of the
     * Spotify API response into a ProjectData object.
     *
     * Using a deserializer like this allows for directly mapping deeply nested fields in the API
     * response into a single, flat object like ProjectData instead of creating a complex Java
     * class hierarchy to mimic the structure of the API response.
     *
     * The mapping from the fields of a `list` object to the fields of a ProjectData object are
     * as follows:
     *
     *   list.dt --> ProjectData.epoch
     */
    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<ProjectData> {
        @Override
        public ProjectData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject listObj = json.getAsJsonObject();
//            JsonObject mainObj = listObj.getAsJsonObject("main");
//            JsonArray weatherArr = listObj.getAsJsonArray("weather");
//            JsonObject weatherObj = weatherArr.get(0).getAsJsonObject();
//            JsonObject cloudsObj = listObj.getAsJsonObject("clouds");
//            JsonObject windObj = listObj.getAsJsonObject("wind");

            return new ProjectData(
                    listObj.getAsJsonPrimitive("name").getAsString()
            );
        }
    }
}
