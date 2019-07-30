package com.example.app_tiendamovil.Fragment.TabProfile;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.product.AdapterNewProduct;
import com.example.app_tiendamovil.Collection.product.EditProductActivity;
import com.example.app_tiendamovil.Collection.product.ImageProductActivity;
import com.example.app_tiendamovil.Collection.product.MyProductDataInfo;
import com.example.app_tiendamovil.Collection.product.ProductActivity;
import com.example.app_tiendamovil.Main2Activity;
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
public class ProfileProductFragment extends Fragment implements View.OnClickListener{


    private View mFA;
    private RecyclerView list;
    private ArrayList<MyProductDataInfo> list_data;
    private AdapterNewProduct adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFA = inflater.inflate(R.layout.fragment_profile_product, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mis Productos");
        loadComponents();
        return mFA;
    }

    private void loadComponents() {

        getData();
        Button btn_create = (Button) mFA.findViewById(R.id.fbtn_create_product);
        btn_create.setOnClickListener(this);


    }

    //Obtener datos de la BD para productos
    private void getData() {
        list = (RecyclerView) mFA.findViewById(R.id.flist_product);
        list_data = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PRODUCTO_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("product");
                    for (int i = 0; i < data.length(); i++){
                        MyProductDataInfo p = new MyProductDataInfo();
                        JSONObject obj = data.getJSONObject(i);
                        if  (obj.getString("idpeople").equals(Utils.ID)){
                            p.id = obj.getString("_id");
                            p.title = obj.getString("title");
                            p.description =  obj.getString("description");
                            p.image = obj.getString("image");
                            p.quantity = obj.getString("quantity");
                            p.date = obj.getString("registerDate");
                            p.price = obj.getString("price");
                            list_data.add(p);
                        }
                    }
                    adapter = new AdapterNewProduct(list_data, getActivity());

                    /*adapter.setOnclickListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            ProfileProductFragment.super.onCreateContextMenu(menu,v,menuInfo);

                            MenuInflater inflater = getActivity().getMenuInflater();
                            inflater.inflate(R.menu.menu_product, menu);
                            menu.setHeaderTitle(list_data.get(list.getChildAdapterPosition(v)).getTitle());
                            id = list_data.get(adapter.setPosition(adapter.));
                        }
                    });*/
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

    private void deleteData(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.delete(Utils.ID_PRODUCTO_SERVICE + id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("deleteProduct")){
                    Toast.makeText(getActivity(), "Producto Eliminado", Toast.LENGTH_SHORT).show();
                    Utils.startTab = 1;
                    Intent intent = new Intent(getActivity(), Main2Activity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Error al Eliminar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "Error al eliminar producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fbtn_create_product){
            Intent intent = new Intent(getActivity(), ImageProductActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if  (list_data .size() != 0) {
            String id = list_data.get(adapter.getPosition()).getId();
            switch (item.getItemId()){
                case R.id.see_product: {
                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    break;
                }
                case R.id.edit_product: {
                    Intent intent = new Intent(getActivity(), EditProductActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    break;
                }
                case R.id.delete_product: {
                    Toast.makeText(getActivity(), "EL ID: " + id, Toast.LENGTH_LONG).show();
                    deleteData(id);
                    break;
                }
                default:
                    Toast.makeText(getActivity(), "Jhonattan Joel", Toast.LENGTH_SHORT).show(); break;
            }

            return true;
        } else {
            Toast.makeText(getActivity(), "ERROR : FRAGMEN MIS PRODUCTOS", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
