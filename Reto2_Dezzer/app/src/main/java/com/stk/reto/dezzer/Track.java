package com.stk.reto.dezzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Track {

    private String name;
    private String album;
    private String artist;
    private int duration;
    private String date;
    private String song;
    private String img;

    private Bitmap bm;


    public Track(String name, String album, String artist, int duration, String date, String song, String img) {
        this.name = name;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.date = date;
        this.song = song;
        this.img = img;

        bm=null;

        new MyTask().execute();
    }


    public String getName() {
        return name;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm){
        this.bm= bm;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getSong() {
        return song;
    }

    public String getImg() {
        return img;
    }

    private class MyTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL imageUrl = null;
            try {
                imageUrl = new URL(img);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                bm = BitmapFactory.decodeStream(conn.getInputStream());
                setBm(bm);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
