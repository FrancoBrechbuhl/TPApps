package com.B3B.farmbros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Mensaje;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

public class MensajeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private List<Mensaje> listaMensajes;
    private ChatsActivity chatsActivity;

    public MensajeViewAdapter(List<Mensaje> mensajes, Context appContext, ChatsActivity activity){
        this.context = appContext;
        this.listaMensajes = mensajes;
        this.chatsActivity = activity;
    }

    @Override
    public int getItemCount(){
        return listaMensajes.size();
    }

    @Override
    public int getItemViewType(int position){
        Mensaje mensaje = listaMensajes.get(position);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

        if(mensaje.getRemitente().equals(account.getEmail())){
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view;

        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mensajes_enviados, parent, false);
            MensajeEnviadoViewHolder vh = new MensajeEnviadoViewHolder(view);
            return vh;
        }
        else if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mensajes_recibidos, parent, false);
            MensajeRecibidoViewHolder vh = new MensajeRecibidoViewHolder(view);
            return vh;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        Mensaje mensaje = listaMensajes.get(position);

        if(holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT){
            ((MensajeEnviadoViewHolder) holder).setTextoMensaje(mensaje.getDatos());
            ((MensajeEnviadoViewHolder) holder).setHoraMensaje(mensaje.getHoraCreacion());
        }
        else if(holder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED){
            ((MensajeRecibidoViewHolder) holder).setTextoMensaje(mensaje.getDatos());
            ((MensajeRecibidoViewHolder) holder).setHoraMensaje(mensaje.getHoraCreacion());
        }
    }

    public void actualizarMensajes(List<Mensaje> mensajeList){
        this.listaMensajes = mensajeList;
    }
}
