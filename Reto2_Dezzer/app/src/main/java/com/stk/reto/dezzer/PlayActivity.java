package com.stk.reto.dezzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<String> dataTrack;
    private Button btnPlay;

    private Bitmap bm;

    private Runnable runnable;

    private ImageView imageTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_activity);

        setTitle("Play Song");

        Bundle parameters = this.getIntent().getExtras();
        dataTrack = parameters.getStringArrayList("songTrack");

        imageTrack = findViewById(R.id.iconTrack);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);

        new MyTask().execute();
        dataTrack();

    }

    public void dataTrack(){

        runnable= new Runnable() {
            @Override
            public void run() {
                imageTrack.setImageBitmap(bm);
            }
        };

        TextView nameTrack = findViewById(R.id.nameTrack);
        nameTrack.setText(dataTrack.get(1));
        TextView artistTrack = findViewById(R.id.artistTrack);
        artistTrack.setText(dataTrack.get(2));
        TextView albumTrack = findViewById(R.id.albumTrack);
        albumTrack.setText(dataTrack.get(3));
        TextView duration = findViewById(R.id.duration);
        int timeInt = Integer.parseInt(dataTrack.get(4));
        int horas = (timeInt / 3600);
        int minutos = ((timeInt-horas*3600)/60);
        int segundos = timeInt-(horas*3600+minutos*60);

        duration.setText(minutos + ":" + segundos);

    }

    private class MyTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL imageUrl = null;
            try {
                imageUrl = new URL(dataTrack.get(0));
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                bm = BitmapFactory.decodeStream(conn.getInputStream());

                runOnUiThread(runnable);

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


    @Override
    public void onClick(View v) {
        if(btnPlay==v){
            try{
                Bundle paremeter = new Bundle();
                paremeter.putString("page",dataTrack.get(5));
                Toast.makeText(this,dataTrack.get(5), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(),ActivityWebView.class);
                i.putExtras(paremeter);
                startActivity(i);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
