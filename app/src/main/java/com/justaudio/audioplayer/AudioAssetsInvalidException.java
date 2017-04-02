package com.justaudio.audioplayer;

/**
 *
 * Created by Joielechong VIDYA
 */

public class AudioAssetsInvalidException extends Exception {
    public AudioAssetsInvalidException(String path){
        super("The file name is not a valid Assets file: " + path);
    }
}
