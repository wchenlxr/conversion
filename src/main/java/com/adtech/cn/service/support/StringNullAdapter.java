package com.adtech.cn.service.support;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Administrator on 2018/3/28.
 */
public class StringNullAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter jsonWriter, String s) throws IOException {
        if (s == null) {
            jsonWriter.value("");
            return;
        }
        jsonWriter.value(s);
    }

    @Override
    public String read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return "";
        }
        return jsonReader.nextString();
    }
}
