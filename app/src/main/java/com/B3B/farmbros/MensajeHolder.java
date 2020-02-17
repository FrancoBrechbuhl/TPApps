package com.B3B.farmbros;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MensajeHolder extends RecyclerView.ViewHolder {
    private TextView texto;
    private TextView hora;
    Button btnBoton;

    public MensajeHolder(@NonNull View itemView){
        super(itemView);
        texto = itemView.findViewById(R.id.txtTexto);
        hora = itemView.findViewById(R.id.txtHora);
        btnBoton = itemView.findViewById(R.id.btnAuxiliar);
    }

    public void setTexto(String texto) {
        this.texto.setText(texto);
    }

    public void setHora(Long hora){
        this.hora.setText(DateUtils.formatElapsedTime(hora));
    }
}
