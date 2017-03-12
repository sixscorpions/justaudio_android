package com.justaudio.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pavan
 */

public class NamesModel implements Serializable{

    private String tabName;
    private ArrayList<String> tabsnameList;


    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public ArrayList<String> getTabsnameList() {
        return tabsnameList;
    }

    public void setTabsnameList(ArrayList<String> tabsnameList) {
        this.tabsnameList = tabsnameList;
    }
}
