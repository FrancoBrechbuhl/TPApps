package com.B3B.farmbros;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Consulta;

import java.util.List;

public class ConsultaViewAdapter extends RecyclerView.Adapter<ConsultaViewHolder>{

    private List<Consulta> listaConsultas;
    private Context contexto;
    private ListaConsultasActivity listaConsultasActivity;

    public ConsultaViewAdapter(List<Consulta> listaConsultas, Context appContext, ListaConsultasActivity activity){
        this.listaConsultas = listaConsultas;
        this.contexto = appContext;
        this.listaConsultasActivity = activity;
    }

    @NonNull
    @Override
    public ConsultaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_consulta, parent, false);
        ConsultaViewHolder vh = new ConsultaViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultaViewHolder holder, int position) {
        final Consulta consulta = listaConsultas.get(position);
        holder.setAsunto(consulta.getAsuntoConsulta());
        holder.setProductor(consulta.getRemitenteConsulta().getNombre());
        holder.setUbicacion(String.valueOf(consulta.getLatConsulta()) + " " + String.valueOf(consulta.getLngConsulta()));
        holder.btnDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detalle = new Intent(contexto,DetalleConsultaActivity.class);
                detalle.putExtra("nombre productor",consulta.getRemitenteConsulta().getNombre());
                detalle.putExtra("consulta",consulta.getTextoConsulta());
                listaConsultasActivity.startActivity(detalle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaConsultas.size();
    }
}
