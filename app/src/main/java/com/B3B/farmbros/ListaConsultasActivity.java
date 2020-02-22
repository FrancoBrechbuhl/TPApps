package com.B3B.farmbros;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.retrofit.ConsultaRepository;
import java.util.ArrayList;

public class ListaConsultasActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner spinnerAsunto;
    private Spinner spinnerOrdenamiento;
    private ArrayAdapter<CharSequence> adapterAsunto;
    private ArrayAdapter<CharSequence> adapterOrden;
    private String profesion;
    private String asuntoSeleccionado;
    private String ordenSeleccionado;

    public static ArrayList<Consulta> _CONSULTAS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consultas);

        spinnerAsunto = findViewById(R.id.spinnerAsuntos);
        spinnerOrdenamiento = findViewById(R.id.spinnerOrdenamiento);
        mRecyclerView = findViewById(R.id.recyclerListaConsultas);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        adapterAsunto = ArrayAdapter.createFromResource(getApplicationContext(), R.array.asuntos_para_spinner, android.R.layout.simple_spinner_item);
        adapterAsunto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAsunto.setAdapter(adapterAsunto);

        adapterOrden = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ordenes, android.R.layout.simple_spinner_item);
        adapterOrden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdenamiento.setAdapter(adapterOrden);

        spinnerAsunto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                asuntoSeleccionado = adapterView.getItemAtPosition(i).toString();
                //TODO: hacer metodo en repository para buscar por asunto
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                asuntoSeleccionado = "Todos";
            }
        });

        spinnerOrdenamiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ordenSeleccionado = adapterView.getItemAtPosition(i).toString();
                //TODO: hacer metodo en repository para ordenar
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ordenSeleccionado = "Ninguno";
            }
        });

        ConsultaRepository.getInstance().listarConsultas(handlerListarConsultas);

        profesion = getIntent().getExtras().getString("profesion");

        mAdapter = new ConsultaViewAdapter(_CONSULTAS,getApplicationContext(),this, null, null, profesion);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    Handler handlerListarConsultas = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case ConsultaRepository._GET:
                    _CONSULTAS.clear();
                    _CONSULTAS.addAll(ConsultaRepository.getInstance().getListaConsultas());
                    mAdapter.notifyDataSetChanged();
                    break;
                case ConsultaRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"Error al cargar la base de datos",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onBackPressed(){
        Intent i1 = new Intent(getApplicationContext(), Home.class);
        i1.putExtra("profesion", profesion);
        startActivity(i1);
    }
}
