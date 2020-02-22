package com.B3B.farmbros;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.retrofit.ConsultaRepository;

import java.util.List;

public class ConsultaViewAdapter extends RecyclerView.Adapter<ConsultaViewHolder>{

    private List<Consulta> listaConsultas;
    private Context contexto;
    private ListaConsultasActivity listaConsultasActivity;
    private ConsultasRealizadasActivity consultasRealizadasActivity;
    private ConsultasAtendidasActivity consultasAtendidasActivity;
    private int actividad;
    private String profesion;

    private static final int ACTIVITY_LISTA_CONSULTAS = 1;
    private static final int ACTIVITY_CONSULTAS_REALIZADAS = 2;
    private static final int ACTIVITY_CONSULTAS_ATENDIDAS = 3;

    public ConsultaViewAdapter(List<Consulta> listaConsultas, Context appContext, ListaConsultasActivity listaActivity, ConsultasRealizadasActivity realizadasActivity, ConsultasAtendidasActivity atendidasActivity, String profesion){
        this.listaConsultas = listaConsultas;
        this.contexto = appContext;
        this.listaConsultasActivity = listaActivity;
        this.consultasRealizadasActivity = realizadasActivity;
        this.consultasAtendidasActivity = atendidasActivity;
        this.profesion = profesion;
        if(listaConsultasActivity != null){
            actividad = ACTIVITY_LISTA_CONSULTAS;
        }
        else if(realizadasActivity != null){
            actividad = ACTIVITY_CONSULTAS_REALIZADAS;
        }
        else if(atendidasActivity != null){
            actividad = ACTIVITY_CONSULTAS_ATENDIDAS;
        }
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
        holder.setUbicacionLat(String.valueOf(consulta.getLatConsulta()));
        holder.setUbicacionLong(String.valueOf(consulta.getLngConsulta()));
        holder.setFecha(consulta.getFechaConsulta());
        holder.btnDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detalle = new Intent(contexto,DetalleConsultaActivity.class);
                detalle.putExtra("nombre productor",consulta.getRemitenteConsulta().getNombre());
                detalle.putExtra("email productor", consulta.getRemitenteConsulta().getEmail());
                detalle.putExtra("consulta",consulta.getTextoConsulta());
                detalle.putExtra("idConsulta", consulta.getId());
                detalle.putExtra("profesion", profesion);
                if (consulta.getFotoConsultaBase64()!=null)
                    detalle.putExtra("foto consulta",consulta.getFotoConsultaBase64());
                if(actividad == ACTIVITY_LISTA_CONSULTAS) {
                    listaConsultasActivity.startActivity(detalle);
                }
                else if(actividad == ACTIVITY_CONSULTAS_REALIZADAS){
                    consultasRealizadasActivity.startActivity(detalle);
                }
                else if(actividad == ACTIVITY_CONSULTAS_ATENDIDAS){
                    consultasAtendidasActivity.startActivity(detalle);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaConsultas.size();
    }
}
