package com.example.app_tiendamovil.Collection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class Permissions extends AppCompatActivity {

    public int CODE_PERMISSIONS = 1100;
    private Context context;

    public Permissions(Context context) {
        this.context = context;
    }

        // Verificación de permisos
    public boolean reviewPermissions(int i) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && i == 1) {
            //Toast.makeText(context, "Los permisos fueron otorgados", Toast.LENGTH_LONG).show();
            return true;
        }
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && i == 2){
            return true;
        }
        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_PERMISSIONS);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_PERMISSIONS ) {
            if (grantResults.length == 3) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Permiso de uso de camara Otorgado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Permisos de camara Denegado", Toast.LENGTH_SHORT).show();
                }
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Permiso de almacenamiento otorgado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Permisos de almacenamiento Denegado", Toast.LENGTH_SHORT).show();
                }
                if (grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Permiso de lectuta almacenamiento otorgado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Permiso de lectura denegado", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(context, "no permisosos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
