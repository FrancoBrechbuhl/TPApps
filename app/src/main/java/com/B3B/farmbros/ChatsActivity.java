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

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.domain.EstadoConsulta;
import com.B3B.farmbros.domain.Ingeniero;
import com.B3B.farmbros.domain.Mensaje;
import com.B3B.farmbros.domain.Productor;
import com.B3B.farmbros.firebase.CuerpoNotificacionJSON;
import com.B3B.farmbros.firebase.FirebaseRepository;
import com.B3B.farmbros.firebase.NotificacionJSON;
import com.B3B.farmbros.retrofit.ConsultaRepository;
import com.B3B.farmbros.retrofit.IngenieroRepository;
import com.B3B.farmbros.retrofit.MensajeRepository;
import com.B3B.farmbros.retrofit.ProductorRepository;
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
    private String profesion;

    public static final int _FINISH = 60;

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

        profesion = getIntent().getExtras().getString("profesion");
        if(profesion.equals("productor")){
            //es productor y viene desde ContactoViewAdapter
            emailEmisor = account.getEmail();
            emailReceptor = getIntent().getExtras().getString("email ingeniero");
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
                if(profesion.equals("ingeniero")){
                    IngenieroRepository.getInstance().buscarIngeniero(emailEmisor, handlerListarMensajes);
                }
                else {
                    IngenieroRepository.getInstance().buscarIngeniero(emailReceptor, handlerListarMensajes);
                }

            }
        });
    }

    Handler handlerListarMensajes = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case MensajeRepository._POST:
                    mensajes.add(MensajeRepository.getInstance().getMensaje());
                    adapterMensajes.notifyDataSetChanged();
                    break;
                case _FINISH:
                    Log.d("HANDLER","Termino el thread secundario");
                    ((MensajeViewAdapter) adapterMensajes).actualizarMensajes(mensajes);
                    adapterMensajes.notifyDataSetChanged();
                    break;
                case IngenieroRepository._GET:
                    Log.d("HANDLER","Se busco al ingeniero");
                    //se indica que el ingeniero esta a cargo de esta consulta y la misma
                    //se marca en procesamiento
                    Ingeniero ingeniero = IngenieroRepository.getInstance().getIngeniero();
                    if(profesion.equals("ingeniero")) {
                        Consulta consulta = ConsultaRepository.getInstance().getConsulta();
                        //solo se registra como encargado a un ingeniero si no hay otro ya encargado
                        if (consulta.getEncargadoConsulta() == null) {
                            consulta.setEncargadoConsulta(ingeniero);
                        }
                        if (consulta.getEstado().equals(EstadoConsulta.ABIERTA)) {
                            consulta.setEstado(EstadoConsulta.EN_TRATAMIENTO);
                        }
                        ConsultaRepository.getInstance().actualizarConsulta(consulta);
                        ProductorRepository.getInstance().buscarProductor(emailReceptor, handlerListarMensajes);
                    }
                    else {
                        enviarMensaje(ingeniero.getToken());
                    }
                    Log.d("HANDLER","Se registro al encargado de la consulta");
                    break;
                case ProductorRepository._GET:
                    Productor productor = ProductorRepository.getInstance().getProductor();
                    enviarMensaje(productor.getToken());
                    break;
                case MensajeRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"Error al cargar la base de datos",Toast.LENGTH_SHORT).show();
                    break;
                case IngenieroRepository._ERROR:
                    Log.d("HANDLER","Retorno error");
                    Toast.makeText(getApplicationContext(),"Error al cargar la base de datos",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private List<Mensaje> ordenarMensajes(List<Mensaje> mensajesDesordenados){
        List<Mensaje> mensajesOrdenados = new ArrayList<>();
        int longitud = mensajesDesordenados.size();
        Mensaje[] mensajes = new Mensaje[longitud];

        Log.d("Size: ", String.valueOf(mensajesDesordenados.size()));
        for(Mensaje m : mensajesDesordenados){
            Log.d("texto mensaje desordenado ", m.getDatos());
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

    public void buscarMensajes(final String emailEmisor, final String emailReceptor){
        final Message msg = new Message();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Mensaje> mensajesDesordenados = new ArrayList<>();
                mensajesDesordenados.addAll(MensajeRepository.getInstance().listarMensajesEmisor(emailEmisor, emailReceptor));
                mensajesDesordenados.addAll(MensajeRepository.getInstance().listarMensajesReceptor(emailEmisor, emailReceptor));
                mensajes = ordenarMensajes(mensajesDesordenados);
                Log.d("Size: ", String.valueOf(mensajes.size()));
                for(Mensaje m : mensajes){
                    Log.d("texto mensaje ordenado ", m.getDatos());
                }
                msg.arg1 = _FINISH;
                handlerListarMensajes.sendMessageAtFrontOfQueue(msg);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void enviarMensaje(String token){
        CuerpoNotificacionJSON cuerpoJson = new CuerpoNotificacionJSON();
        cuerpoJson.setTitle("FarmBros");
        cuerpoJson.setIcon("appicon");
        NotificacionJSON notificacionJSON = new NotificacionJSON();
        notificacionJSON.setTo(token);
        if(profesion.equals("productor")) {
            cuerpoJson.setBody("Se ha respondido una consulta que realizaste");
        }
        else {
            cuerpoJson.setBody("Tenes un nuevo mensaje en una de tus consultas asignadas");
        }
        notificacionJSON.setNotification(cuerpoJson);
        FirebaseRepository.getInstance().enviarNotificacion(notificacionJSON);
    }
}
