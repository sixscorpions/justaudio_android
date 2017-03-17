package com.justaudio.dto;

import com.justaudio.audioplayer.Origin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by VIDYA
 */

public class TrackAudioModel implements Serializable {

    private long id;
    private String title;
    private int position;
    private String path;
    private Origin origin;
    private boolean isPlaying = false;
    private String thumbnail_image;


    TrackAudioModel(JSONObject object) throws JSONException {


        setOrigin(Origin.URL);

        if (object.has("id"))
            setId(object.getInt("id"));

        if (object.has("name"))
            setTitle(object.getString("name"));

        if (object.has("path"))
            setPath(object.getString("path"));

        if (object.has("thumbnail_image"))
            setThumbnail_image(object.getString("thumbnail_image"));








    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}