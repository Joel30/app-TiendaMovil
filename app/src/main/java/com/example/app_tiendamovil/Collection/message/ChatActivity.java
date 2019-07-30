package com.example.app_tiendamovil.Collection.message;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Seller.DataInfoSeller;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.loopj.android.http.RequestParams;


public class ChatActivity extends AppCompatActivity {


    public RecyclerView myRecylerView ;
    public List<DataInfoChat> chatList ;
    public ChatAdapter chatAdapter;
    public EditText messagetxt ;
    public Button send ;

    //declare socket object
    private Socket socket;

    public String Nickname , idseller , my_image, seller_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chat");

        loadComponents();
    }
    // Regresar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void loadComponents() {
        messagetxt = (EditText) findViewById(R.id.chat_send_message) ;
        send = (Button)findViewById(R.id.send_message);
        Nickname= getIntent().getStringExtra("name");
        idseller = getIntent().getStringExtra("id");


        chatList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.list_chat_message);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());


        //
        getImagen();
        //este es el metodo para cargar mensajes anteriores o persistentes de la bd
        cargarMensajes();
        //este es el metodo para poner a la escucha el chat con sockets
        socketEscuchaEmisor();
    }

    private void cargarImagen(){

    }
    private void getImagen() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PERSONA_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("result");
                    for (int i = 0; i < data.length(); i++){
                        JSONObject obj = data.getJSONObject(i);
                        if  (Utils.ID.equals(obj.getString("_id"))){
                            my_image = obj.getString("avatar");
                        } if (idseller.equals(obj.getString("_id"))){
                            seller_image = obj.getString("avatar");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(ChatActivity.this, "ERROR >:", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarMensajes() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idbuyer", Utils.ID);
        params.add("idseller", idseller);
        client.get(Utils.MENSAJE_SERVICE,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                if  (response.has("message")){
                    try {
                    JSONArray data = response.getJSONArray("message");
                    for (int i=0;i<data.length();i++){
                            JSONObject obj = data.getJSONObject(i);
                            String nickname = obj.getString("nickname");
                            String message = obj.getString("message");
                            String date = obj.getString("create_at").substring(11,16);
                            Toast.makeText(ChatActivity.this, obj.getString("create_at"), Toast.LENGTH_SHORT).show();
                            DataInfoChat m;
                            if (obj.getString("id").equals(Utils.ID)){
                                m = new DataInfoChat(nickname,message, date, my_image);
                            } else {
                                m = new DataInfoChat(nickname,message, date,seller_image);
                            }
                            chatList.add(m);
                            chatAdapter = new ChatAdapter(chatList, ChatActivity.this);
                            //chatAdapter.notifyDataSetChanged();
                            myRecylerView.setAdapter(chatAdapter);
                            myRecylerView.getLayoutManager().scrollToPosition(chatAdapter.getItemCount()-1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(ChatActivity.this, "Error insert", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void socketEscuchaEmisor() {
        try {
            socket = IO.socket(Utils.HOST);
            socket.connect();
            socket.emit("join", Nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        //setting up recyler
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messagetxt.getText().toString().isEmpty()){
                    socket.emit("messagedetection",Nickname,messagetxt.getText().toString(), idseller, Utils.ID,Utils.ID);
                    messagetxt.setText(" ");
                }


            }
        });
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(ChatActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(ChatActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            if ((data.getString("id").equals(Utils.ID) && (data.getString("idreceiver").equals(idseller))) || (data.getString("id").equals(idseller) && (data.getString("idreceiver").equals(Utils.ID)))){
                                String nickname = data.getString("nickname");
                                String message = data.getString("message");
                                String date = data.getString("create_at");
                                DataInfoChat m;
                                if (data.getString("id").equals(Utils.ID)){
                                    m = new DataInfoChat(nickname,message, date, my_image);
                                } else {
                                    m = new DataInfoChat(nickname,message, date,seller_image);
                                }

                                chatList.add(m);
                                chatAdapter = new ChatAdapter(chatList, ChatActivity.this);
                                //chatAdapter.notifyDataSetChanged();
                                myRecylerView.setAdapter(chatAdapter);
                                myRecylerView.getLayoutManager().scrollToPosition(chatAdapter.getItemCount()-1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();
    }
}
