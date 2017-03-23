package com.ims.tasol.networkingexample.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ims.tasol.networkingexample.model.Task;

import java.lang.reflect.Type;

/**
 * Created by tasol on 22/3/17.
 */

public class CustomGsonAdapter {
    public static class UserAdapter implements JsonSerializer<Task>{

        @Override
        public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context) {
            Gson gson= new Gson();
            JsonElement je=gson.toJsonTree(src);
            JsonObject jo=new JsonObject();
            jo.add("reqObject",je);
            return jo;
        }
    }
}
