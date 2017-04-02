package com.justaudio.audioplayer;

/**
 *
 * Created by VIDYA
 */

public class AudioFilePathInvalidException extends Exception {
    public AudioFilePathInvalidException(String url){
        super("The file path is not a valid path: " + url +
        "\n" +
        "Have you add File Access Permission?");
    }
}
