package com.B3B.farmbros;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConsultaViewHolder extends RecyclerView.ViewHolder {

    private TextView asunto;
    private TextView ubicacion;
    private TextView productor;
    Button btnDetalles;

    public ConsultaViewHolder(@NonNull View itemView){
        super(itemView);
        asunto = (TextView) itemView.findViewById(R.id.editAsuntoListaCons);
        ubicacion = (TextView) itemView.findViewById(R.id.editUbicacionListaCons);
        productor = (TextView) itemView.findViewById(R.id.editProductorListaCons);
        btnDetalles = (Button) itemView.findViewById(R.id.btnVerDetallesListaCons);
    }

    public void setAsunto(String asunto1){
        asunto.setText(asunto1);
    }
    public void setUbicacion(String ubicacion1){
        ubicacion.setText(ubicacion1);
    }
    public void setProductor(String productor1){
        productor.setText(productor1);
    }

}
