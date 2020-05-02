package com.stk.reto.dezzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayList {

    private String name;
    private  String nameUser;
    private int cantSongs;
    private String img;
    private int fans;
    private String tracks;

    private String description;

    private Bitmap bm;

    public PlayList(String name,String nameUser,int cantSongs,int fans,String img,String description,String tracks){
        this.name = name;
        this.nameUser= nameUser;
        this.cantSongs=cantSongs;
        this.img=img;
        this.fans = fans;
        this.tracks = tracks;
        this.description= description;

        new MyTask().execute();
    }

    public String getName(){
        return name;
    }


    public String getNameUser(){
        return nameUser;
    }

    public int getCantSongs(){
        return cantSongs;
    }

    public int getFans(){ return fans; }

    public String getDescription(){ return  description; }


    public Bitmap getBm(){ return bm;}

    public String getTracks(){ return tracks; }

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
