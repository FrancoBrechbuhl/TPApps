package com.B3B.farmbros;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Ingeniero;

import java.util.List;

public class ContactoViewAdapter extends RecyclerView.Adapter<ContactoViewHolder> {
    private List<Ingeniero> listaContactos;
    private Context contexto;
    private ListaContactosActivity listaContactosActivity;

    public ContactoViewAdapter(List<Ingeniero> contactos, Context appContext, ListaContactosActivity activity){
        this.listaContactos = contactos;
        this.contexto = appContext;
        this.listaContactosActivity = activity;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_contacto, parent, false);
        ContactoViewHolder vh = new ContactoViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        final Ingeniero contacto = listaContactos.get(position);
        holder.setNombreContacto(contacto.getNombre());
        holder.setEmailContacto(contacto.getEmail());
        holder.btnVerConversacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailContacto = contacto.getEmail();
                Intent i1 = new Intent(contexto, ChatsActivity.class);
                i1.putExtra("profesion","productor");
                i1.putExtra("email ingeniero", emailContacto);
                listaContactosActivity.startActivity(i1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

}
