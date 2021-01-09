package com.air.localmusicplayer;

public class MusicFiles {
    private String path ,title , artist ,album_ID ,duration,album;

    public MusicFiles(String path, String title, String artist, String album_ID, String duration,String album) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album_ID = album_ID;
        this.duration = duration;
        this.album = album;
    }

    public MusicFiles() {
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_ID() {
        return album_ID;
    }

    public void setAlbum_ID(String album_ID) {
        this.album_ID = album_ID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
