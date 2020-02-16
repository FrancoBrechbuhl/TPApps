package com.B3B.farmbros;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Mensaje;

public class MensajeEnviadoViewHolder extends RecyclerView.ViewHolder {
    private TextView textoMensaje;
    private TextView horaMensaje;

    public MensajeEnviadoViewHolder(@NonNull View itemView){
        super(itemView);
        textoMensaje = (TextView) itemView.findViewById(R.id.txtMensajeEnviado);
        horaMensaje = (TextView) itemView.findViewById(R.id.txtHoraMensajeEnviado);
    }

    void bind(Mensaje mensaje){
        textoMensaje.setText(mensaje.getDatos());
        horaMensaje.setText(DateUtils.formatElapsedTime(mensaje.getHoraCreacion()));
    }
}
