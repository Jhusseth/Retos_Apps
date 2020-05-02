package com.stk.reto1_maps;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener, View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CODE = 111;
    private LocationManager manager;

    private double longitudeGPS, latitudeGPS;

    private FrameMap frame;

    private Button btnSet,btnInfo,btnErase,btnAdd;

    private MarkerOptions markerPos;

    private LatLng pos;
    private LatLng myPos;

    private ArrayList<MarkerOptions> arrayMarkers;

    Dialog customDialog = null;


    public MarkerOptions markerSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        frame = FrameMap.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, frame)
                .commit();

        frame.getMapAsync(this);

        requestPermissions();

        btnSet=findViewById(R.id.setPos);
        btnSet.setOnClickListener(this);

        btnErase=findViewById(R.id.eraseM);
        btnErase.setOnClickListener(this);


        btnInfo=findViewById(R.id.InfoPos);
        btnInfo.setOnClickListener(this);

        btnAdd = findViewById(R.id.addM);
        btnAdd.setOnClickListener(this);

        pos = new LatLng(3.343235, -76.529617);

        arrayMarkers= new ArrayList<>();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markerPos = new MarkerOptions();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setCompassEnabled(true);


        // Add a marker in my location and move the camera
        LatLng cali = new LatLng(3.343235, -76.529617);
        markerPos.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mMap.addMarker(markerPos.position(cali).title("Marker in Cali"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cali));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(3.343235, -76.529617), 15));

        //arrayMarkers.add(markerPos);


    }


    @Override
    public void onMapClick(LatLng latLng) {
        pos = latLng;


        markerSelect = new MarkerOptions();
        markerSelect.icon(BitmapDescriptorFactory.fromResource(R.drawable.select));
        mMap.addMarker(markerSelect.position(latLng));
    }


    public void requestPermissions(){
        int GPS_permissions = ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int locationPermissions = ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int network = ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.INTERNET);


        if(GPS_permissions==PackageManager.PERMISSION_GRANTED||locationPermissions==PackageManager.PERMISSION_GRANTED||
        network==PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},LOCATION_REQUEST_CODE);
                Toast.makeText(MapsActivity.this, "Permits Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.KEY_LOCATION_CHANGED);
    }

    public void toggleBestUpdates(View view) {
        if (!checkLocation())
            return;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
        manager.removeUpdates(locationListenerBest);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = manager.getBestProvider(criteria, true);
        if (provider != null) {
            manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(provider, 1000, 5, locationListenerBest);
           // manager.requestLocationUpdates(provider, 2 * 20 * 1000, 10, locationListenerBest);
            Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
        }

    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LatLng act = new LatLng(latitudeGPS,longitudeGPS);
                    myPos=act;
                    markerPos.icon(BitmapDescriptorFactory.fromResource(R.drawable.act));
                    markerPos.position(act);
                    mMap.addMarker(markerPos.title("Your location"))
                    .setSnippet("Location User");
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(act));
                    Toast.makeText(MapsActivity.this, "Best Provider update", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    public void dialogAddMerker(View view)
    {
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(this,R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_add_marker);

        final EditText name = (EditText) customDialog.findViewById(R.id.name);

        final EditText decs = (EditText) customDialog.findViewById(R.id.desc);

        ((Button) customDialog.findViewById(R.id.add_marker)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if(pos!=null){
                    MarkerOptions m = new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.select))
                            .title(name.getText().toString())
                            .snippet(decs.getText().toString());

                    arrayMarkers.add(m);
                    mMap.addMarker(m);
                }
                else{
                    Toast.makeText(MapsActivity.this, "Select Positions", Toast.LENGTH_SHORT).show();
                }
                customDialog.dismiss();
                Toast.makeText(MapsActivity.this, "Marker Added", Toast.LENGTH_SHORT).show();

            }
        });

        ((Button) customDialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();
                Toast.makeText(MapsActivity.this, R.string.cancel, Toast.LENGTH_SHORT).show();

            }
        });

        customDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(btnSet==v){
            mMap.clear();
            mMap.setMyLocationEnabled(true);
            toggleBestUpdates(v);
        }
        else if(btnInfo==v){
            repaintMarkers();
            try {
                int i = Math.round(calculateDistances()[1]);
                float distance = calculateDistances()[0];
                MarkerOptions m = arrayMarkers.get(i);

              if(distance<3.0){
                  btnInfo.setText("Se encuentra en: " +m.getTitle().toString());
              }
              else {
                  btnInfo.setText("El lugar mas cercano es: " + m.getTitle());
                  String str = m.getTitle() + " " + m.getSnippet() + " se encuentra a " + distance + " Metros";
                  m.visible(true);
                  m.icon(null);
                  MarkerOptions mark = new MarkerOptions().position(pos)
                          .icon(BitmapDescriptorFactory.fromResource(R.drawable.near))
                          .title(m.getTitle())
                          .snippet(str);

                  mMap.addMarker(mark);
              }

            }catch(Exception e){
                Toast.makeText(MapsActivity.this, "Add Marker First", Toast.LENGTH_SHORT).show();
            }
        }

        else if(btnAdd==v){
            dialogAddMerker(v);
        }
        else if(btnErase==v){
            mMap.clear();
            arrayMarkers = new ArrayList<>();
            btnInfo.setText("POSICCION");
            Toast.makeText(MapsActivity.this, "ERASE", Toast.LENGTH_SHORT).show();
        }
    }


    public Float[] calculateDistances(){

        Float[] distances = new Float[arrayMarkers.size()];
        Float[] result = new Float[2];
        float index =0;
        Location la = new Location("Punto A");
        la.setLatitude(myPos.latitude);
        la.setLongitude(myPos.longitude);
        for (int i = 0; i < arrayMarkers.size(); i++) {
            Location lb = new Location("Punto B");
            lb.setLatitude(arrayMarkers.get(i).getPosition().latitude);
            lb.setLongitude(arrayMarkers.get(i).getPosition().longitude);

            if (!(distances.length>arrayMarkers.size())) {
                float ab = la.distanceTo(lb);
                distances[i] = ab;
            }
        }

        float max, min;

        min=max=distances[0];

        for(int i = 0; i < distances.length; i++)
        {
            if(min>distances[i])
            {
                min=distances[i];
                index =i;
            }
            if(max<distances[i])
            {
                max=distances[i];
            }
        }

        result[0]=min;
        result[1]=index;

        return  result;
    }


    public void repaintMarkers(){
        mMap.clear();
        mMap.addMarker(markerPos);
        for(MarkerOptions m:arrayMarkers){
            mMap.addMarker(m)
            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.near));
        }
    }

    public void dialogChangeMap(View view)
    {
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(this,R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.type_maps);

       final Spinner sp = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Google_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        ((Button) customDialog.findViewById(R.id.add_marker)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {

                String spt = sp.getSelectedItem().toString();
                customDialog.dismiss();
                Toast.makeText(MapsActivity.this, "Marker Added", Toast.LENGTH_SHORT).show();

            }
        });

        customDialog.show();
    }



}
