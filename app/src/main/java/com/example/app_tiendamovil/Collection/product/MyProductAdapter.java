package com.example.app_tiendamovil.Collection.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Fragment.product_fragment.ProductFragment;
import com.example.app_tiendamovil.Main2Activity;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyProductAdapter extends BaseAdapter implements View.OnClickListener {

    private View.OnClickListener listener;
    private Context context;
    private ArrayList<MyProductDataInfo> list;
    public MyProductAdapter(Context context, ArrayList<MyProductDataInfo> list){
        this.context = context;
        this.list = list;
    }
        // metodo de onCLick que es usado pricede el fragment (ProductFragment)
    public void setOnclickListener (View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;//this.list.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflate = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.item_product, null);
        }
        final CheckBox checkBox = convertView.findViewById(R.id.card_check_product);
        ImageView img = convertView.findViewById(R.id.card_img_product);
        TextView price = convertView.findViewById(R.id.card_price_product);
        TextView title = convertView.findViewById(R.id.card_title_product);
        Glide.with (this.context).load (Utils.HOST + this.list.get(position).getImage()).placeholder(R.drawable.defaultimage).into(img);
        price.setText(this.list.get(position).getPrice() + " Bs.");
        title.setText(this.list.get(position).getTitle());
        convertView.setOnClickListener(this);
        // controles del checkBox
        if (this.list.get(position).getCheck().equals("true")){
            checkBox.setChecked(true);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    updateData(position);
                }else {
                    deleteData(position);
                }
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

        // Actualizar la lista de favoritos
    private void updateData(final int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idproduct", MyProductAdapter.this.list.get(position).getId());

        client.put(Utils.ID_FAVORITO_SERVICE + Utils.ID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("updateFavorite")){
                    Toast.makeText(context, "Favorito Actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                if  (errorResponse.has("errVoid")){
                    insertData(position);
                } else {

                }
            }
        });
    }

    // Crear la lista de favoritos para la persona
    private void insertData(int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idproduct", MyProductAdapter.this.list.get(position).getId());
        params.add("idpeople", Utils.ID);

        client.post(Utils.FAVORITO_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("favorite")){
                    Toast.makeText(context, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(context, "Error insert", Toast.LENGTH_SHORT).show();
            }
        });
    }

        // eliminar un producto de la lista de favoritos
    private void deleteData(int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idproduct", MyProductAdapter.this.list.get(position).getId());
        client.delete(Utils.ID_FAVORITO_SERVICE + Utils.ID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("deleteFavorite")){
                    Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al Eliminar", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
