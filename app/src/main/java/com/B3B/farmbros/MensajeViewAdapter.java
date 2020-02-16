package com.B3B.farmbros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Mensaje;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

public class MensajeViewAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private List<Mensaje> listaMensajes;

    public MensajeViewAdapter(Context context, List<Mensaje> mensajes){
        this.context = context;
        this.listaMensajes = mensajes;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;

        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mensajes_enviados, parent, false);
            return new MensajeEnviadoViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mensajes_recibidos, parent, false);
            return new MensajeRecibidoViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        Mensaje mensaje = listaMensajes.get(position);

        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((MensajeEnviadoViewHolder) holder).bind(mensaje);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((MensajeRecibidoViewHolder) holder).bind(mensaje);
                break;
        }
    }

}
