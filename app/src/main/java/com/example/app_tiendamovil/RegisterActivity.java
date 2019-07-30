package com.example.app_tiendamovil;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient client;
    private int GOOGLE_CODE = 3012;

    private TextView txt_name, txt_phone, txt_user, txt_pass, txt_confirm;
    private TextInputLayout til_name, til_phone, til_user, til_pass, til_confirm;
    private GoogleSignInResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        Button btn_ingresar = (Button) findViewById(R.id.btn_registerU_singin);
        btn_ingresar.setOnClickListener(this);

        TextView txt_session = (TextView) findViewById(R.id.txt_registerU_session);
        txt_session.setOnClickListener(this);

        SignInButton googlebtn = (SignInButton) this.findViewById(R.id.signInButtonR);
        googlebtn.setOnClickListener(this);

        txt_name = (TextView) findViewById(R.id.txt_registerU_name);
        txt_phone = (TextView) findViewById(R.id.txt_registerU_phone);
        txt_user = (TextView) findViewById(R.id.txt_registerU_userName);
        txt_pass = (TextView) findViewById(R.id.txt_registerU_pass);
        txt_confirm = (TextView) findViewById(R.id.txt__register_passConfirm);

        til_name = (TextInputLayout) findViewById(R.id.til_registerU_name);
        til_phone = (TextInputLayout) findViewById(R.id.til_registerU_phone);
        til_user = (TextInputLayout) findViewById(R.id.til_registerU_user);
        til_pass = (TextInputLayout) findViewById(R.id.til_registerU_pass);
        til_confirm = (TextInputLayout) findViewById(R.id.til_registerU_passConfirm);
    }

        //Enviar el registro a la Base de Datos
    private void sendData() {

        String lat = getIntent().getStringExtra("lat");
        String lon = getIntent().getStringExtra("lon");
        String street = getIntent().getStringExtra("street");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if (result == null){
            params.add("name", txt_name.getText().toString());
            params.add("phone", txt_phone.getText().toString());
            params.add("street", street);
            params.add("user",txt_user.getText().toString());
            params.add("password", txt_pass.getText().toString());
            params.add("lat", lat);
            params.add("lon", lon);
        } else {
            params.add("name", result.getSignInAccount().getDisplayName());
            params.add("email", result.getSignInAccount().getEmail());
            params.add("user", result.getSignInAccount().getEmail());
            //params.add("avatar", String.valueOf(result.getSignInAccount().getPhotoUrl()));
            params.add("street", street);
            params.add("lat", lat);
            params.add("lon", lon);
        }

        client.post(Utils.PERSONA_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("checkIn")){
                    Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject data = response.getJSONObject("checkIn");
                        Utils.ID = data.getString("idpeople");
                        String name = txt_user.getText().toString();
                        String pass = txt_pass.getText().toString();
                        Intent intent = new Intent(RegisterActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("name",name);
                        //intent.putExtra("pass", pass);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
                // validaciones si falla el envio
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                if (result == null){
                    if (errorResponse.has("errUserL")){
                        til_user.setError(getString(R.string.err_userL));
                    } else {
                        if (errorResponse.has("errUser")){
                            til_user.setError(getString(R.string.err_user));
                        } else {
                            til_user.setErrorEnabled(false);
                        }
                    }
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


                    if (errorResponse.has("errPass")){
                        til_pass.setError(getString(R.string.err_password));
                    } else {
                        til_pass.setErrorEnabled(false);
                    }
                } else {
                    if (errorResponse.has("errUser")){
                        try {
                            JSONObject obj = errorResponse.getJSONObject("errUser");
                            JSONArray user = obj.getJSONArray("user");
                            JSONObject obj1 = user.getJSONObject(0);
                            Utils.ID = obj1.getString("idpeople");

                            String name = obj1.getString("user");
                            Intent intent = new Intent(RegisterActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("name",name);
                            //intent.putExtra("pass", pass);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

    }

        // Registro con Google (Firebase)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_CODE){
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                sendData();
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
        //Error de conexion (Firebase)
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        //Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_registerU_singin){
            if (validateRegister()){
                sendData();
            }
        }
        if (v.getId() == R.id.txt_registerU_session){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.signInButtonR){
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
            startActivityForResult(intent, GOOGLE_CODE);
        }
    }

        //Validar campos vacios del registro
    private Boolean validateRegister(){
        boolean b = true;
        if (txt_name.getText().toString().trim().isEmpty()){
            til_name.setError(getString(R.string.err_void_name));
            b = false;
        } else {
            til_name.setErrorEnabled(false);
        }
        if (txt_user.getText().toString().trim().isEmpty()){
            til_user.setError(getString(R.string.err_void_user));
            b = false;
        } else {
            til_user.setErrorEnabled(false);
        }
        if (txt_pass.getText().toString().trim().isEmpty()){
            til_pass.setError(getString(R.string.err_void_pass));
            b = false;
        } else {
            til_pass.setErrorEnabled(false);
        }
        if (txt_confirm.getText().toString().trim().isEmpty()){
            til_confirm.setError(getString(R.string.err_void_pass));
            b = false;
        } else {
            if (txt_pass.getText().toString().trim().equals(txt_confirm.getText().toString().trim())){
                til_confirm.setErrorEnabled(false);
            } else {
                til_confirm.setError(getString(R.string.err_pass));
                b = false;
            }
        }
        return  b;
    }
}
