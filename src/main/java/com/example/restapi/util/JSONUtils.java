package com.example.restapi.util;

import com.example.restapi.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class JSONUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String toJSON(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJSON(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // Test JSON serialization and deserialization
        User user = new User(1, "John Doe", "john@example.com", 30);
        String json = toJSON(user);
        System.out.println("Serialized JSON: " + json);

        User deserializedUser = fromJSON(json, User.class);
        System.out.println("Deserialized User: " + deserializedUser);
    }
}
