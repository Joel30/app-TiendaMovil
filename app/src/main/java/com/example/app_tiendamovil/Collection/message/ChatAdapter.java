package com.example.app_tiendamovil.Collection.message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    //private List<DataInfoChat> MessageList;

    /*public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;
        public TextView date;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.chat_nickname);
            message = (TextView) view.findViewById(R.id.chat_message);
            date = (TextView) view.findViewById(R.id.chat_date);
            image = (ImageView) view.findViewById(R.id.chat_msn_image);
        }
    }
    public ChatAdapter(List<DataInfoChat>MessagesList) {
        this.MessageList = MessagesList;

    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.MyViewHolder holder, final int position) {
        final DataInfoChat m = MessageList.get(position);
        holder.nickname.setText(m.getNickname());
        holder.message.setText(m.getMessage());
        holder.date.setText(m.getDate().substring(11,16));
        Glide.with (holder).load (Utils.HOST + dataInfo.getAvatar()).placeholder(R.drawable.profiledefault).error(R.drawable.profiledefault).circleCrop().into(img);


    }*/

    //////////////////////
    private List<DataInfoChat> listDatos;
    private Context context;

    private View.OnClickListener listener;

    public ChatAdapter(List<DataInfoChat> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder viewHolder, int i) {
        viewHolder.asignarDatos(listDatos.get(i));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;
        public TextView date;
        public ImageView image;

        public ViewHolder(View itemView){
            super(itemView);

            nickname = itemView.findViewById(R.id.chat_nickname);
            message = itemView.findViewById(R.id.chat_message);
            date = itemView.findViewById(R.id.chat_date);
            image = itemView.findViewById(R.id.chat_msn_image);
        }

        public void asignarDatos(final DataInfoChat dataInfo) {
            nickname.setText(dataInfo.getNickname());
            message.setText(dataInfo.getMessage());
            date.setText(dataInfo.getDate());
            Glide.with (context).load (Utils.HOST + dataInfo.getAvatar()).placeholder(R.drawable.profiledefault).error(R.drawable.profiledefault).circleCrop().into(image);

        }
    }

}