package com.B3B.farmbros;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MensajeEnviadoViewHolder extends RecyclerView.ViewHolder {
    private TextView textoMensaje;
    private TextView horaMensaje;

    public MensajeEnviadoViewHolder(@NonNull View itemView){
        super(itemView);
        textoMensaje = itemView.findViewById(R.id.txtMensajeEnviado);
        horaMensaje = itemView.findViewById(R.id.txtHoraMensajeEnviado);
    }

    public void setTextoMensaje(String texto){
        textoMensaje.setText(texto);
    }

    public void setHoraMensaje(long hora){
        horaMensaje.setText(DateUtils.formatElapsedTime(hora));
    }
}
