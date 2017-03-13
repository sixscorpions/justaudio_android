package com.justaudio.dto;

import android.support.annotation.RawRes;

import com.justaudio.audioplayer.Origin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

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
    private ArrayList<NamesModelNew> tabsName;


    TrackAudioModel(JSONObject object, int i) throws JSONException {


        setOrigin(Origin.URL);


        if (object.has("id"))
            setId(object.getInt("id"));

        if (object.has("name"))
            setTitle(object.getString("name"));

        if (object.has("path"))
            setPath(object.getString("path"));

        if (object.has("thumbnail_image"))
            setThumbnail_image(object.getString("thumbnail_image"));







       /* tabsName = new ArrayList<>();


        if (object.has("Movie Parts")) {

            ArrayList<TrackAudioModel> movie_Partslist = new ArrayList<>();
            NamesModelNew model = new NamesModelNew();
            model.setTabName("Movie Parts");

            JSONArray categoryList = object.getJSONArray("Movie Parts");
            for (int i = 0; i < categoryList.length(); i++) {
                TrackAudioModel dramalist = new TrackAudioModel();
                JSONObject dataObj = categoryList.getJSONObject(i);
                dramalist.setId(i);

                if (dataObj.has("name"))
                    dramalist.setTitle(dataObj.getString("name"));

                if (dataObj.has("path"))
                    dramalist.setPath(dataObj.getString("path"));

                if (dataObj.has("thumbnail_image"))
                    dramalist.setThumbnail_image(dataObj.getString("thumbnail_image"));

                dramalist.setOrigin(Origin.URL);

                movie_Partslist.add(dramalist);

            }
            model.setTabsnameList(movie_Partslist);
            tabsName.add(model);
        }

        if (object.has("Artists")) {

            ArrayList<TrackAudioModel> artistslist = new ArrayList<>();
            NamesModelNew model = new NamesModelNew();
            model.setTabName("Artists");

            JSONArray categoryList = object.getJSONArray("Artists");
            for (int i = 0; i < categoryList.length(); i++) {
                TrackAudioModel dramalist = new TrackAudioModel();
                JSONObject dataObj = categoryList.getJSONObject(i);

                dramalist.setId(i);
                if (dataObj.has("name"))
                    dramalist.setTitle(dataObj.getString("name"));

                if (dataObj.has("path"))
                    dramalist.setPath(dataObj.getString("path"));

                if (dataObj.has("thumbnail_image"))
                    dramalist.setThumbnail_image(dataObj.getString("thumbnail_image"));

                dramalist.setOrigin(Origin.URL);

                artistslist.add(dramalist);

            }
            model.setTabsnameList(artistslist);
            tabsName.add(model);

        }

        if (object.has("Comedy")) {

            ArrayList<TrackAudioModel> comedylist = new ArrayList<>();
            NamesModelNew model = new NamesModelNew();
            model.setTabName("Comedy");

            JSONArray categoryList = object.getJSONArray("Comedy");
            for (int i = 0; i < categoryList.length(); i++) {
                TrackAudioModel dramalist = new TrackAudioModel();
                JSONObject dataObj = categoryList.getJSONObject(i);

                dramalist.setId(i);

                if (dataObj.has("name"))
                    dramalist.setTitle(dataObj.getString("name"));

                if (dataObj.has("path"))
                    dramalist.setPath(dataObj.getString("path"));

                if (dataObj.has("thumbnail_image"))
                    dramalist.setThumbnail_image(dataObj.getString("thumbnail_image"));

                dramalist.setOrigin(Origin.URL);

                comedylist.add(dramalist);

            }
            model.setTabsnameList(comedylist);
            tabsName.add(model);
        }

        if (object.has("Drama")) {
            ArrayList<TrackAudioModel> dramalist1 = new ArrayList<>();
            NamesModelNew model = new NamesModelNew();
            model.setTabName("Drama");

            JSONArray categoryList = object.getJSONArray("Drama");
            for (int i = 0; i < categoryList.length(); i++) {
                TrackAudioModel dramalist = new TrackAudioModel();
                JSONObject dataObj = categoryList.getJSONObject(i);

                dramalist.setId(i);

                if (dataObj.has("name"))
                    dramalist.setTitle(dataObj.getString("name"));

                if (dataObj.has("path"))
                    dramalist.setPath(dataObj.getString("path"));

                if (dataObj.has("thumbnail_image"))
                    dramalist.setThumbnail_image(dataObj.getString("thumbnail_image"));

                dramalist.setOrigin(Origin.URL);

                dramalist1.add(dramalist);

            }
            model.setTabsnameList(dramalist1);
            tabsName.add(model);
        }

        if (object.has("Movie Audio")) {

            ArrayList<TrackAudioModel> movie_Audiolist = new ArrayList<>();
            NamesModelNew model = new NamesModelNew();
            model.setTabName("BGM");

            JSONArray categoryList = object.getJSONArray("Movie Audio");
            for (int i = 0; i < categoryList.length(); i++) {

                TrackAudioModel dramalist = new TrackAudioModel();
                JSONObject dataObj = categoryList.getJSONObject(i);

                dramalist.setId(i);

                if (dataObj.has("name"))
                    dramalist.setTitle(dataObj.getString("name"));

                if (dataObj.has("path"))
                    dramalist.setPath(dataObj.getString("path"));

                if (dataObj.has("thumbnail_image"))
                    dramalist.setThumbnail_image(dataObj.getString("thumbnail_image"));

                dramalist.setOrigin(Origin.URL);

                movie_Audiolist.add(dramalist);

            }
            model.setTabsnameList(movie_Audiolist);
            tabsName.add(model);
        }*/


    }


    /* public TrackAudioModel(String title, String path) {
             this.title = title;
             this.path = path;
             this.origin = Origin.URL;
         }*/
    public ArrayList<NamesModelNew> getTabsName() {
        return tabsName;
    }

    public void setTabsName(ArrayList<NamesModelNew> tabsName) {
        this.tabsName = tabsName;
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