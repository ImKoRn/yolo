package com.korn.im.yolo.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Portfolio representation. Has ownerName his owner, price, description and references to images.
 */
public class Portfolio extends Post {
    public static final int CATEGORY_ID = 24;
    private static final String ICON_FIELD = "title_image_link";
    private static final String IMAGE_LIST_FIELD = "body_image_link";
    private static final String PORTFOLIO_CONTENT_FIELD = "portfolio_content";

    private int price;

    private String iconReference = null;
    private List<String> listOfPhotoReferences = new ArrayList<>();

    public Portfolio() {
    }

    public Portfolio(JSONObject object) throws JSONException, ParseException {
        parseFromJsonObject(object);
    }

    private Portfolio(Parcel parcel) {
        super(parcel);
        iconReference = parcel.readString();
        parcel.readStringList(listOfPhotoReferences);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getIconReference() {
        return iconReference;
    }

    public void setIconReference(String iconReference) {
        this.iconReference = iconReference;
    }

    public List<String> getListOfPhotoReferences() {
        return listOfPhotoReferences;
    }

    public void setListOfPhotoReferences(List<String> listOfPhotoReferences) {
        this.listOfPhotoReferences = listOfPhotoReferences;
    }

    @Override
    public void parseFromJsonObject(JSONObject jsonObject) throws JSONException, ParseException {
        super.parseFromJsonObject(jsonObject);
        JSONArray arrayOfTitleImageLinks = jsonObject.optJSONArray(ICON_FIELD);
        if(arrayOfTitleImageLinks != null) {
            iconReference = arrayOfTitleImageLinks.optString(0, null);
        }

        JSONArray arrayOfLinks = jsonObject.optJSONArray(IMAGE_LIST_FIELD);
        if (arrayOfLinks != null) {
            String string;
            for (int i = 0; i < arrayOfLinks.length(); i++) {
                if((string = arrayOfLinks.optString(i, null)) != null)
                    listOfPhotoReferences.add(string);
            }
        }

        JSONArray content = jsonObject.optJSONArray(PORTFOLIO_CONTENT_FIELD);
        if(content != null) {
            setContent(Html.fromHtml(content.optString(0, "")).toString());
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

        JSONArray content = new JSONArray();
        content.put(getContent());
        jsonObject.put(PORTFOLIO_CONTENT_FIELD, content);

        JSONArray imagesArray = new JSONArray();
        if(!listOfPhotoReferences.isEmpty())
            for (String reference : listOfPhotoReferences) imagesArray.put(reference);
        jsonObject.put(IMAGE_LIST_FIELD, imagesArray);

        return jsonObject;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(iconReference);
        dest.writeStringList(listOfPhotoReferences);
    }

    public static final Parcelable.Creator<Portfolio> CREATOR = new Parcelable.Creator<Portfolio>() {
        public Portfolio createFromParcel(Parcel in) {
            return new Portfolio(in);
        }

        public Portfolio[] newArray(int size) {
            return new Portfolio[size];
        }
    };
}
