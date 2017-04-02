package com.justaudio.audioplayer;

/**
 * Created by VIDYA
 */

public class AudioUrlInvalidException extends IllegalStateException {
    public AudioUrlInvalidException(String url){
        super("The url does not appear valid: " + url);
    }
}
