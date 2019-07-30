package com.example.app_tiendamovil.Collection.Seller;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.favorites.DataInfoFavorite;
import com.example.app_tiendamovil.Collection.product.ProductSellerActivity;
import com.example.app_tiendamovil.R;

import java.util.ArrayList;

public class AdapterProductSeller extends RecyclerView.Adapter<AdapterProductSeller.ViewHolder>{
    private ArrayList<DataInfoFavorite> listDatos;
    private Context context;

    private View.OnCreateContextMenuListener listener;

    public AdapterProductSeller(ArrayList<DataInfoFavorite> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterProductSeller.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_seller, viewGroup, false);
        //view.setOnCreateContextMenuListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductSeller.ViewHolder viewHolder, int i) {
        viewHolder.asignarDatos(listDatos.get(i));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    /*public void setOnclickListener (View.OnCreateContextMenuListener listener){
        this.listener = listener;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (listener != null) {
            listener.onCreateContextMenu(menu, v, menuInfo);
        }
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, title, quantity, price;
        ImageView img;

        public ViewHolder(View itemView){
            super(itemView);
            //name = itemView.findViewById(R.id.name_p_seller);
            title = itemView.findViewById(R.id.title_p_seller);
            quantity = itemView.findViewById(R.id.quantity_p_seller);
            price = itemView.findViewById(R.id.price_p_seller);
            img = itemView.findViewById(R.id.img_p_seller);

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
        }
    }
}
