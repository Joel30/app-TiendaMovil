package com.example.app_tiendamovil.Collection.Seller;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SellerActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private TextView txt_name, txt_age, txt_quantity, txt_followers, txt_phone;
    private ImageView img_seller;
    private RatingBar rb_seller, rb_buyer;
    private ImageButton imageButton;
    private MapView map;
    private GoogleMap mMap;
    private String  id, lat, lon , my_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_seller);

        loadComponents(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void loadComponents(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("idpeople");

        txt_name = (TextView) findViewById(R.id.p_name_seller);
        txt_quantity = (TextView) findViewById(R.id.p_quantity_seller);
        //txt_followers = (TextView) findViewById(R.id.p_followers_seller);
        txt_phone = (TextView) findViewById(R.id.p_phone_seller);

        rb_buyer = (RatingBar) findViewById(R.id.rb_buyer);
        rb_seller = (RatingBar) findViewById(R.id.rb_seller);

        img_seller = (ImageView) findViewById(R.id.p_img_seller);

        getDataPersona(savedInstanceState);
        getMyData();
        getDataFollowers();
        getDataProduct();
        Button btn_ps = (Button) findViewById(R.id.p_btn_productseller);
        btn_ps.setOnClickListener(this);
        Button btn_chat = (Button) findViewById(R.id.p_btn_chat_seller);
        btn_chat.setOnClickListener(this);
        imageButton = (ImageButton) findViewById(R.id.ibtn_follow_seller);
        imageButton.setOnClickListener(this);
    }

    // optener datos del vendedor
    private void getDataPersona (final Bundle sIS){
        AsyncHttpClient client = new AsyncHttpClient();
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        client.get(Utils.ID_PERSONA_SERVICE + id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);
                    getSupportActionBar().setTitle(obj.getString("name"));
                    String img = obj.getString("avatar");
                    lat = obj.getString("lat");
                    lon = obj.getString("lon");
                    txt_name.setText(obj.getString("name"));

                    txt_phone.setText(obj.getString("phone"));
                    //txt_age.setText(obj.getString("registerDate").substring(0,10));

                    //txt_quantity.setText(obj.getString("idbuyer"));
                    //txt_followers.setText(obj.getString(""));
                    Toast.makeText(SellerActivity.this, "1", Toast.LENGTH_SHORT).show();
                    JSONArray seller = obj.getJSONArray("qSeller");
                    JSONArray buyer = obj.getJSONArray("qBuyer");
                    Toast.makeText(SellerActivity.this, "2", Toast.LENGTH_SHORT).show();

                    float b = (float) seller.getInt(1)/seller.getInt(0);
                    float s = (float) buyer.getInt(1)/buyer.getInt(0);
                    rb_buyer.setRating(b);
                    rb_seller.setRating(s);
                    if (!img.equals("")){
                        Glide.with (SellerActivity.this).load(Utils.HOST + img).into(img_seller);
                    }
                    //Crear el mapa con la ubicacion del vendedor
                    map = findViewById(R.id.p_map_seller);
                    map.onCreate(sIS);
                    map.onResume();
                    MapsInitializer.initialize(SellerActivity.this);
                    map.getMapAsync(SellerActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(SellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

        //Obtener la cantidad de seguidores
    private void getDataFollowers(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_SEGUIDOS_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("seguidos");
                    JSONObject obj = data.getJSONObject(0);
                    JSONArray idseller = obj.getJSONArray("idseller");
                    for (int i = 0; i < idseller.length(); i++){
                        if (idseller.getString(i).equals(id)){
                            imageButton.setVisibility(View.INVISIBLE);
                        }
                    }
                    //txt_followers.setText(String.valueOf(idseller.length()));
                    //getDataProducto();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(SellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                //txt_followers.setText("0");
            }
        });
    }
        private void getDataProduct(){
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(Utils.PRODUCTO_SERVICE, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray data = response.getJSONArray("product");
                        Toast.makeText(SellerActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                        int cont = 0;
                        for (int i = 0; i < data.length(); i++){
                            JSONObject obj = data.getJSONObject(i);
                            if (obj.getString("idpeople").equals(id)){
                                cont ++;
                            }
                        }
                        txt_quantity.setText(String.valueOf(cont));
                        //getDataProducto();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                    Toast.makeText(SellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    txt_followers.setText("0");
                }
            });
        }

    private void getMyData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);
                    my_name = obj.getString("name");
                    Toast.makeText(SellerActivity.this, "---------------------------------------", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(SellerActivity.this, "ERROR >:", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng street = new LatLng(Float.parseFloat(lat), Float.parseFloat(lon));
        mMap.addMarker(new MarkerOptions().position(street).title("Lugar").zIndex(16).draggable(true));
        mMap.setMinZoomPreference(16);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(street));
    }

    // Actualizar la lista de favoritos
    public void updateData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idseller", id);

        client.put(Utils.ID_SEGUIDOS_SERVICE + Utils.ID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("updateSeguir")){
                    Toast.makeText(SellerActivity.this, "Favorito Actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(SellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                if  (errorResponse.has("errVoid")){
                    insertData();
                } else {

                }
            }
        });
    }

    // Crear la lista de favoritos para la persona
    private void insertData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idpeople", Utils.ID);
        params.add("idseller", id);

        client.post(Utils.SEGUIDOS_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("seguir")){
                    Toast.makeText(SellerActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(SellerActivity.this, "Error insert", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.p_btn_productseller){
            Intent intent = new Intent(this, PSellerActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        if (v.getId() == R.id.p_btn_chat_seller){
            Intent intent = new Intent(this, ChatActivity.class);
            //intent.putExtra("id", id);
            intent.putExtra("id", id);

            //intent.putExtra("name", txt_name.getText());
            intent.putExtra("name", my_name);

            startActivity(intent);
        }
        if (v.getId() == R.id.ibtn_follow_seller){
            updateData();
            imageButton.setVisibility(View.INVISIBLE);
        }
    }
}
