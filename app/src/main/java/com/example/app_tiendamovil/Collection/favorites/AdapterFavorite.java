package com.example.app_tiendamovil.Collection.favorites;

import android.content.Context;
import android.content.Intent;
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

import com.example.app_tiendamovil.Collection.product.ProductSellerActivity;
import com.example.app_tiendamovil.Main2Activity;
import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.ViewHolder> implements View.OnClickListener {

    private ArrayList<DataInfoFavorite> listDatos;
    private Context context;

    private View.OnClickListener listener;

    public AdapterFavorite(ArrayList<DataInfoFavorite> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorite, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorite.ViewHolder viewHolder, int i) {
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
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, title, quantity, price;
        ImageView img, delete;

        public ViewHolder(View itemView){
            super(itemView);
            //name = itemView.findViewById(R.id.name_favorite);
            title = itemView.findViewById(R.id.title_favorite);
            quantity = itemView.findViewById(R.id.quantity_favorite);
            price = itemView.findViewById(R.id.price_favorite);
            img = itemView.findViewById(R.id.img_favorite);
            delete = itemView.findViewById(R.id.imgbtn_delete_favorite);

        }

        public void asignarDatos(final DataInfoFavorite dataInfo) {
            //name.setText(dataInfo.getName());
            title.setText(dataInfo.getTitle());
            quantity.setText(dataInfo.getQuantity());
            price.setText(dataInfo.getPrice() + " Bs.");
            Glide.with (context).load (Utils.HOST + dataInfo.getImage()).placeholder(R.drawable.defaultimage).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductSellerActivity.class);
                    intent.putExtra("id", dataInfo.getId());
                    intent.putExtra("idpeople", dataInfo.getIdPeople());
                    context.startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteData(dataInfo.getId());
                }
            });
        }
    }

    // eliminar un producto de la lista de favoritos
    private void deleteData(String position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("idproduct", position);
        //params.add("idproduct", AdapterFavorite.this.listDatos.get(position).getId());
        client.delete(Utils.ID_FAVORITO_SERVICE + Utils.ID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("deleteFavorite")){
                    Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();
                    Utils.startTab = 4;
                    Intent intent = new Intent(context, Main2Activity.class);
                    context.startActivity(intent);
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
