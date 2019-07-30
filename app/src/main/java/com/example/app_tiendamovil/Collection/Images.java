package com.example.app_tiendamovil.Collection;

import android.content.Context;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Images extends AppCompatActivity {

        // Codes
    public int REQUEST_IMAGE_CAPTURE =111;
    public int REQUEST_TAKE_PHOTO=222;

    public String currentPhotoPath;
    public Bitmap imgBitmap;
    private Context context;

    public Images(Context context) {
        this.context = context;
    }

        // Obterner la Ruta de la Imagen
    public  String getPathFromURI(Uri contentURI) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(contentURI,null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

        //Almacenar imagen en el dispositivo
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  //prefix
                ".jpg",   // suffix
                storageDir      //directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void saveToInternalStorage(Bitmap bitmapImage) {
        File mypath = null;
        try {
            mypath = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String path = currentPhotoPath;
        imgBitmap = BitmapFactory.decodeFile(path);
    }
}
