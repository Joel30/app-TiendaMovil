package com.example.app_tiendamovil.Collection.Seller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.favorites.DataInfoFavorite;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PSellerActivity extends AppCompatActivity {

    private ArrayList<String> id_products;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pseller);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadComponents();
    }

    private void loadComponents() {
        id = getIntent().getStringExtra("id");
        id_products = new ArrayList<>();
        getProduct();
    }

    // Regresar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void getProduct(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PRODUCTO_SERVICE , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("product");
                    Toast.makeText(PSellerActivity.this, String.valueOf(data.length()), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i< data.length(); i++){
                        JSONObject obj = data.getJSONObject(i);
                        if (obj.getString("idpeople").equals(id)){
                            id_products.add(obj.getString("idpeople"));
                            Toast.makeText(PSellerActivity.this,obj.getString("idpeople") + " " + id , Toast.LENGTH_SHORT).show();
                        }
                    }
                    getDataProducto();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(PSellerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataProducto() {
        final RecyclerView list = (RecyclerView) findViewById(R.id.recycler_pseller);
        final ArrayList<DataInfoFavorite> list_data = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.PRODUCTO_SERVICE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("product");
                    Toast.makeText(PSellerActivity.this, "PSELLER", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < data.length(); i++){
                        DataInfoFavorite p = new DataInfoFavorite();
                        JSONObject obj = data.getJSONObject(i);
                        for (int j = 0; j < id_products.size(); j++){
                            if  (id_products.get(j).equals(obj.getString("idpeople"))){
                                p.id = obj.getString("_id");
                                p.title = obj.getString("title");
                                p.image = obj.getString("image");
                                p.quantity = obj.getString("quantity");
                                p.idPeople = obj.getString("idpeople");
                                p.price = obj.getString("price");
                                list_data.add(p);
                            }
                        }
                    }
                    AdapterProductSeller adapter = new AdapterProductSeller(list_data, PSellerActivity.this);

                 /*   adapter.setOnclickListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            //super.onCreateContextMenu(menu,v,menuInfo);
                            MenuInflater inflater = getMenuInflater();
                            inflater.inflate(R.menu.menu_product, menu);
                            menu.setHeaderTitle(list_data.get(list.getChildAdapterPosition(v)).getTitle());
                        }
                    });*/
                    list.setLayoutManager(new LinearLayoutManager(PSellerActivity.this));
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(PSellerActivity.this, "ERROR >:", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
