package com.stk.reto.dezzer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityTracks extends AppCompatActivity {

    private RequestQueue queue;

    private List<Track> trackList;

    private ArrayList<String> dataPlayList;

    private String URL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracklist_activity);

        setTitle("Play List");

        Bundle parameters = this.getIntent().getExtras();
        dataPlayList = parameters.getStringArrayList("dataPlaylist");

        queue = Volley.newRequestQueue(this);
        trackList = new ArrayList<>();

        postData();
        new MyTask().execute();
    }

    public void postData(){
        TextView title = findViewById(R.id.namePlayList);
        title.setText(dataPlayList.get(0));
        TextView describe = findViewById(R.id.descriptionPlayList);
        describe.setText(dataPlayList.get(1));
        TextView songs = findViewById(R.id.numSongs);
        songs.setText(""+ dataPlayList.get(2));
        TextView fans = findViewById(R.id.numFans);
        fans.setText("" + dataPlayList.get(3));

        URL = dataPlayList.get(4);
    }

    public void postList(String url){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String next="";

                try {
                    JSONArray mJSONArray = response.getJSONArray("data");

                    for(int i =0;i<mJSONArray.length();i++){
                        JSONObject mJSONObject = mJSONArray.getJSONObject(i);
                        String title = mJSONObject.getString("title");
                        JSONObject art = mJSONObject.getJSONObject("artist");
                        String artist = art.getString("name");
                        String img = art.getString("picture_medium");
                        JSONObject alb = mJSONObject.getJSONObject("album");
                        String album = alb.getString("title");
                        int duration = Integer.parseInt(mJSONObject.getString("duration"));
                        String date = dataPlayList.get(1).split(" ")[0].split("-")[0];
                        String song = mJSONObject.getString("link");


                        Track t = new Track(title,album,artist,duration,date,song,img);
                        trackList.add(t);
                        setupTrackList();
                    }

                    next = response.getString("next");

                }catch(JSONException e){
                    Toast.makeText(ActivityTracks.this,"Finished", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                if (!next.isEmpty()){
                    postList(next);
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    private void setupTrackList() {
        ArrayAdapter<Track> listAdapter = new ArrayAdapter<Track>(this,
                R.layout.item_title_tracks, trackList) {

            @Override
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                Track track = getItem(position);

                View view = convertView;
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.item_title_tracks, null);
                }


                TextView title = (TextView) view.findViewById(R.id.titleTrack);
                title.setText(track.getName());

                TextView user = (TextView) view.findViewById(R.id.artistTrack);
                user.setText(track.getArtist());

                TextView tracks = (TextView) view.findViewById(R.id.descriptionTrack);
                tracks.setText(track.getDate());

                ImageView image = (ImageView) view.findViewById(R.id.iconListTrack);
                image.setImageBitmap(track.getBm());


                return view;
            }
        };

        ListView listview = findViewById(R.id.trackPlay);
        listview.setAdapter(listAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final long id) {
                Track tk = trackList.get(position);

                Bundle paremeter = new Bundle();
                ArrayList<String> array = new ArrayList<>();
                array.add(tk.getImg());
                array.add(tk.getName());
                array.add(tk.getArtist());
                array.add(tk.getAlbum());
                array.add(""+ tk.getDuration());
                array.add(tk.getSong());

                paremeter.putStringArrayList("songTrack",array);

                Intent i = new Intent(view.getContext(), PlayActivity.class);
                i.putExtras(paremeter);
                startActivity(i);
            }
        });
        listAdapter.notifyDataSetChanged();
    }

    private class MyTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
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
            postList(URL);
        }
    }
}
