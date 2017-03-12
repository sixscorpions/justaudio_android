package com.justaudio.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Pavan on 1/31/2017.
 */

public class MovieListModel implements Serializable{

    private String movie_id = "";
    private String movie_name = "";
    private String movie_lang = "";
    private String movie_releaseDate = "";
    private String movie_background_image = "";
    private String movie_thumbnail_image = "";
    private boolean isFav;


    public MovieListModel(JSONObject object) throws JSONException {


        if (object.has("id"))
            setMovie_id(object.getString("id"));

        if (object.has("name"))
            setMovie_name(object.getString("name"));

        if (object.has("lang"))
            setMovie_lang(object.getString("lang"));

        if (object.has("releaseDate"))
            setMovie_releaseDate(object.getString("releaseDate"));

        if (object.has("background_image"))
            setMovie_background_image(object.getString("background_image"));

        if (object.has("thumbnail_image"))
            setMovie_thumbnail_image(object.getString("thumbnail_image"));

        if (object.has("like"))
            setFav(object.getBoolean("like"));
        else
            setFav(false);


    }


    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_lang() {
        return movie_lang;
    }

    public void setMovie_lang(String movie_lang) {
        this.movie_lang = movie_lang;
    }

    public String getMovie_releaseDate() {
        return movie_releaseDate;
    }

    public void setMovie_releaseDate(String movie_releaseDate) {
        this.movie_releaseDate = movie_releaseDate;
    }

    public String getMovie_background_image() {
        return movie_background_image;
    }

    public void setMovie_background_image(String movie_background_image) {
        this.movie_background_image = movie_background_image;
    }

    public String getMovie_thumbnail_image() {
        return movie_thumbnail_image;
    }

    public void setMovie_thumbnail_image(String movie_thumbnail_image) {
        this.movie_thumbnail_image = movie_thumbnail_image;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
