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
import com.B3B.farmbros.util.SortConsultaByTimeStampAscendent;
import com.B3B.farmbros.util.SortConsultaByTimeStampDescendent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<Consulta> _CONSULTAS = new ArrayList<>();

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
                //TODO: tener en cuenta el orden tambien aca
                //Se buscan las consultas segun el asunto seleccionado
                asuntoSeleccionado = adapterView.getItemAtPosition(i).toString();
                if(asuntoSeleccionado.equals("Todos")){
                    ConsultaRepository.getInstance().listarConsultas(handlerListarConsultas);
                }
                else {
                    ConsultaRepository.getInstance().listarConsultasPorAsunto(asuntoSeleccionado, handlerListarConsultas);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerOrdenamiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Se ordenan las consultas segun el criterio seleccionado
                ordenSeleccionado = adapterView.getItemAtPosition(i).toString();
                if(!_CONSULTAS.isEmpty()){
                    List<Consulta> consultasSinOrden = _CONSULTAS;
                    if(ordenSeleccionado.equals("Mas recientes")){
                        ordenarConsultasRecientes(consultasSinOrden);
                        mAdapter.notifyDataSetChanged();
                    }
                    else if(ordenSeleccionado.equals("Mas antiguas")){
                        ordenarConsultasAntiguas(consultasSinOrden);
                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        ConsultaRepository.getInstance().listarConsultas(handlerListarConsultas);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Se buscan las consultas en el repository sin orden y con todos los asuntos
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
                    List<Consulta> consultasSinOrden = _CONSULTAS;
                    if(ordenSeleccionado.equals("Mas recientes")){
                        ordenarConsultasRecientes(consultasSinOrden);
                    }
                    else if(ordenSeleccionado.equals("Mas antiguas")){
                        ordenarConsultasAntiguas(consultasSinOrden);
                    }
                    mAdapter.notifyDataSetChanged();

                    break;
                case ConsultaRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"Error al cargar la base de datos",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void ordenarConsultasRecientes(List<Consulta> consultasDesordenadas){
        int longitud = consultasDesordenadas.size();
        Consulta[] consultasArray = new Consulta[longitud];

        for(int a = 0; a < longitud; a++){
            consultasArray[a] = consultasDesordenadas.get(a);
        }

        Arrays.sort(consultasArray, new SortConsultaByTimeStampAscendent());

        _CONSULTAS.clear();
        for (int j = 0; j < longitud; j++) {
            _CONSULTAS.add(consultasArray[j]);
        }
    }

    private void ordenarConsultasAntiguas(List<Consulta> consultasDesordenadas){
        int longitud = consultasDesordenadas.size();
        Consulta[] consultasArray = new Consulta[longitud];

        for(int a = 0; a < longitud; a++){
            consultasArray[a] = consultasDesordenadas.get(a);
        }

        Arrays.sort(consultasArray, new SortConsultaByTimeStampDescendent());

        _CONSULTAS.clear();
        for (int j = 0; j < longitud; j++) {
            _CONSULTAS.add(consultasArray[j]);
        }
    }

    @Override
    public void onBackPressed(){
        Intent i1 = new Intent(getApplicationContext(), Home.class);
        i1.putExtra("profesion", profesion);
        startActivity(i1);
    }
}
