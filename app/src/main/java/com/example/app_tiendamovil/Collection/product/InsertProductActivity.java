package com.example.app_tiendamovil.Collection.product;

import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Images;
import com.example.app_tiendamovil.Collection.Permissions;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Main2Activity;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class InsertProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Images images;
    private Permissions permissions;

    private TextView txt_title, txt_price, txt_quantity, txt_description;
    private TextInputLayout til_title, til_price, til_quantity, til_description;
    private Spinner spinner;
    private String text, product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);
        getSupportActionBar().setTitle("Crear Producto");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadComponents();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void loadComponents() {
        images = new Images(this);

        txt_title = (TextView) findViewById(R.id.txt_title_insert_product);
        txt_price = (TextView) findViewById(R.id.txt_price_insert_product);
        txt_quantity = (TextView) findViewById(R.id.txt_quantity_insert_product);
        txt_description = (TextView) findViewById(R.id.txt_description_insert_product);

        til_title = (TextInputLayout) findViewById(R.id.til_title_insert_product);
        til_price = (TextInputLayout) findViewById(R.id.til_price_insert_product);
        til_quantity = (TextInputLayout) findViewById(R.id.til_quantity_insert_product);
        til_description = (TextInputLayout) findViewById(R.id.til_description_insert_product);

        spinner = (Spinner) findViewById(R.id.spinner_categoty_insert_product);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button btn_save = (Button) findViewById(R.id.btn_save_insert_product);
        btn_save.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void sendData(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("title", txt_title.getText().toString());
        params.add("price", txt_price.getText().toString());
        params.add("quantity", txt_quantity.getText().toString());
        params.add("category", text);
        params.add("description", txt_description.getText().toString());
        params.add("idpeople", Utils.ID);

        client.post(Utils.PRODUCTO_SERVICE, params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("product");
                    product_id = data.getString("_id");
                    if(product_id != null){
                        updateImage();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse.has("errTitle")){
                    til_title.setError(getString(R.string.err_title));
                } else {
                    til_title.setErrorEnabled(false);
                }
                if (errorResponse.has("errPrice")){
                    til_price.setError(getString(R.string.err_price));
                } else {
                    til_price.setErrorEnabled(false);
                }
                if (errorResponse.has("errQuantity")){
                    til_quantity.setError(getString(R.string.err_quantity));
                } else {
                    til_quantity.setErrorEnabled(false);
                }
                if (errorResponse.has("errDescription")){
                    til_description.setError(getString(R.string.err_description));
                } else {
                    til_description.setErrorEnabled(false);
                }
            }
        });
        
    }
    private void updateImage(){
        String url = getIntent().getStringExtra("ruta");
        Toast.makeText(this, "URL: " + url, Toast.LENGTH_SHORT).show();
        if (url != null) {
            AsyncHttpClient client = new AsyncHttpClient();
            File file_avatar = new File(url);
            RequestParams params = new RequestParams();
            try {
                params.put("product", file_avatar);
                client.post(Utils.ID_PRODUCTO_IMAGE_SERVICE + product_id, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(InsertProductActivity.this, "Espere un momento por favor", Toast.LENGTH_SHORT).show();
                        if (response.has("image")) {
                            Utils.startTab = 3;
                            Toast.makeText(InsertProductActivity.this, "Producto creado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(InsertProductActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(InsertProductActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
                        }
                    }
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //Toast.makeText(InsertProductActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(InsertProductActivity.this, "Errr", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (FileNotFoundException e) {

            }
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_insert_product){
            if (validateProduct()){
                sendData();
            }
        }
    }

    private Boolean validateProduct(){
        boolean b = true;
        if (txt_title.getText().toString().trim().isEmpty()){
            til_title.setError(getString(R.string.err_void_title));
            b = false;
        } else {
            til_title.setErrorEnabled(false);
        }
        if (txt_price.getText().toString().trim().isEmpty()){
            til_price.setError(getString(R.string.err_void_price));
            b = false;
        } else {
            til_price.setErrorEnabled(false);
        }
        if (txt_quantity.getText().toString().trim().isEmpty()){
            til_quantity.setError(getString(R.string.err_void_quantity));
            b = false;
        } else {
            til_quantity.setErrorEnabled(false);
        }
        if (txt_description.getText().toString().trim().isEmpty()){
            til_description.setError(getString(R.string.err_void_description));
            b = false;
        } else {
            til_description.setErrorEnabled(false);
        }
        return  b;
    }

}
