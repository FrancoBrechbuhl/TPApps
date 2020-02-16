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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMensajes;
    private MensajeViewAdapter adapterMensajes;
    private TextView textoMensaje;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chats);

        btnEnviar = (Button) findViewById(R.id.btnEnviarMensaje);
        textoMensaje = (TextView) findViewById(R.id.edit_chat);

        List<Mensaje> mensajes = new ArrayList<Mensaje>();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        final String emailEmisor = account.getEmail();
        final String emailReceptor = getIntent().getExtras().getString("email productor");

        MensajeRepository.getInstance().listarMensajesPorEmisoryReceptor(emailEmisor, emailReceptor);
        List<Mensaje> lista = MensajeRepository.getInstance().getListaMensajes();
        for(Mensaje m : lista){
            Log.d("message: ", m.getDatos());
        }
        Log.d("Size: ", String.valueOf(lista.size()));
        mensajes.addAll(MensajeRepository.getInstance().getListaMensajes());

        recyclerViewMensajes = (RecyclerView) findViewById(R.id.recyclerChats);
        adapterMensajes = new MensajeViewAdapter(this, mensajes);
        recyclerViewMensajes.setLayoutManager(new LinearLayoutManager(this));

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
            }
        });
    }
}
