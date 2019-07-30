package com.example.app_tiendamovil.Collection.Seller;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdapterSeller extends RecyclerView.Adapter<AdapterSeller.ViewHolder> implements View.OnClickListener {
    private ArrayList<DataInfoSeller> listDatos;
    private Context context;

    private View.OnClickListener listener;

    public AdapterSeller(ArrayList<DataInfoSeller> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSeller.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_followed, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSeller.ViewHolder viewHolder, int i) {
        viewHolder.asignarDatos(listDatos.get(i));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnclickListener (View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, phone;
        ImageView img, delete;

        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name_item_follow);
            location  = itemView.findViewById(R.id.location_item_follow);
            phone = itemView.findViewById(R.id.phone_item_follow);
            img = itemView.findViewById(R.id.img_item_follow);
            delete = itemView.findViewById(R.id.imgbtn_delete_follow);
        }

        public void asignarDatos(final DataInfoSeller dataInfo) {
            name.setText(dataInfo.getName());
            location.setText(dataInfo.getLocation());
            phone.setText(dataInfo.getPhone());
            Glide.with (context).load (Utils.HOST + dataInfo.getAvatar()).placeholder(R.drawable.profiledefault).error(R.drawable.profiledefault).circleCrop().into(img);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteData(dataInfo.getId());
                }
            });
        }
    }

    // eliminar a la persona de la lista de Seguidos
    private void deleteData(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idseller", id);
        //params.add("idproduct", AdapterSeller.this.listDatos.get(position).getId());
        client.delete(Utils.ID_SEGUIDOS_SERVICE + Utils.ID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("deleteSeguir")){
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
