package com.example.app_tiendamovil.Fragment.TabProfile;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Seller.AdapterSeller;
import com.example.app_tiendamovil.Collection.Seller.DataInfoSeller;
import com.example.app_tiendamovil.Collection.Seller.SellerActivity;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragment extends Fragment {

    private View mFA;
    private ArrayList<String> id_seller;
    private String name_people, id_p;
    private RecyclerView list;
    private ArrayList<DataInfoSeller> list_data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFA = inflater.inflate(R.layout.fragment_follow, container, false);
        loadComponents();
        return mFA;
    }

    private void loadComponents() {
        id_seller = new ArrayList<>();
        getDataFollowed();
    }

    private String getDataPersona (String id){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Toast.makeText(getActivity(), "NOMBRE", Toast.LENGTH_SHORT).show();
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);
                    name_people = obj.getString("name");
                    Toast.makeText(getActivity(), obj.getString("name") , Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        return name_people;
    }

    private void getDataFollowed (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_SEGUIDOS_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("seguidos");
                    JSONObject obj = data.getJSONObject(0);
                    JSONArray idseller = obj.getJSONArray("idseller");
                    for (int i = 0; i< idseller.length(); i++){
                        id_seller.add(idseller.getString(i));
                    }
                    getDataProducto();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataProducto() {
        list = (RecyclerView) mFA.findViewById(R.id.rv_followed);
        list_data = new ArrayList<>();
        registerForContextMenu(list);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PERSONA_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Toast.makeText(getActivity(), "FAVORITO", Toast.LENGTH_SHORT).show();
                    JSONArray data = response.getJSONArray("result");
                    for (int i = 0; i < data.length(); i++){
                        DataInfoSeller p = new DataInfoSeller();
                        JSONObject obj = data.getJSONObject(i);
                        for (int j = 0; j < id_seller.size(); j++){
                            if  (id_seller.get(j).equals(obj.getString("_id"))){
                                p.id = obj.getString("_id");
                                p.name = obj.getString("name");
                                p.avatar = obj.getString("avatar");
                                p.location = obj.getString("street");
                                p.phone = obj.getString("phone");
                                id_p = obj.getString("_id");
                                list_data.add(p);
                            }
                        }
                    }
                    AdapterSeller adapter = new AdapterSeller(list_data, getActivity());

                    adapter.setOnclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SellerActivity.class);
                            intent.putExtra("idpeople", list_data.get(list.getChildAdapterPosition(v)).getId());
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
