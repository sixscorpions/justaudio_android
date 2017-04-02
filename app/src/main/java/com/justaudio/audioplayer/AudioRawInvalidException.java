package com.justaudio.audioplayer;

/**
 *
 * Created by VIDYA
 */
public class AudioRawInvalidException extends Exception {
    public AudioRawInvalidException(String rawId){
        super("Not a valid raw file id: " + rawId);
    }
}
