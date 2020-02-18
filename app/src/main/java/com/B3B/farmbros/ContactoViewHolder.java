package com.B3B.farmbros;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactoViewHolder extends RecyclerView.ViewHolder {
    private TextView nombreContacto;
    private TextView emailContacto;
    Button btnVerConversacion;

    public ContactoViewHolder(@NonNull View itemView){
        super(itemView);
        this.nombreContacto = itemView.findViewById(R.id.txtNombreContacto);
        this.emailContacto = itemView.findViewById(R.id.txtEmailContacto);
        this.btnVerConversacion = itemView.findViewById(R.id.btnVerConversacion);
    }

    public void setNombreContacto(String nombre){
        this.nombreContacto.setText(nombre);
    }

    public void setEmailContacto(String email){
        this.emailContacto.setText(email);
    }
}
