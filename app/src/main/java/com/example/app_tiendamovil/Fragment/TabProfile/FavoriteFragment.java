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

import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.favorites.AdapterFavorite;
import com.example.app_tiendamovil.Collection.favorites.DataInfoFavorite;
import com.example.app_tiendamovil.Collection.product.ProductSellerActivity;
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
public class FavoriteFragment extends Fragment implements View.OnCreateContextMenuListener {


    private View mFA;
    private ArrayList<String> id_products;
    private String name_people, id_p;
    private RecyclerView list;
    private ArrayList<DataInfoFavorite> list_data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFA = inflater.inflate(R.layout.fragment_favorite, container, false);
        loadComponents();
        return mFA;
    }

    private void loadComponents() {
        id_products = new ArrayList<>();
        getDataFavorite();
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

    private void getDataFavorite (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_FAVORITO_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("favorite");
                    JSONObject obj = data.getJSONObject(0);
                    JSONArray idproduct = obj.getJSONArray("idproduct");
                    for (int i = 0; i< idproduct.length(); i++){
                        id_products.add(idproduct.getString(i));
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
        list = (RecyclerView) mFA.findViewById(R.id.flist_favorite);
        list_data = new ArrayList<>();
        registerForContextMenu(list);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PRODUCTO_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Toast.makeText(getActivity(), "FAVORITO", Toast.LENGTH_SHORT).show();
                    JSONArray data = response.getJSONArray("product");
                    for (int i = 0; i < data.length(); i++){
                        DataInfoFavorite p = new DataInfoFavorite();
                        JSONObject obj = data.getJSONObject(i);
                        for (int j = 0; j < id_products.size(); j++){
                            if  (id_products.get(j).equals(obj.getString("_id"))){
                                p.id = obj.getString("_id");
                                p.title = obj.getString("title");
                                p.image = obj.getString("image");
                                p.quantity = obj.getString("quantity");
                                p.idPeople = obj.getString("idpeople");
                                id_p = obj.getString("idpeople");
                                Toast.makeText(getActivity(), getDataPersona(obj.getString("idpeople")), Toast.LENGTH_SHORT).show();
                                p.name = getDataPersona(obj.getString("idpeople"));
                                p.price = obj.getString("price");
                                list_data.add(p);
                            }
                        }
                    }
                    AdapterFavorite adapter = new AdapterFavorite(list_data, getActivity());

                    adapter.setOnclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ProductSellerActivity.class);
                            intent.putExtra("id", list_data.get(list.getChildAdapterPosition(v)).getId());
                            intent.putExtra("idpeople", list_data.get(list.getChildAdapterPosition(v)).getIdPeople());
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

