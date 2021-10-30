package com.kxw959.programmingapplication.utils;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JSONUtil {

    public JSONUtil(){

    }

    public List<JsonObject> getStudents(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try{
            return readArray(reader);
        }
        finally {
            reader.close();
        }
    }

    private List<JsonObject> readArray(JsonReader reader) throws IOException {
        List<JsonObject> records = new ArrayList<>();

        reader.beginArray();
        while(reader.hasNext()){
            records.add(readObject(reader));
        }
        reader.endArray();
        return records;
    }

    private JsonObject readObject(JsonReader reader) throws IOException {
        JsonObject object = new JsonObject();

        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(Objects.equals(name, "name") || Objects.equals(name, "username") || Objects.equals(name, "password")){
                object.addProperty(name, reader.nextString());
            }
            else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return object;
    }




}
