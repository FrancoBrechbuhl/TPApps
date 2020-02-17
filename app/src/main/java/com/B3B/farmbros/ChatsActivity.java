package com.B3B.farmbros;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Mensaje;
import com.B3B.farmbros.retrofit.MensajeRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMensajes;
    private RecyclerView.Adapter adapterMensajes;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private TextView textoMensaje;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_mensajes);
        /*
        setContentView(R.layout.activity_chats);
        btnEnviar = findViewById(R.id.btnEnviarMensaje);
        textoMensaje = findViewById(R.id.edit_chat);

         */

        List<Mensaje> mensajes = new ArrayList<Mensaje>();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        final String emailEmisor = account.getEmail();
        final String emailReceptor = getIntent().getExtras().getString("email productor");

        List<Mensaje> lista = MensajeRepository.getInstance().getListaMensajes();
        Log.d("Size: ", String.valueOf(lista.size()));
        for(Mensaje m : lista){
            Log.d("message: ", m.getDatos());
        }
        mensajes.addAll(lista);

        /*
        recyclerViewMensajes = findViewById(R.id.recyclerChats);
        recyclerViewMensajes.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMensajes.setLayoutManager(mLayoutManager);

        adapterMensajes = new MensajeViewAdapter(getApplicationContext(), mensajes, this);
        recyclerViewMensajes.setAdapter(adapterMensajes);
        adapterMensajes.notifyDataSetChanged();

         */

        recyclerViewMensajes = findViewById(R.id.recyclerListaMensajes);
        recyclerViewMensajes.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMensajes.setLayoutManager(mLayoutManager);

        mAdapter = new MensajeAdapter(mensajes, getApplicationContext(), this);
        recyclerViewMensajes.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        /*
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datos = textoMensaje.getText().toString();
                Mensaje mensaje = new Mensaje();
                mensaje.setDatos(datos);
                mensaje.setHoraCreacion(System.currentTimeMillis());
                mensaje.setRemitente(emailEmisor);
                mensaje.setReceptor(emailReceptor);
                textoMensaje.setText("");

                MensajeRepository.getInstance().crearMensaje(mensaje);
                adapterMensajes.notifyDataSetChanged();
            }
        });

         */
    }

    /*
    @Override
    public void onBackPressed(){
        String profesion = getIntent().getExtras().getString("profesion");
        Intent i1 = new Intent(getApplicationContext(), DetalleConsultaActivity.class);
        i1.putExtra("profesion", profesion);
        startActivity(i1);
    }

     */
}
