package com.example.app_tiendamovil;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.profile.EditProfileActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{

    private MapView map;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private TextView txt_location;
    private TextInputLayout til_location;
    private Button btn_next;
    private LatLng position;
    private String lat = "-19.5759897", lon = "-65.7581637", streetI;
    private int epa = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //getSupportActionBar().setTitle("Images");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        map = findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.onResume();
        MapsInitializer.initialize(this);
        map.getMapAsync(this);

        loadComponents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void loadComponents() {
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        txt_location = (TextView) findViewById(R.id.txt_location);
        til_location = (TextInputLayout) findViewById(R.id.textInputLayout_map_location);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        //String location = getIntent().getStringExtra("location");
        String epa_lat = getIntent().getStringExtra("lat");
        String epa_lon = getIntent().getStringExtra("lon");
        String epa_street = getIntent().getStringExtra("street");
        if (epa_lat != null){
            lat = epa_lat;
            lon = epa_lon;
            epa = 1;
            txt_location.setText(epa_street);
        }
        else {
            Toast.makeText(this, "MAP GG", Toast.LENGTH_SHORT).show();
        }

    }

        // Mostrar el mapa enfocado en una Area determinada
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //@-19.5759897,-65.7581637,16z
        // Add a marker in Sydney and move the camera
        final LatLng street = new LatLng(Float.parseFloat(lat), Float.parseFloat(lon));
        position = street;
        mMap.addMarker(new MarkerOptions().position(street).title("Lugar").zIndex(16).draggable(true));
        mMap.setMinZoomPreference(16);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(street));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                position = marker.getPosition();
                streetI = getStreet(marker.getPosition().latitude, marker.getPosition().longitude);
                if (streetI.equals("null")){
                    txt_location.setText("");
                }else {
                    txt_location.setText(streetI);
                }
            }
        });
    }

        //Obtener el nombre de un determinado lugar
    public String getStreet(Double lat, double lon){
        List<Address> address;
        String result = "";
        try {
            address = geocoder.getFromLocation(lat, lon, 1);
            result += address.get(0).getThoroughfare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

        // Enviar ubicacion a la actividad (EditProfileActivity o RegisterActivity)
    private void sendData() {
        lat = String.valueOf(position.latitude);
        lon = String.valueOf(position.longitude);
        /*Toast.makeText(this, lat, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, lon, Toast.LENGTH_SHORT).show();*/
        Intent lat_lon;
        if(epa == 1){
            lat_lon = new Intent(this, EditProfileActivity.class);
        } else {
            lat_lon = new Intent(this, RegisterActivity.class);
        }
        lat_lon.putExtra("lat", lat);
        lat_lon.putExtra("lon", lon);
        lat_lon.putExtra("street", streetI);
        startActivity(lat_lon);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next){
            if (validateMap()){
                sendData();
            }
        }
    }


    private Boolean validateMap(){
        if (txt_location.getText().toString().trim().isEmpty()){
            til_location.setError(getString(R.string.err_map_location));
            return false;
        } else {
            til_location.setErrorEnabled(false);
        }
        return  true;
    }
}
