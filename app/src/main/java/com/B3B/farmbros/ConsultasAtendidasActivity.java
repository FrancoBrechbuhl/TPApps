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

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.retrofit.ConsultaRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class ConsultasAtendidasActivity extends AppCompatActivity {
    /*
        En esta actividad se muestran las consultas que atendio un ingeniero, o
        las consultas de un productor que hayan sido contestadas
    */
    private RecyclerView mRecyclerViewConsultas;
    private RecyclerView.Adapter mAdapterConsultas;
    private RecyclerView.LayoutManager mLayoutManager;

    public static List<Consulta> _CONSULTAS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consultas);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mRecyclerViewConsultas = findViewById(R.id.recyclerListaConsultas);
        mRecyclerViewConsultas.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewConsultas.setLayoutManager(mLayoutManager);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        String profesion = getIntent().getExtras().getString("profesion");

        if(profesion.equals("ingeniero")){
            //buscar consultas que atendió el ingeniero logueado
            String emailIngeniero = account.getEmail();
            ConsultaRepository.getInstance().listarConsultasPorIngeniero(emailIngeniero, handlerListarConsultas);
        }
        else{
            //buscar consultas del productor que hayan sido respondidas
            String emailProductor = account.getEmail();
            ConsultaRepository.getInstance().listarConsultasResueltasPorProductor(emailProductor, handlerListarConsultas);
        }

        mAdapterConsultas = new ConsultaViewAdapter(_CONSULTAS, getApplicationContext(), null, null, this, profesion);
        mRecyclerViewConsultas.setAdapter(mAdapterConsultas);
        mAdapterConsultas.notifyDataSetChanged();
    }

    Handler handlerListarConsultas = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case ConsultaRepository._GET:
                    _CONSULTAS.clear();
                    _CONSULTAS.addAll(ConsultaRepository.getInstance().getListaConsultas());
                    //TODO: aca no se porque pero no carga la lista cuando termina la consulta
                    mAdapterConsultas.notifyDataSetChanged();
                    break;
                case ConsultaRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"Error al cargar la base de datos",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
