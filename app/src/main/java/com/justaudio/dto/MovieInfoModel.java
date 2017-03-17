package com.justaudio.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pavan
 */

public class MovieInfoModel implements Serializable {

    private String movie_id;
    private String movie_name;
    private String movie_lang;
    private String movie_releaseDate;
    private String movie_background_image;
    private String movie_thumbnail_image;
    private String synopsis;
    private String genre;
    private String director;
    private String producation;
    private String cast;
    private String music;
    private ArrayList<TabListModel> tabList;
    private ArrayList<TrackAudioModel> fullMovieList;


    public MovieInfoModel(JSONObject object) throws JSONException {

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

        if (object.has("synopsis"))
            setSynopsis(object.getString("synopsis"));

        if (object.has("genres")) {
            JSONArray gArray = object.getJSONArray("genres");
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < gArray.length(); i++) {
                value.append(gArray.get(i));

                if (i != gArray.length() - 1)
                    value.append(",");
            }
            setGenre(value.toString());
        }

        if (object.has("directors")) {
            JSONArray dArray = object.getJSONArray("directors");
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < dArray.length(); i++) {
                value.append(dArray.get(i));

                if (i != dArray.length() - 1)
                    value.append(",");
            }
            setDirector(value.toString());
        }


        if (object.has("productionHouses")) {
            JSONArray pArray = object.getJSONArray("productionHouses");
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < pArray.length(); i++) {
                value.append(pArray.get(i));


                if (i != pArray.length() - 1)
                    value.append(",");
            }
            setProducation(value.toString());
        }


        if (object.has("cast")) {
            JSONArray cArray = object.getJSONArray("cast");
            StringBuilder value = new StringBuilder(100);
            for (int i = 0; i < cArray.length(); i++) {
                value.append(cArray.get(i));

                if (i != cArray.length() - 1)
                    value.append(",");
            }
            setCast(value.toString());
        }
        if (object.has("audioCompanies")) {
            JSONArray mArray = object.getJSONArray("audioCompanies");
            StringBuilder value = new StringBuilder(100);
            for (int i = 0; i < mArray.length(); i++) {
                value.append(mArray.get(i));

                if (i != mArray.length() - 1)
                    value.append(",");
            }
            setMusic(value.toString());
        }

        if (object.has("audios")) {
            JSONArray audioArray = object.getJSONArray("audios");
            ArrayList<TabListModel> list = new ArrayList<>();
            for (int i = 0; i < audioArray.length(); i++) {
                JSONObject tabObject = audioArray.getJSONObject(i);
                TabListModel model = new TabListModel(tabObject);
                list.add(model);
            }
            setTabList(list);
        }


        if (object.has("fullMovieAudio")) {
            JSONObject fullAudio = object.getJSONObject("fullMovieAudio");
            ArrayList<TrackAudioModel> list = new ArrayList<>();
            TrackAudioModel model = new TrackAudioModel(fullAudio);
            list.add(model);
            setFullMovieList(list);
        }
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducation() {
        return producation;
    }

    public void setProducation(String producation) {
        this.producation = producation;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public ArrayList<TabListModel> getTabList() {
        return tabList;
    }

    public void setTabList(ArrayList<TabListModel> tabList) {
        this.tabList = tabList;
    }

    public ArrayList<TrackAudioModel> getFullMovieList() {
        return fullMovieList;
    }

    public void setFullMovieList(ArrayList<TrackAudioModel> fullMovieList) {
        this.fullMovieList = fullMovieList;
    }
}
