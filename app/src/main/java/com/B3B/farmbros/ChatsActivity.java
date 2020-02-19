package com.B3B.farmbros;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private static List<Mensaje> mensajes = new ArrayList<>();
    private TextView textoMensaje;
    private Button btnEnviar;
    private String emailEmisor;
    private String emailReceptor;

    public static final int _FINISH = 100;

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

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        String profesion = getIntent().getExtras().getString("profesion");
        if(profesion.equals("productor")){
            //es productor y viene desde ListaContactosActivity
            emailEmisor = account.getEmail();
            emailReceptor = getIntent().getExtras().getString("email productor");
        }
        else {
            //es ingeniero y viene desde DetalleConsultaActivity
            emailEmisor = account.getEmail();
            emailReceptor = getIntent().getExtras().getString("email productor");
        }

        buscarMensajes(emailEmisor, emailReceptor);

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
                MensajeRepository.getInstance().crearMensaje(mensaje, handlerListarMensajes);
                adapterMensajes.notifyDataSetChanged();
            }
        });
    }

    private List<Mensaje> ordenarMensajes(List<Mensaje> mensajesDesordenados){
        List<Mensaje> mensajesOrdenados = new ArrayList<>();
        int longitud = mensajesDesordenados.size();
        Mensaje[] mensajes = new Mensaje[longitud];

        Log.d("Size: ", String.valueOf(mensajesDesordenados.size()));
        for(Mensaje m : mensajesDesordenados){
            Log.d("texto mensaje desordenado: ", m.getDatos());
        }

        for (int j = 0; j < longitud; j++){
            mensajes[j] = mensajesDesordenados.get(j);
        }

        Arrays.sort(mensajes, new SortMessageByTimeStamp());

        for(int i = 0; i < mensajes.length; i++){
            mensajesOrdenados.add(mensajes[i]);
        }

        return mensajesOrdenados;
    }

    Handler handlerListarMensajes = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case MensajeRepository._POST:
                    mensajes.clear();
                    mensajes.addAll(MensajeRepository.getInstance().getListaMensajes());
                    adapterMensajes.notifyDataSetChanged();
                    break;
                case _FINISH:
                    Log.d("HANDLER","Termino el thread secundario");
                    mensajes.clear();
                    mensajes.addAll(MensajeRepository.getInstance().getListaMensajes());
                    ((MensajeViewAdapter) adapterMensajes).actualizarMensajes(mensajes);
                    adapterMensajes.notifyDataSetChanged();
                    break;
                case MensajeRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"@string/error_BD",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void buscarMensajes(final String emailEmisor, final String emailReceptor){
        final Message msg = new Message();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Mensaje> mensajesDesordenados;
                mensajesDesordenados = new ArrayList<>();
                mensajesDesordenados.addAll(MensajeRepository.getInstance().listarMensajesEmisor(emailEmisor, emailReceptor));
                mensajesDesordenados.addAll(MensajeRepository.getInstance().listarMensajesReceptor(emailEmisor, emailReceptor));
                mensajes = ordenarMensajes(mensajesDesordenados);
                Log.d("Size: ", String.valueOf(mensajes.size()));
                for(Mensaje m : mensajes){
                    Log.d("texto mensaje ordenado: ", m.getDatos());
                }
                msg.arg1 = _FINISH;
                handlerListarMensajes.sendMessageAtFrontOfQueue(msg);
            }
        };
        Thread t = new Thread(r);
        t.start();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {

            }
        };
        Thread t1 = new Thread(r1);
        t1.start();
    }
}
