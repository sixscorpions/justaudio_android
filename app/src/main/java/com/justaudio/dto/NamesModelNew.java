package com.justaudio.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pavan
 */

public class NamesModelNew implements Serializable {

    private String tabName;
    private ArrayList<TrackAudioModel> tabsnameList;

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public ArrayList<TrackAudioModel> getTabsnameList() {
        return tabsnameList;
    }

    public void setTabsnameList(ArrayList<TrackAudioModel> tabsnameList) {
        this.tabsnameList = tabsnameList;
    }

}
