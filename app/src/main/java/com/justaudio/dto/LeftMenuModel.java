package com.justaudio.dto;

/**
 * Created by user
 */

public class LeftMenuModel {
    private String menuTitle;

    public LeftMenuModel(String str) {
        this.menuTitle = str;
    }


    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }
}
