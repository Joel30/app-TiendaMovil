package com.example.app_tiendamovil.Collection.product;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterNewProduct extends /*BaseAdapter*/RecyclerView.Adapter<AdapterNewProduct.ViewHolder> /*implements View.OnCreateContextMenuListener */{


    ArrayList <MyProductDataInfo> listDatos;
    Context context;
    private int position;

    private View.OnCreateContextMenuListener listener;

    // Obtiene la posicio de cada item de reciclerView
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public AdapterNewProduct(ArrayList<MyProductDataInfo> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_new_product, viewGroup, false);
        //view.setOnCreateContextMenuListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.asignarDatos(listDatos.get(i));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    /*public void setOnclickListener (View.OnCreateContextMenuListener listener){
        this.listener = listener;
    }
    //@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (listener != null) {
            listener.onCreateContextMenu(menu, v, menuInfo);
        }
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {
        TextView title;
        TextView date;
        TextView quantity;
        TextView price;
        ImageView img;

        public ViewHolder(View itemView){
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            title = itemView.findViewById(R.id.title_new_product);
            date = itemView.findViewById(R.id.date_new_product);
            quantity = itemView.findViewById(R.id.quantity_new_product);
            price = itemView.findViewById(R.id.price_new_product);
            img = itemView.findViewById(R.id.img_new_product);

        }

        public void asignarDatos(final MyProductDataInfo myProductDataInfo) {
            title.setText(myProductDataInfo.getTitle());

            date.setText(myProductDataInfo.getDate().substring(0,10));
            quantity.setText(myProductDataInfo.getQuantity());
            price.setText(myProductDataInfo.getPrice() + " Bs.");
            Glide.with (context).load (Utils.HOST + myProductDataInfo.getImage()).placeholder(R.drawable.defaultimage).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ProductActivity.class);
                    intent.putExtra("id", myProductDataInfo.getId());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_product, menu);
            menu.setHeaderTitle(title.getText());
           // popupMenu.setOnMenuItemClickListener(this);
            //popupMenu.show();
            setPosition(getAdapterPosition());
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }


    }

}

