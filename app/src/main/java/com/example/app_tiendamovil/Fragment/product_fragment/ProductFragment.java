package com.example.app_tiendamovil.Fragment.product_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.product.MyProductAdapter;
import com.example.app_tiendamovil.Collection.product.MyProductDataInfo;
import com.example.app_tiendamovil.Collection.product.ProductSellerActivity;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class ProductFragment extends Fragment{


    private ArrayList<String> id_products;
    private View mFA;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFA = inflater.inflate(R.layout.fragment_product, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Productos");

        loadComponents();
        return mFA;
    }

    private void loadComponents() {
        id_products = new ArrayList<>();
        getDataFavorite();

    }
        //Buscar los productos favoritos
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
                    getData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                getData();
                Toast.makeText(getActivity(), id_products.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
        // Obtener productos de la vase de datos
    public void getData() {
        final GridView list = (GridView) mFA.findViewById(R.id.grid_product);
        final ArrayList<MyProductDataInfo> list_data = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PRODUCTO_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("product");
                    for (int i = 0; i < data.length(); i++){
                        MyProductDataInfo p = new MyProductDataInfo();
                        JSONObject obj = data.getJSONObject(i);
                        if  (!obj.getString("idpeople").equals(Utils.ID)){
                            int box = 0;
                            for (int j = 0; j < id_products.size(); j++){
                                if  (id_products.get(j).equals(obj.getString("_id"))){
                                    box = 1;
                                    break;
                                }
                            }
                            if (box == 1){p.check = "true";}
                            else {p.check = "false";}
                            p.id = obj.getString("_id");
                            p.title = obj.getString("title");
                            p.quantity = obj.getString("quantity");
                            p.idpeople = obj.getString("idpeople");
                            p.description =  obj.getString("description");
                            p.price = obj.getString("price");
                            p.image = obj.getString("image");
                            p.idpeople = obj.getString("idpeople");
                            list_data.add(p);
                        }
                    }
                    MyProductAdapter adapter = new MyProductAdapter(getActivity(), list_data);
                    adapter.setOnclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = list_data.get(list.getPositionForView(v)).getId();
                            String idpeople = list_data.get(list.getPositionForView(v)).getIdpeople();
                            Intent intent = new Intent(getActivity(), ProductSellerActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("idpeople", idpeople);
                            startActivity(intent);

                        }
                    });
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
