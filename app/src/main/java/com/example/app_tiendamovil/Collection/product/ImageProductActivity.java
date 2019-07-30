package com.example.app_tiendamovil.Collection.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Images;
import com.example.app_tiendamovil.Collection.Permissions;
import com.example.app_tiendamovil.R;


public class ImageProductActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_product;
    private Images images;
    private Permissions permissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_product);
        //getSupportActionBar().setTitle("Images");
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
        permissions = new Permissions(this);

        img_product = (ImageView) findViewById(R.id.image_create_product);

        Button btn_camera = (Button) findViewById(R.id.btn_camera_create_product);
        btn_camera.setOnClickListener(this);
        Button btn_insert = (Button) findViewById(R.id.btn_insert_create_product);
        btn_insert.setOnClickListener(this);
        Button btn_send = (Button) findViewById(R.id.btn_send_create_product);
        btn_send.setOnClickListener(this);

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
        if (v.getId() == R.id.btn_camera_create_product){
            if (permissions.reviewPermissions(1)){
                subirImagen();
            }
        }
        if (v.getId() == R.id.btn_insert_create_product){
            if (permissions.reviewPermissions(2)){
                cargarImagen();
            }
        }
        if (v.getId() == R.id.btn_send_create_product){
            Intent intent = new Intent(this, InsertProductActivity.class);
            intent.putExtra("ruta", images.currentPhotoPath);
            Toast.makeText(this, "IMAGENES :" + images.currentPhotoPath, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
}
