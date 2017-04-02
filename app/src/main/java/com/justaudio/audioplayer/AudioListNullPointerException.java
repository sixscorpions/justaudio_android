package com.justaudio.audioplayer;

/**
 * Created by jean VIDYA
 */

public class AudioListNullPointerException extends NullPointerException {
    public AudioListNullPointerException(){
        super("The playlist is empty or null");
    }
}
