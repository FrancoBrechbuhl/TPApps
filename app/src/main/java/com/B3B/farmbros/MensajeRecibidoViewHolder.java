package com.B3B.farmbros;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MensajeRecibidoViewHolder extends RecyclerView.ViewHolder {
    private TextView textoMensaje;
    private TextView horaMensaje;

    public MensajeRecibidoViewHolder(@NonNull View itemView){
        super(itemView);
        textoMensaje = itemView.findViewById(R.id.txtMensajeRecibido);
        horaMensaje = itemView.findViewById(R.id.txtHoraMensajeRecibido);
    }

    public void setTextoMensaje(String texto){
        textoMensaje.setText(texto);
    }

    public void setHoraMensaje(long hora){
        String date = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss").format(new Date(hora));
        horaMensaje.setText(date);
    }
}
