package com.example.app_tiendamovil.Collection.product;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Seller.SellerActivity;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.message.ChatActivity;
import com.example.app_tiendamovil.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProductSellerActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private ImageView img_product, img_profile;
    private TextView txt_price, txt_name, txt_date, txt_description, txt_quantity, txt_img;
    private MapView map;
    private GoogleMap mMap;
    private String id_producto, id_people, lat, lon, my_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_product_seller);

        //getSupportActionBar().setTitle("Images");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadComponents(savedInstanceState);

    }
    private void loadComponents(Bundle savedInstanceState) {
        id_producto = getIntent().getStringExtra("id");
        id_people = getIntent().getStringExtra("idpeople");

        img_product = (ImageView) findViewById(R.id.img_seller_product);
        img_profile = (ImageView) findViewById(R.id.small_img_seller_product);
        txt_price = (TextView) findViewById(R.id.txt_price_seller_product);
        txt_date = (TextView) findViewById(R.id.txt_date_seller_product);
        txt_name = (TextView) findViewById(R.id.txt_name_seller_product);
        txt_description = (TextView) findViewById(R.id.txt_description_seller_product);
        txt_quantity = (TextView) findViewById(R.id.txt_quantity_seller_product);
        txt_img = (TextView) findViewById(R.id.txt_img_seller_product);
        getData();
        getDataPersona(savedInstanceState);
        getMyData();

        Button btnSeller = (Button) findViewById(R.id.btn_go_seller);
        btnSeller.setOnClickListener(this);

        Button btnChat = (Button) findViewById(R.id.btn_chat_seller_product);
        btnChat.setOnClickListener(this);
    }

        // Regresar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

        // optener datos del vendedor
    private void getDataPersona (final Bundle sIS){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + id_people, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);
                    txt_img.setText(obj.getString("name"));
                    String img = obj.getString("avatar");
                    lat = obj.getString("lat");
                    lon = obj.getString("lon");
                    if (!img.equals("")){
                        Glide.with (ProductSellerActivity.this).load(Utils.HOST + img).circleCrop().into(img_profile);
                    }
                        //Crear el mapa con la ubicacion del vendedor
                    map = findViewById(R.id.map_seller_product);
                    map.onCreate(sIS);
                    map.onResume();
                    MapsInitializer.initialize(ProductSellerActivity.this);
                    map.getMapAsync(ProductSellerActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(ProductSellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getData (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PRODUCTO_SERVICE + id_producto, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("product");
                    JSONObject obj = data.getJSONObject(0);
                    txt_date.setText(obj.getString("updateDate").substring(0,10));
                    txt_price.setText(obj.getString("price") + " Bs.");
                    txt_quantity.setText(obj.getString("quantity"));
                    txt_description.setText(obj.getString("description"));
                    txt_name.setText(obj.getString("title"));
                    String image = obj.getString("image");
                    getSupportActionBar().setTitle(obj.getString("title"));
                    Glide.with (ProductSellerActivity.this).load (Utils.HOST + image).into(img_product);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(ProductSellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMyData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);
                    //String img = obj.getString("avatar");
                    my_name = obj.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(ProductSellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


        //Mapa con la ubicacion del vendedor
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng street = new LatLng(Float.parseFloat(lat), Float.parseFloat(lon));
        mMap.addMarker(new MarkerOptions().position(street).title("Lugar").zIndex(16).draggable(true));
        mMap.setMinZoomPreference(16);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(street));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go_seller){
            Intent intent = new Intent(this, SellerActivity.class);
            intent.putExtra("idpeople", id_people);
            startActivity(intent);
        } if (v.getId() == R.id.btn_chat_seller_product){
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("id", id_people);
            intent.putExtra("name", my_name);
            startActivity(intent);
        }

    }
}
