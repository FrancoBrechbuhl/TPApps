package com.B3B.farmbros;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Mensaje;

public class MensajeRecibidoViewHolder extends RecyclerView.ViewHolder {
    private TextView textoMensaje;
    private TextView horaMensaje;

    public MensajeRecibidoViewHolder(@NonNull View itemView){
        super(itemView);
        textoMensaje = (TextView) itemView.findViewById(R.id.txtMensajeRecibido);
        horaMensaje = (TextView) itemView.findViewById(R.id.txtHoraMensajeRecibido);
    }

    public void setTextoMensaje(String texto){
        textoMensaje.setText(texto);
        Log.d("llego", "seteo de mensaje recibido");
    }

    public void setHoraMensaje(Long hora){
        horaMensaje.setText(DateUtils.formatElapsedTime(hora));
        Log.d("llego", "seteo de mensaje recibido");
    }
}
