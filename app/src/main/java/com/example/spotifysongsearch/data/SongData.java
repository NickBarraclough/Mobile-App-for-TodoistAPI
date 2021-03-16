package com.example.spotifysongsearch.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class SongData implements Serializable {

    private long timeSinceLastSearch;
    private String name;
    private String artist;

    public SongData() {
        this.timeSinceLastSearch = 0;
        this.name = "";
        this.artist = "";
    }

    public SongData(String name, String artist, long timeSinceLastSearch) {
        this.name = name;
        this.artist = artist;
        this.timeSinceLastSearch = timeSinceLastSearch;
    }

    public long getTimeSinceLastSearch() {
        return timeSinceLastSearch;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    /**
     * This class is a custom JSON deserializer that can be plugged into Gson to directly parse
     * parts of the Spotify API response into a SongData object.
     * Specifically, this class can be used to parse objects in the `list` field of the
     * Spotify API response into a SongData object.
     *
     * Using a deserializer like this allows for directly mapping deeply nested fields in the API
     * response into a single, flat object like SongData instead of creating a complex Java
     * class hierarchy to mimic the structure of the API response.
     *
     * The mapping from the fields of a `list` object to the fields of a SongData object are
     * as follows:
     *
     *   list.dt --> SongData.epoch
     */
    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<SongData> {
        @Override
        public SongData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject listObj = json.getAsJsonObject();
//            JsonObject mainObj = listObj.getAsJsonObject("main");
//            JsonArray weatherArr = listObj.getAsJsonArray("weather");
//            JsonObject weatherObj = weatherArr.get(0).getAsJsonObject();
//            JsonObject cloudsObj = listObj.getAsJsonObject("clouds");
//            JsonObject windObj = listObj.getAsJsonObject("wind");

            return new SongData(
                    listObj.getAsJsonPrimitive("name").getAsString(),
                    listObj.getAsJsonPrimitive("artist").getAsString(),
                    listObj.getAsJsonPrimitive("dt").getAsInt()
            );
        }
    }
}
