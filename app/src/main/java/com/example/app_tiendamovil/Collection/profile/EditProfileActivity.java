package com.example.app_tiendamovil.Collection.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.app_tiendamovil.Collection.Images;
import com.example.app_tiendamovil.Collection.Permissions;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.LocationActivity;
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

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txt_name, txt_location, txt_phone, txt_user, txt_pass;
    private TextInputLayout til_name, til_location, til_phone, til_user, til_pass;
    private ImageView img_avatar;
    private Images images;
    private Permissions permissions;
    private String lat, lon, street;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //getSupportActionBar().setTitle("Images");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadComponents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Utils.startTab = 1;
        Intent intent = new Intent(EditProfileActivity.this, Main2Activity.class);//.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        //onBackPressed();
        return false;
    }

    private void loadComponents() {
        images = new Images(this);
        permissions = new Permissions(this);

        txt_name = (TextView) findViewById(R.id.txt_name_edit_profile);
        txt_location = (TextView) findViewById(R.id.txt_location_edit_profile);
        txt_phone = (TextView) findViewById(R.id.txt_phone_edit_profile);
        txt_user = (TextView) findViewById(R.id.txt_user_edit_profile);
        txt_pass = (TextView) findViewById(R.id.txt_pass_edit_profile);

        til_name = (TextInputLayout) findViewById(R.id.til_edit_profile_name);
        til_location = (TextInputLayout) findViewById(R.id.til_edit_profile_location);
        til_phone = (TextInputLayout) findViewById(R.id.til_edit_profile_phone);
        til_user = (TextInputLayout) findViewById(R.id.til_edit_profile_user);
        til_pass = (TextInputLayout) findViewById(R.id.til_edit_profile_pass);

        img_avatar = (ImageView) findViewById(R.id.img_edit_profile);
        getData();

        Button btn = (Button) findViewById(R.id.btn_save_edit_profile);
        btn.setOnClickListener(this);
        Button btn_edit = (Button) findViewById(R.id.change_img_profile);
        btn_edit.setOnClickListener(this);
        Button btn_location = (Button) findViewById(R.id.btn_location_edit_profile);
        btn_location.setOnClickListener(this);

    }

    private void getData (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);
                    txt_name.setText(obj.getString("name"));

                    String epa_street = getIntent().getStringExtra("street");
                    if (epa_street != null){
                        txt_location.setText(epa_street);
                    } else{
                        txt_location.setText(obj.getString("street"));
                    }
                    txt_phone.setText(obj.getString("phone"));
                    txt_user.setText(Utils.USER);
                    String avatar = obj.getString("avatar");
                    Glide.with (EditProfileActivity.this).load (Utils.HOST + avatar).error(R.drawable.profiledefault).placeholder(R.drawable.profiledefault).transform(new CenterCrop(), new RoundedCorners(16)).into(img_avatar);
                    lat = obj.getString("lat");
                    lon = obj.getString("lon");
                    street = obj.getString("street");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("name", txt_name.getText().toString());
        params.add("street", txt_location.getText().toString());
        String epa_lat = getIntent().getStringExtra("lat");
        String epa_lon = getIntent().getStringExtra("lon");
        if (epa_lat != null){
            params.add("lat", epa_lat);
            params.add("lon", epa_lon);
        }
        else {
            params.add("lat", lat);
            params.add("lon", lon);
        }
        params.add("phone", txt_phone.getText().toString());

        String a = txt_user.getText().toString();
        if (a != null){
            if  (!Utils.USER.equals(a)){
                params.add("user", txt_user.getText().toString());
            }
            if (!txt_pass.getText().toString().equals("")){
                params.add("password", txt_pass.getText().toString());
            }
        }

        client.put(Utils.ID_PERSONA_SERVICE + Utils.ID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("updatePersona")){
                    Toast.makeText(EditProfileActivity.this, "Usuario Actualizado", Toast.LENGTH_SHORT).show();
                    Utils.startTab = 1;
                    Intent intent = new Intent(EditProfileActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(EditProfileActivity.this, "Falla", Toast.LENGTH_SHORT).show();

                if (errorResponse.has("errName")){
                    til_name.setError(getString(R.string.err_name));
                } else {
                    til_name.setErrorEnabled(false);
                }
                if (errorResponse.has("errPhone")){
                    til_phone.setError(getString(R.string.err_phone));
                } else {
                    til_phone.setErrorEnabled(false);
                }
                if (errorResponse.has("errUserL")){
                    til_user.setError(getString(R.string.err_userL));
                } else {
                    if (errorResponse.has("errUser")){
                        til_user.setError(getString(R.string.err_user));
                    } else {
                        til_user.setErrorEnabled(false);
                    }
                }

                if (errorResponse.has("errPass")){
                    til_pass.setError(getString(R.string.err_password));
                } else {
                    til_pass.setErrorEnabled(false);
                }
            }
        });
    }

    private void updateImage(){
        if (images.currentPhotoPath != null) {
            AsyncHttpClient client = new AsyncHttpClient();
            File file_avatar = new File(images.currentPhotoPath);
            RequestParams params = new RequestParams();
            try {
                params.put("profile", file_avatar);

                client.post(Utils.ID_PERSONA_IMAGE_SERVICE + Utils.ID, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(EditProfileActivity.this, "Esperre un momento por favor", Toast.LENGTH_SHORT).show();
                        if (response.has("image")) {
                            Toast.makeText(EditProfileActivity.this, "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //Toast.makeText(EditProfileActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(EditProfileActivity.this, "Errr", Toast.LENGTH_SHORT).show();
                    }

                });
            } catch (FileNotFoundException e) {

            }
        }
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicaion"), images.REQUEST_IMAGE_CAPTURE);
    }

    private void subirImagen(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, images.REQUEST_TAKE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if  (resultCode == RESULT_OK && requestCode == images.REQUEST_TAKE_PHOTO){
            Bitmap img = (Bitmap) data.getExtras().get("data");
            images.saveToInternalStorage(img);
            img_avatar.setImageBitmap(images.imgBitmap);
            updateImage();
        }

        if  (resultCode == RESULT_OK && requestCode == images.REQUEST_IMAGE_CAPTURE){
            Uri path = data.getData();
            images.currentPhotoPath = images.getPathFromURI(path);
            img_avatar.setImageURI(path);
            updateImage();
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

    private void goToMap(){
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("street", street);
        startActivity(intent);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_edit_profile){
            if (txt_location.getText().toString().trim().isEmpty()){
                til_location.setError(getString(R.string.err_map_location));
            } else {
                til_location.setErrorEnabled(false);
                updateData();
            }
        }
        if (v.getId() == R.id.change_img_profile){
            AlertEditImage();
        }
        if (v.getId() == R.id.btn_location_edit_profile){
            goToMap();
        }
    }
}
