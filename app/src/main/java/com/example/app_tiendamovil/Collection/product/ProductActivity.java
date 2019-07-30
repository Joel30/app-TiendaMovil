package com.example.app_tiendamovil.Collection.product;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProductActivity extends AppCompatActivity {

    private TextView txt_price, txt_quantity, txt_category, txt_description;
    private ImageView img_product;
    private String id_producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_product);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadComponents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void loadComponents() {
        id_producto = getIntent().getStringExtra("id");
        txt_price = (TextView) findViewById(R.id.txt_show_price_product);
        txt_quantity = (TextView) findViewById(R.id.txt_show_quantity_product);
        txt_category = (TextView) findViewById(R.id.txt_show_category_product);
        txt_description = (TextView) findViewById(R.id.txt_show_description_product);

        img_product = (ImageView) findViewById(R.id.img_show_product);

        getData();
    }

    // Cargar datos del Producto seleccionado
    private void getData (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PRODUCTO_SERVICE + id_producto, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("product");
                    JSONObject obj = data.getJSONObject(0);
                    txt_price.setText(obj.getString("price") + " Bs.");
                    getSupportActionBar().setTitle(obj.getString("title"));
                    txt_quantity.setText("Cantidad: " + obj.getString("quantity"));
                    txt_category.setText(obj.getString("category"));
                    txt_description.setText(obj.getString("description"));
                    String image = obj.getString("image");
                    Glide.with (ProductActivity.this).load (Utils.HOST + image).into(img_product);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(ProductActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menu Editar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = this.getMenuInflater();
        inflate.inflate(R.menu.update_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_edit_profile){
            Intent intent = new Intent(this, EditProductActivity.class);
            intent.putExtra("id", id_producto);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
