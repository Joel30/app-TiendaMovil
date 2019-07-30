package com.example.app_tiendamovil.Fragment.fragment_mensaje;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Seller.DataInfoSeller;
import com.example.app_tiendamovil.Collection.Seller.SellerActivity;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.message.ChatActivity;
import com.example.app_tiendamovil.Collection.message.ListChatAdapter;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MessageFragment extends Fragment {


    private View mFA;
    private ArrayList<String> my_id;
    private RecyclerView list;
    private String my_name;
    private ArrayList<DataInfoSeller> list_data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFA = inflater.inflate(R.layout.fragment_message, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mis Mensajes");

        loadComponents();
        return mFA;
    }

    private void loadComponents() {
        my_id = new ArrayList<>();
        getMyData();
        getDataMessage();
    }

    private void getMyData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);
                    my_name = obj.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "ERROR >:", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataMessage (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.MENSAJE_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Toast.makeText(getActivity(), "MESSAGE", Toast.LENGTH_SHORT).show();
                    JSONArray data = response.getJSONArray("list");
                    for (int i = 0; i < data.length(); i++){
                        JSONObject obj = data.getJSONObject(i);
                        //for (int j = 0; j < obj.length(); j++){
                            if  (Utils.ID.equals(obj.getString("idseller"))){
                                my_id.add(obj.getString("idbuyer"));
                            } else if (Utils.ID.equals(obj.getString("idbuyer"))){
                                my_id.add(obj.getString("idseller"));
                            }
                        //}
                    }
                    Toast.makeText(getActivity(), "AQUI", Toast.LENGTH_SHORT).show();
                    getDataPersona();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataPersona() {
        list = (RecyclerView) mFA.findViewById(R.id.recycler_view_list_chat);
        list_data = new ArrayList<>();
        registerForContextMenu(list);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PERSONA_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("result");
                    for (int i = 0; i < data.length(); i++){
                        DataInfoSeller p = new DataInfoSeller();
                        JSONObject obj = data.getJSONObject(i);
                        for (int j = 0; j < my_id.size(); j++){
                            if  (my_id.get(j).equals(obj.getString("_id"))){
                                p.id = obj.getString("_id");
                                p.name = obj.getString("name");
                                p.phone = obj.getString("phone");
                                p.avatar = obj.getString("avatar");
                                list_data.add(p);
                            }
                        }
                    }
                    ListChatAdapter adapter = new ListChatAdapter(list_data, getActivity());

                    adapter.setOnclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);;
                            intent.putExtra("id", list_data.get(list.getChildAdapterPosition(v)).getId());
                            intent.putExtra("name", my_name);
                            startActivity(intent);
                        }
                    });
                    list.setLayoutManager(new LinearLayoutManager(getContext()));
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "ERROR >:", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
