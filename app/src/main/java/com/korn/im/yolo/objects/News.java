package com.korn.im.yolo.objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * News object
 */
public class News extends Post {
    public static final int CATEGORY_ID = 22;
    private static final String ICON_FIELD = "title_image_link";

    private String iconReference = null;

    public News(JSONObject object) throws JSONException, ParseException {
        parseFromJsonObject(object);
    }

    private News(Parcel parcel) {
        super(parcel);
        iconReference = parcel.readString();
    }

    public String getIconReference() {
        return iconReference;
    }

    @Override
    public void parseFromJsonObject(JSONObject jsonObject) throws JSONException, ParseException {
        super.parseFromJsonObject(jsonObject);
        JSONArray arrayOfTitleImageLinks = jsonObject.optJSONArray(ICON_FIELD);
        if(arrayOfTitleImageLinks != null) {
            iconReference = arrayOfTitleImageLinks.optString(0, null);
        }
    }

    @Override
    public JSONObject parseToJsonObject() throws JSONException {
        JSONObject jsonObject = super.parseToJsonObject();

        JSONArray titleImage;
        if(iconReference != null) {
            titleImage = new JSONArray();
            titleImage.put(iconReference);
            jsonObject.put(ICON_FIELD, titleImage);
        }

        return jsonObject;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(iconReference);
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
