package com.caskit.desktop_app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;


public interface Jsonable {

    ObjectMapper mapper = new ObjectMapper();

    default JSONObject toJsonObject(String... ignore) {
        return new JSONObject(toJSON(ignore));
    }

    default String toJSON(String... ignore){
        try {
            JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(this));
            for (String s : ignore){
                if (jsonObject.has(s)){
                    jsonObject.remove(s);
                }
            }
            return jsonObject.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

//    default String toJSON(){
//        try {
//            return new ObjectMapper().writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new IllegalStateException(e.getMessage());
//        }
//    }

    static <T> T fromJSON(String json, Class<T> valueType){
        try {
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }


}
