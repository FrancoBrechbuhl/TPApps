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

public class ConsultasRealizadasActivity extends AppCompatActivity {
    //En esta actividad se muestran las consultas realizadas por el productor logueado

    private RecyclerView mRecyclerViewConsultas;
    private RecyclerView.Adapter mAdapterConsultas;
    private RecyclerView.LayoutManager mLayoutManager;

    public static ArrayList<Consulta> _CONSULTAS= new ArrayList<>();

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
        final String emailProductor = account.getEmail();

        ConsultaRepository.getInstance().listarConsultasPorProductor(emailProductor, handlerListarConsultas);

        mAdapterConsultas = new ConsultaViewAdapter(_CONSULTAS, getApplicationContext(), null, this, "productor");
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
                    mAdapterConsultas.notifyDataSetChanged();
                    break;
                case ConsultaRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"@string/error_BD",Toast.LENGTH_SHORT).show();
            }
        }
    };

}
