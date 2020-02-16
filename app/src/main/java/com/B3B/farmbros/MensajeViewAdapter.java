package com.B3B.farmbros;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Mensaje;

import java.util.List;

public class MensajeViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Mensaje> listaMensajes;

    public MensajeViewAdapter(Context context, List<Mensaje> mensajes){
        this.context = context;
        this.listaMensajes = mensajes;
    }
}
