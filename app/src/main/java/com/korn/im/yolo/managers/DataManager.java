package com.korn.im.yolo.managers;

import android.content.Context;

import com.korn.im.yolo.objects.News;
import com.korn.im.yolo.objects.Portfolio;
import com.korn.im.yolo.objects.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager
{
    private static final String DATA_PREFIX = ".json";

    private Map<Integer, List<Post>> dataMap;

    private static DataManager instance;

    public DataManager() {
        dataMap = Collections.synchronizedMap(new HashMap<Integer, List<Post>>());
    }

    public boolean parseStringToObjects(int type, String data, boolean append) {
        try {
            JSONObject object;
            JSONArray dataArray = new JSONArray(data);

            if (dataMap == null) dataMap = new HashMap<>();
            List<Post> posts;
            if ((posts = dataMap.get(type)) == null) {
                posts = Collections.synchronizedList(new ArrayList<Post>());
                dataMap.put(type, posts);
            }

            if(!append) {
                posts.clear();
            }

            for (int i = 0; i < dataArray.length(); i++) {
                object = dataArray.optJSONObject(i);
                if(object != null) {

                    switch (type) {
                        case News.CATEGORY_ID : {
                            try {
                                posts.add(new News(object));
                            } catch (JSONException | ParseException ignored) {}
                            break;
                        }
                        case Portfolio.CATEGORY_ID : {
                            try {
                                posts.add(new Portfolio(object));
                            } catch (JSONException | ParseException ignored) {}
                            break;
                        }
                    }
                }

            }
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public boolean save(int type, Context context) {
        File file = null;
        try {
            file = new File(context.getApplicationInfo().dataDir + File.separator + type + DATA_PREFIX);
            if (!file.exists()) file.createNewFile();

            List<Post> posts = dataMap.get(type);

            if(posts != null && posts.size() != 0) {
                JSONArray jsonArray = new JSONArray();

                for (Post post:
                     posts) {
                    try {
                        jsonArray.put(post.parseToJsonObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(jsonArray.toString());
                bufferedWriter.close();
            } else return false;
        } catch (IOException e) {
            if (file != null)
                file.delete();
            return false;
        }
        return true;
    }

    public boolean restore(int type, Context context) {
        File file = null;
        try {
            file = new File(context.getApplicationInfo().dataDir + File.separator + type + DATA_PREFIX);
            if (file.exists()) {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String string;

                while ((string = bufferedReader.readLine()) != null)
                    stringBuilder.append(string);

                bufferedReader.close();

                return parseStringToObjects(type, stringBuilder.toString(), false);
            } else return false;
        } catch (IOException e) {
            if(file != null && file.exists())
                file.delete();
            return false;
        }
    }

    public  List<Post> getDataByCategory(int category) {
        return dataMap.get(category);
    }
}
