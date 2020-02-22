package com.B3B.farmbros;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsultaViewHolder extends RecyclerView.ViewHolder {

    private TextView asunto;
    private TextView ubicacionLat;
    private TextView ubicacionLong;
    private TextView productor;
    private TextView fecha;
    Button btnDetalles;

    public ConsultaViewHolder(@NonNull View itemView){
        super(itemView);
        asunto = itemView.findViewById(R.id.editAsuntoListaCons);
        ubicacionLat = itemView.findViewById(R.id.editLatListaCons);
        ubicacionLong = itemView.findViewById(R.id.editLongListaCons);
        productor = itemView.findViewById(R.id.editProductorListaCons);
        fecha = itemView.findViewById(R.id.editFechaConsulta);
        btnDetalles = itemView.findViewById(R.id.btnVerDetallesListaCons);
    }

    public void setAsunto(String asunto1){
        asunto.setText(asunto1);
    }
    public void setUbicacionLat(String lat){
        ubicacionLat.setText(lat);
    }
    public void setUbicacionLong(String longitud){
        ubicacionLong.setText(longitud);
    }
    public void setProductor(String productor1){
        productor.setText(productor1);
    }
    public void setFecha(long fecha) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(fecha));
        this.fecha.setText(date);
    }
}
