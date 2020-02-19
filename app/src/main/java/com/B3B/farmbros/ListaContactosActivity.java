package com.B3B.farmbros;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Ingeniero;
import com.B3B.farmbros.domain.Mensaje;
import com.B3B.farmbros.retrofit.IngenieroRepository;
import com.B3B.farmbros.retrofit.MensajeRepository;

import java.util.ArrayList;
import java.util.List;

public class ListaContactosActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewContactos;
    private RecyclerView.Adapter mAdapterContactos;
    private RecyclerView.LayoutManager mLayoutManager;

    public static List<Ingeniero> contactos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //se obtienen los mensajes que se hayan enviado al productor logueado
        List<Mensaje> mensajes = MensajeRepository.getInstance().getListaMensajes();
        List<String> correosContactos = new ArrayList<>();

        //se obtienen los correos de los contactos que hayan hablado con el productor
        for (Mensaje m : mensajes){
            if(!correosContactos.contains(m.getRemitente())) {
                correosContactos.add(m.getRemitente());
            }
        }

        //se buscan los ingenieros asociados a los emails correspondientes
        for (String email : correosContactos){
            IngenieroRepository.getInstance().buscarIngeniero(email, handlerListarIngenieros);
        }

        mRecyclerViewContactos = findViewById(R.id.recyclerListaContactos);
        mRecyclerViewContactos.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewContactos.setLayoutManager(mLayoutManager);

        mAdapterContactos = new ContactoViewAdapter(contactos, getApplicationContext(), this);
        mRecyclerViewContactos.setAdapter(mAdapterContactos);
        mAdapterContactos.notifyDataSetChanged();
    }

    Handler handlerListarIngenieros = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case IngenieroRepository._GET:
                    contactos.clear();
                    contactos.add(IngenieroRepository.getInstance().getIngeniero());
                    mAdapterContactos.notifyDataSetChanged();
                    break;
                case MensajeRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"@string/error_BD",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
