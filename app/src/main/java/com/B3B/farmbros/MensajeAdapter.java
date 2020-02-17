package com.B3B.farmbros;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Mensaje;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeHolder> {
    private List<Mensaje> listaMensajes;
    private Context contexto;
    private ChatsActivity chatsActivity;

    public MensajeAdapter(List<Mensaje> mensajes, Context appContext, ChatsActivity activity){
        this.listaMensajes = mensajes;
        this.contexto = appContext;
        this.chatsActivity = activity;
    }

    @NonNull
    @Override
    public MensajeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mensaje, parent, false);
        MensajeHolder vh = new MensajeHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeHolder holder, int position) {
        final Mensaje mensaje = listaMensajes.get(position);
        holder.setTexto(mensaje.getDatos());
        holder.setHora(mensaje.getHoraCreacion());
        holder.btnBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Se apreto", "boton");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }
}
