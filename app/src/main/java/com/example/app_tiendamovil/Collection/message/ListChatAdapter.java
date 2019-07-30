package com.example.app_tiendamovil.Collection.message;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.app_tiendamovil.Collection.Seller.DataInfoSeller;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;

import java.util.ArrayList;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.ViewHolder> implements View.OnClickListener{
    private ArrayList<DataInfoSeller> listDatos;
    private Context context;

    private View.OnClickListener listener;

    public ListChatAdapter(ArrayList<DataInfoSeller> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @NonNull
    @Override
    public ListChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_chat, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.ViewHolder viewHolder, int i) {
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
            name = itemView.findViewById(R.id.txt_list_chat_name);
            //location  = itemView.findViewById(R.id.location_item_follow);
            phone = itemView.findViewById(R.id.txt_list_chat_phone);
            img = itemView.findViewById(R.id.img_list_chat);
        }

        public void asignarDatos(final DataInfoSeller dataInfo) {
            name.setText(dataInfo.getName());
            //location.setText(dataInfo.getLocation());
            phone.setText(dataInfo.getPhone());
            Glide.with (context).load (Utils.HOST + dataInfo.getAvatar()).placeholder(R.drawable.profiledefault).error(R.drawable.profiledefault).circleCrop().into(img);

        }
    }

}
