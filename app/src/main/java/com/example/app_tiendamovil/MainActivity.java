package com.example.app_tiendamovil;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final int DURACION_SPLASH = 2000;
    private GoogleSignInClient client;
    String er;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);
        loadComponents();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        //Toast.makeText(this, acct.getEmail(), Toast.LENGTH_SHORT).show();
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            er = personEmail;
        }
        Toast.makeText(this, er, Toast.LENGTH_SHORT).show();

        loadComponents();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURACION_SPLASH);
    }


    private void loadComponents() {
       /* Button btn_inicio = (Button) findViewById(R.id.btn_inicio);
        btn_inicio.setOnClickListener(this);*/

    }

        // Ingresar al LogIn
    @Override
    public void onClick(View v) {
        /*if(v.getId() == R.id.btn_inicio){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }*/

    }
}
