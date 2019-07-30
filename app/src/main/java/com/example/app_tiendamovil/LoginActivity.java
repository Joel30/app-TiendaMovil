package com.example.app_tiendamovil;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient client;
    private int GOOGLE_CODE = 4031;
    private TextInputLayout til_user, til_pass;
    private EditText txt_user, txt_pass;
    private GoogleSignInResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();

        loadComponents();
    }

    private void loadComponents() {
        SignInButton googlebtn = (SignInButton) this.findViewById(R.id.signInButton);
        googlebtn.setOnClickListener(this);

        Button btn_login = (Button) findViewById(R.id.btn_logIn);
        btn_login.setOnClickListener(this);

        TextView txt_register = (TextView) findViewById(R.id.txt_register);
        txt_register.setOnClickListener(this);

        txt_user = (EditText) findViewById(R.id.txt_userL);
        txt_pass = (EditText) findViewById(R.id.txt_passL);
        til_user = (TextInputLayout) findViewById(R.id.til_login_user);
        til_pass = (TextInputLayout) findViewById(R.id.til_login_pass);
    }

        // Almacenar datos del Login el la Base de Datos
    private void sendData(){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user", txt_user.getText().toString());
        params.add("password", txt_pass.getText().toString());

        client.post(Utils.LOGIN_SERVICE, params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Utils.TOKEN = response.getString("token");

                    Intent intent= new Intent(LoginActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    JSONArray data = response.getJSONArray("user");
                    JSONObject obj = data.getJSONObject(0);

                    Utils.ID = obj.getString("idpeople");

                    String name = obj.getString("user");
                    String pass = obj.getString("password");
                    intent.putExtra("name",name);
                    intent.putExtra("pass", pass);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

                //Error al conectarse a la base de datos
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse.has("errUser")){
                    til_user.setError(getString(R.string.err_no_user));
                } else {
                    til_user.setErrorEnabled(false);
                }
                if (errorResponse.has("errPass")){
                    til_pass.setError(getString(R.string.err_pass));
                } else {
                    til_pass.setErrorEnabled(false);
                }
                //Toast.makeText(LoginActivity.this, errorResponse.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerData() {

        //String lat = getIntent().getStringExtra("lat");
        //String lon = getIntent().getStringExtra("lon");
        //String street = getIntent().getStringExtra("street");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("name", result.getSignInAccount().getDisplayName());
        params.add("email", result.getSignInAccount().getEmail());
        params.add("user", result.getSignInAccount().getEmail());
        //params.add("avatar", String.valueOf(result.getSignInAccount().getPhotoUrl()));
        //params.add("street", street);
        //params.add("lat", lat);
        //params.add("lon", lon);

        client.post(Utils.PERSONA_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("checkIn")){
                    Toast.makeText(LoginActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject data = response.getJSONObject("checkIn");
                        Utils.ID = data.getString("idpeople");
                        String name = txt_user.getText().toString();
                        String pass = txt_pass.getText().toString();
                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("name",name);
                        //intent.putExtra("pass", pass);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            // validaciones si falla el envio
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                if (errorResponse.has("errUser")){
                    try {
                        JSONObject obj = errorResponse.getJSONObject("errUser");
                        JSONArray user = obj.getJSONArray("user");
                        JSONObject obj1 = user.getJSONObject(0);
                        Utils.ID = obj1.getString("idpeople");

                        String name = obj1.getString("user");
                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("name",name);
                        //intent.putExtra("pass", pass);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }



    // LogIn con Google (Firebase)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_CODE){
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
                registerData();
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

        // Erro en la conexion (firebase)
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        //Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signInButton){
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
            startActivityForResult(intent, GOOGLE_CODE);
        }
        if (v.getId() == R.id.btn_logIn){
            if (validateLogIn()){
                sendData();
            }
        }
        if (v.getId() == R.id.txt_register){
            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        }
    }

        // Validar datos vacios del LogIn
    private Boolean validateLogIn(){
        if (txt_user.getText().toString().trim().isEmpty()){
            til_user.setError(getString(R.string.err_void_user));
            return false;
        } else {
            til_user.setErrorEnabled(false);
        }
        if (txt_pass.getText().toString().trim().isEmpty()){
            til_pass.setError(getString(R.string.err_void_pass));
            return false;
        } else {
            til_pass.setErrorEnabled(false);
        }
        return  true;
    }
}
