package com.justaudio.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ${VIDYA}
 */

public class TabListModel implements Serializable {

    private String tabName;
    private ArrayList<TrackAudioModel> audioList;

    TabListModel(JSONObject object) throws JSONException {
        if (object.has("audioType"))
            setTabName(object.getString("audioType"));
        if (object.has("audios")) {
            JSONArray array = object.getJSONArray("audios");
            ArrayList<TrackAudioModel> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject audioObject = array.getJSONObject(i);
                TrackAudioModel model = new TrackAudioModel(audioObject);
                list.add(model);
            }
            setAudioList(list);
        }

    }

    public ArrayList<TrackAudioModel> getAudioList() {
        return audioList;
    }

    public void setAudioList(ArrayList<TrackAudioModel> audioList) {
        this.audioList = audioList;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
