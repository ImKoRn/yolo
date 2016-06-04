package com.korn.im.yolo.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Post implements Parcelable {

    public static final String ID_FIELD = "id";
    public static final String DATE_FIELD = "date";
    public static final String TITLE_FIELD = "title";
    public static final String CONTENT_FIELD = "content";
    private static final String RENDERED_FIELD = "rendered";

    private int id;
    private Date date;
    private String title = "";
    private String content = "";

    protected Post() {
    }

    protected Post(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        content = parcel.readString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void parseFromJsonObject(JSONObject jsonObject) throws JSONException, ParseException {
        // TODO Add more protection
        this.id = jsonObject.getInt(ID_FIELD);
        //this.date = DateFormat.getInstance().parse(jsonObject.getString(DATE_FIELD));
        this.title = Html.fromHtml(jsonObject.optJSONObject(TITLE_FIELD).getString(RENDERED_FIELD)).toString();
        this.content = Html.fromHtml(jsonObject.optJSONObject(CONTENT_FIELD).getString(RENDERED_FIELD)).toString();
    }

    public JSONObject parseToJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID_FIELD, id);
        //jsonObject.put(DATE_FIELD, date.toString());
        JSONObject titleObj = new JSONObject();
        titleObj.put(RENDERED_FIELD, title);
        jsonObject.put(TITLE_FIELD, titleObj);
        JSONObject contentObj = new JSONObject();
        contentObj.put(RENDERED_FIELD, content);
        jsonObject.put(CONTENT_FIELD, contentObj);

        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
