package com.example.app_tiendamovil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.app_tiendamovil.Collection.product.InsertProductActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Fragment.category_fragment.CategoryFragment;
import com.example.app_tiendamovil.Fragment.ProfileFragment;
import com.example.app_tiendamovil.Fragment.TabProfile.FavoriteFragment;
import com.example.app_tiendamovil.Fragment.TabProfile.ProfileProductFragment;
import com.example.app_tiendamovil.Fragment.TabsFragment;
import com.example.app_tiendamovil.Fragment.product_fragment.ProductFragment;
import com.example.app_tiendamovil.Fragment.fragment_citas.CitasFragment;
import com.example.app_tiendamovil.Fragment.fragment_mensaje.MessageFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager fm;
    private ImageView img_avatar;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, options);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Toast.makeText(this, Utils.ID, Toast.LENGTH_SHORT).show();
        loadComponents();
    }

    private void loadComponents() {
        fm = getSupportFragmentManager();
        if (Utils.startTab == 0){
            fm.beginTransaction().replace(R.id.stage, new ProductFragment()).commit();
        } else if(Utils.startTab == 1){
            fm.beginTransaction().replace(R.id.stage, new ProfileFragment()).commit();
        } else if(Utils.startTab == 2){
            fm.beginTransaction().replace(R.id.stage, new CategoryFragment()).commit();
        } else if(Utils.startTab == 3){
            fm.beginTransaction().replace(R.id.stage, new ProfileProductFragment()).commit();
        } else if(Utils.startTab == 4){
            fm.beginTransaction().replace(R.id.stage, new FavoriteFragment()).commit();
        } else if(Utils.startTab == 5){
            fm.beginTransaction().replace(R.id.stage, new ProfileProductFragment()).commit();
        }

        //View nav = navigationView.getHeaderView(0);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        String user = getIntent().getStringExtra("name");
        String pass = getIntent().getStringExtra("pass");
        Utils.USER = user;
        Utils.PASS = pass;

        TextView txt_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_nav_name);
        txt_name.setText(user);
        img_avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        getData();

        //if ()
    }

        //Desplegar el Menu de navegacion
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        // Uso de la base de datos para Perfil del usuario
    private void getData (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);

                    String avatar = obj.getString("avatar");
                    if (!avatar.equals("")){
                        Glide.with (Main2Activity.this).load (Utils.HOST + avatar).transform(new CenterCrop(), new CircleCrop()).into(img_avatar);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(Main2Activity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

        //Acciones para el Menu de navegacion
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //fm = getSupportFragmentManager();
        if (id == R.id.nav_home) {
            fm.beginTransaction().replace(R.id.stage, new ProductFragment()).commit();

        } else if (id == R.id.nav_profile) {
            fm.beginTransaction().replace(R.id.stage, new ProfileFragment()).commit();

        } else if (id == R.id.nav_category) {
            fm.beginTransaction().replace(R.id.stage, new CategoryFragment()).commit();
        } else if (id == R.id.nav_products) {
            Utils.tab = 0;
            fm.beginTransaction().replace(R.id.stage, new TabsFragment()).commit();
        } else if (id == R.id.nav_message) {
            fm.beginTransaction().replace(R.id.stage, new MessageFragment()).commit();
        } else if (id == R.id.nav_favorite) {
            Utils.tab = 1;
            fm.beginTransaction().replace(R.id.stage, new TabsFragment()).commit();
        } else if (id == R.id.nav_meeting) {
            fm.beginTransaction().replace(R.id.stage, new CitasFragment()).commit();
        } else if (id == R.id.nav_logout) {
            AlertSignOut();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void AlertSignOut(){
        final CharSequence[] opciones = {"Salir", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i] == "Salir"){
                    signOut();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Main2Activity.this, "Sicces Sing upot", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Main2Activity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
