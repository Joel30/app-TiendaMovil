package com.example.app_tiendamovil.Collection.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Images;
import com.example.app_tiendamovil.Collection.Permissions;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Main2Activity;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class EditProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView txt_title, txt_price, txt_quantity, txt_description;
    private TextInputLayout til_title, til_price, til_quantity, til_description;
    private ImageView img_product;
    private String id_producto, inicializarItem, item;
    private Spinner spinner_product;
    private Images images;
    private Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        getSupportActionBar().setTitle("Editar Producto");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadComponents();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

        //declarar elementos del layout (activity_edit_product.xml)
    private void loadComponents() {
        images = new Images(this);
        permissions = new Permissions(this);

        txt_title = (TextView) findViewById(R.id.txt_edit_one_title_product);
        txt_price = (TextView) findViewById(R.id.txt_edit_one_price_product);
        txt_quantity = (TextView) findViewById(R.id.txt_edit_one_quantity_product);
        txt_description = (TextView) findViewById(R.id.txt_edit_one_description_product);

        til_title = (TextInputLayout) findViewById(R.id.til_edit_one_title_product);
        til_price = (TextInputLayout) findViewById(R.id.til_edit_one_price_product);
        til_quantity = (TextInputLayout) findViewById(R.id.til_edit_one_quantity_product);
        til_description = (TextInputLayout) findViewById(R.id.til_edit_one_description_product);

        img_product = (ImageView) findViewById(R.id.img_edit_one_product);

        id_producto = getIntent().getStringExtra("id");

        spinner_product = (Spinner) findViewById(R.id.spinner_edit_product);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_product.setAdapter(adapter);
        getData();
        spinner_product.setOnItemSelectedListener(this);

        Button btn_save = (Button) findViewById(R.id.btn_save_edit_product);
        btn_save.setOnClickListener(this);

        img_product.setOnClickListener(this);
    }

        // Obtener el item seleccionado del spinner ¿
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Obtener datos de la DB
    private void getData (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PRODUCTO_SERVICE + id_producto, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("product");
                    JSONObject obj = data.getJSONObject(0);
                    txt_title.setText(obj.getString("title"));
                    txt_price.setText(obj.getString("price"));
                    txt_quantity.setText(obj.getString("quantity"));
                    inicializarItem = obj.getString("category");
                    spinner_product.setSelection(obtenerPosicionItem(spinner_product, inicializarItem));
                    txt_description.setText(obj.getString("description"));
                    String image = obj.getString("image");
                    Glide.with (EditProductActivity.this).load (Utils.HOST + image).into(img_product);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(EditProductActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para obtener la posición de un ítem del spinner
    public static int obtenerPosicionItem(Spinner spinner, String c) {
        int posicion = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(c)) {
                posicion = i;
            }
        }
        return posicion;
    }

    // enviar datos del producto para su almacenamiento
    private void updateData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("title", txt_title.getText().toString());
        params.add("price", txt_price.getText().toString());
        params.add("quantity", txt_quantity.getText().toString());
        params.add("category", item);
        params.add("description", txt_description.getText().toString());
        params.add("idpeople", Utils.ID);
        Toast.makeText(this, Utils.ID, Toast.LENGTH_SHORT).show();
        client.put(Utils.ID_PRODUCTO_SERVICE + id_producto, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("product")){
                    updateImage();
                    Toast.makeText(EditProductActivity.this, "Usuario Actualizado", Toast.LENGTH_SHORT).show();
                    Utils.startTab = 3;
                    Intent intent = new Intent(EditProductActivity.this, Main2Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditProductActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(EditProductActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Toast.makeText(EditProductActivity.this, "slkdfjsñlkjfsñlfjñ" , Toast.LENGTH_SHORT).show();
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

    // Actualizar la imagen en la Base de Datos
    private void updateImage(){
        if (images.currentPhotoPath != null) {
            AsyncHttpClient client = new AsyncHttpClient();
            File file_avatar = new File(images.currentPhotoPath);
            RequestParams params = new RequestParams();
            try {
                params.put("product", file_avatar);
                client.post(Utils.ID_PRODUCTO_IMAGE_SERVICE + id_producto, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(EditProductActivity.this, "Espere un momento por favor", Toast.LENGTH_SHORT).show();
                        if (response.has("image")) {
                            Toast.makeText(EditProductActivity.this, "Producto creado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProductActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
                        }
                    }
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //Toast.makeText(EditProductActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(EditProductActivity.this, "Errr", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (FileNotFoundException e) {

            }
        }
    }

        // Alerta para la seleccion de metodo de cargar la imagen
    public void AlertEditImage(){
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i] == "Tomar Foto"){
                    if (permissions.reviewPermissions(1)){
                        subirImagen();
                    }
                } else if (opciones[i] == "Cargar Imagen"){
                    if (permissions.reviewPermissions(2)){
                        cargarImagen();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

        //Metodo para cargar una imagen desde el dispositivo
    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicaion"), images.REQUEST_IMAGE_CAPTURE);
    }

        //Metodo para tomar una fotografia
    private void subirImagen(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, images.REQUEST_TAKE_PHOTO);
    }

        // Almacenar y cargar imagens en/del dispositivo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if  (resultCode == RESULT_OK && requestCode == images.REQUEST_TAKE_PHOTO){
            Bitmap img = (Bitmap) data.getExtras().get("data");
            images.saveToInternalStorage(img);
            img_product.setImageBitmap(img);
        }

        if  (resultCode == RESULT_OK && requestCode == images.REQUEST_IMAGE_CAPTURE){
            Uri path = data.getData();
            images.currentPhotoPath = images.getPathFromURI(path);
            img_product.setImageURI(path);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_edit_product){
            updateData();
        }
        if (v.getId() == R.id.img_edit_one_product){
            AlertEditImage();
        }
    }
}
