package com.B3B.farmbros;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsultasRealizadasActivity extends AppCompatActivity {
    //En esta actividad se muestran las consultas realizadas por el productor logueado

    private RecyclerView mRecyclerViewConsultas;
    private RecyclerView.Adapter mAdapterConsultas;
    private RecyclerView.LayoutManager mLayoutManager;
    private String asuntoSeleccionado;
    private String ordenSeleccionado;
    private Spinner spinnerAsunto;
    private Spinner spinnerOrdenamiento;
    private ArrayAdapter<CharSequence> adapterAsunto;
    private ArrayAdapter<CharSequence> adapterOrden;


    public static ArrayList<Consulta> _CONSULTAS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consultas);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        spinnerAsunto = findViewById(R.id.spinnerAsuntos);
        spinnerOrdenamiento = findViewById(R.id.spinnerOrdenamiento);
        mRecyclerViewConsultas = findViewById(R.id.recyclerListaConsultas);
        mRecyclerViewConsultas.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewConsultas.setLayoutManager(mLayoutManager);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        final String emailProductor = account.getEmail();

        ConsultaRepository.getInstance().listarConsultasPorProductor(emailProductor, handlerListarConsultas);

        adapterAsunto = ArrayAdapter.createFromResource(getApplicationContext(), R.array.asuntos_para_spinner, android.R.layout.simple_spinner_item);
        adapterAsunto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAsunto.setAdapter(adapterAsunto);

        adapterOrden = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ordenes, android.R.layout.simple_spinner_item);
        adapterOrden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdenamiento.setAdapter(adapterOrden);

        spinnerAsunto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Se buscan las consultas segun el asunto seleccionado
                asuntoSeleccionado = adapterView.getItemAtPosition(i).toString();
                if(asuntoSeleccionado.equals("Todos")){
                    ConsultaRepository.getInstance().listarConsultasPorProductor(emailProductor, handlerListarConsultas);
                }
                else {
                    ConsultaRepository.getInstance().listarConsultasPorProductoryAsunto(emailProductor, asuntoSeleccionado, handlerListarConsultas);
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
                        mAdapterConsultas.notifyDataSetChanged();
                    }
                    else if(ordenSeleccionado.equals("Mas antiguas")){
                        ordenarConsultasAntiguas(consultasSinOrden);
                        mAdapterConsultas.notifyDataSetChanged();
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

        mAdapterConsultas = new ConsultaViewAdapter(_CONSULTAS, getApplicationContext(), null, this, null, "productor");
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
                    List<Consulta> consultasSinOrden = _CONSULTAS;
                    if(ordenSeleccionado.equals("Mas recientes")){
                        ordenarConsultasRecientes(consultasSinOrden);
                    }
                    else if(ordenSeleccionado.equals("Mas antiguas")){
                        ordenarConsultasAntiguas(consultasSinOrden);
                    }
                    mAdapterConsultas.notifyDataSetChanged();
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
}
