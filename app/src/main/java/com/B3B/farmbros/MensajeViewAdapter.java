package com.B3B.farmbros;

import android.content.Context;
import android.util.Log;
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

    public MensajeViewAdapter(Context context, List<Mensaje> mensajes, ChatsActivity activity){
        this.context = context;
        this.listaMensajes = mensajes;
        this.chatsActivity = activity;
        Log.d("creacion", "adapter");
    }

    @Override
    public int getItemCount(){
        return listaMensajes.size();
    }

    @Override
    public int getItemViewType(int position){
        Log.d("posicion: ", String.valueOf(position));
        Mensaje mensaje = listaMensajes.get(position);
        Log.d("mensaje: ", mensaje.getDatos());

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

        if(mensaje.getRemitente().equals(account.getEmail())){
            Log.d("retorno", "mensaje enviado");
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else{
            Log.d("retorno", "mensaje recibido");
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view;

        Log.d("viewType: ", String.valueOf(viewType));
        Log.d("se ejecuta", "onCreateViewHolder");

        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mensajes_enviados, parent, false);
            MensajeEnviadoViewHolder vh = new MensajeEnviadoViewHolder(view);
            Log.d("Ejecuto", "view holder enviado");
            return vh;
        }
        else if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mensajes_recibidos, parent, false);
            MensajeRecibidoViewHolder vh = new MensajeRecibidoViewHolder(view);
            Log.d("Ejecuto", "view holder recibido");
            return vh;
        }

        Log.d("No ejecuto", "no corresponde a ningun view holder");

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        Mensaje mensaje = listaMensajes.get(position);

        if(holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT){
            Log.d("Ejecutado", "view holder mensaje enviado");
            ((MensajeEnviadoViewHolder) holder).setTextoMensaje(mensaje.getDatos());
            ((MensajeEnviadoViewHolder) holder).setHoraMensaje(mensaje.getHoraCreacion());
        }
        else if(holder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED){
            Log.d("Ejecutado", "view holder mensaje recibido");
            ((MensajeRecibidoViewHolder) holder).setTextoMensaje(mensaje.getDatos());
            ((MensajeRecibidoViewHolder) holder).setHoraMensaje(mensaje.getHoraCreacion());
        }
    }

}
