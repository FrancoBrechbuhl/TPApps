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

public class ConsultasAtendidasActivity extends AppCompatActivity {
    /*
        En esta actividad se muestran las consultas que atendio un ingeniero, o
        las consultas de un productor que hayan sido contestadas
    */
    private RecyclerView mRecyclerViewConsultas;
    private RecyclerView.Adapter mAdapterConsultas;
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

        profesion = getIntent().getExtras().getString("profesion");

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
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if(profesion.equals("ingeniero")){
                    //buscar consultas que atendió el ingeniero logueado
                    String emailIngeniero = account.getEmail();
                    if(asuntoSeleccionado.equals("Todos")) {
                        ConsultaRepository.getInstance().listarConsultasPorIngeniero(emailIngeniero, handlerListarConsultas);
                    }
                    else {
                        ConsultaRepository.getInstance().listarConsultasPorIngenieroyAsunto(emailIngeniero, asuntoSeleccionado, handlerListarConsultas);
                    }
                }
                else {
                    //buscar consultas del productor que hayan sido respondidas
                    String emailProductor = account.getEmail();
                    if(asuntoSeleccionado.equals("Todos")) {
                        ConsultaRepository.getInstance().listarConsultasResueltasPorProductor(emailProductor, handlerListarConsultas);
                    }
                    else {
                        ConsultaRepository.getInstance().listarConsultasResueltasPorProductoryAsunto(emailProductor, asuntoSeleccionado, handlerListarConsultas);
                    }
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
                //TODO: cambiar para productor e ingeniero
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
                        if(profesion.equals("ingeniero")) {
                            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            String emailIngeniero = account.getEmail();
                            ConsultaRepository.getInstance().listarConsultasPorIngeniero(emailIngeniero, handlerListarConsultas);
                        }
                        else {
                            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            String emailProductor = account.getEmail();
                            ConsultaRepository.getInstance().listarConsultasResueltasPorProductor(emailProductor, handlerListarConsultas);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                    break;
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
