package com.B3B.farmbros;

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
import com.B3B.farmbros.util.SortMessageByTimeStamp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMensajes;
    private RecyclerView.Adapter adapterMensajes;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textoMensaje;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chats);
        btnEnviar = findViewById(R.id.btnEnviarMensaje);
        textoMensaje = findViewById(R.id.edit_chat);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        List<Mensaje> mensajes = new ArrayList<Mensaje>();

        //Revisar que cambios hacer para que funcione con productores e ingenieros
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        final String emailEmisor = account.getEmail();
        final String emailReceptor = getIntent().getExtras().getString("email productor");

        List<Mensaje> listaAux = MensajeRepository.getInstance().getListaMensajes();
        List<Mensaje> lista = ordenarMensajes(listaAux);

        Log.d("Size: ", String.valueOf(lista.size()));
        for(Mensaje m : lista){
            Log.d("texto mensaje: ", m.getDatos());
        }
        mensajes.addAll(lista);

        recyclerViewMensajes = findViewById(R.id.recyclerChats);
        recyclerViewMensajes.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMensajes.setLayoutManager(mLayoutManager);

        adapterMensajes = new MensajeViewAdapter(mensajes, getApplicationContext(), this);
        recyclerViewMensajes.setAdapter(adapterMensajes);
        adapterMensajes.notifyDataSetChanged();

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
    }

    private List<Mensaje> ordenarMensajes(List<Mensaje> mensajesDesordenados){
        List<Mensaje> mensajesOrdenados = new ArrayList<>();
        int longitud = mensajesDesordenados.size();
        Log.d("Longitud", String.valueOf(longitud));
        Mensaje[] mensajes = new Mensaje[longitud];

        for (int j = 0; j < longitud; j++){
            mensajes[j] = mensajesDesordenados.get(j);
        }

        Arrays.sort(mensajes, new SortMessageByTimeStamp());

        for(int i = 0; i < mensajes.length; i++){
            mensajesOrdenados.add(mensajes[i]);
        }

        return mensajesOrdenados;
    }
}
