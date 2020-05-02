package com.stk.reto.dezzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityPlayList extends AppCompatActivity implements AdapterList.OnPlayListListener, View.OnClickListener{

    private RequestQueue queue;

    private List<PlayList> playList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private AdapterList adapter;

    private TextView state ;

    private EditText search;
    private ImageButton btn_search;

    private String searchPlayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity_main);

        setTitle("Search PlayList");

        queue = Volley.newRequestQueue(this);

        playList = new ArrayList<>();

        recyclerView = findViewById(R.id.listPlay);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipeAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList();
            }
        });

        state = findViewById(R.id.progress);

        search = findViewById(R.id.textSearch);
        btn_search = findViewById(R.id.btnSearch);
        btn_search.setOnClickListener(this);

        searchPlayList = search.getText().toString();

        new MyTask().execute();
    }

    public void dataPlayList(String searchList){
        if(searchPlayList.isEmpty()){
            searchPlayList="https://api.deezer.com/search/playlist?q="+ "Pergolizzi";
        }
        else{
            searchPlayList="https://api.deezer.com/search/playlist?q="+ searchList;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchPlayList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray mJSONArray = response.getJSONArray("data");

                    for(int i =0;i<mJSONArray.length();i++){
                        JSONObject mJSONObject = mJSONArray.getJSONObject(i);
                        String title = mJSONObject.getString("title");
                        int cantSongs = Integer.parseInt(mJSONObject.getString("nb_tracks"));
                        int fans =0;
                        //Integer.parseInt(mJSONObject.getString("fans"));
                        String img = mJSONObject.getString("picture_big");
                        String tracks= mJSONObject.getString("tracklist");
                        JSONObject creator = mJSONObject.getJSONObject("user");
                        String user = creator.getString("name");
                        String describe = mJSONObject.getString("creation_date");
                        PlayList p = new PlayList(title,user,cantSongs,fans,img,describe,tracks);
                        playList.add(p);
                        setupPlayList();
                        recyclerView.getAdapter().notifyDataSetChanged();

                    }

                }catch(JSONException e){
                    Toast.makeText(ActivityPlayList.this,e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    private void setupPlayList() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapter = new AdapterList(playList,this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onPlayListClick(int position) {
        Intent intent = new Intent(this, ActivityTracks.class);

        PlayList pl = playList.get(position);

        ArrayList<String> data = new ArrayList<>();
        data.add(pl.getName());
        data.add(pl.getDescription());
        data.add(""+ pl.getCantSongs());
        data.add(""+pl.getFans());
        data.add(pl.getTracks());

        intent.putExtra("dataPlaylist", data);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if(btn_search==v){
            Toast.makeText(this,"Searching: " + search.getText().toString(),Toast.LENGTH_LONG).show();
            playList = new ArrayList<>();
            dataPlayList(search.getText().toString());
            adapter.notifyDataSetChanged();
        }
    }

    private class MyTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            state.setText(values[0] + "%");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dataPlayList(search.getText().toString());
            state.setText("");
        }
    }

    private class updateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateList() {
        new updateTask().execute();
    }

}
