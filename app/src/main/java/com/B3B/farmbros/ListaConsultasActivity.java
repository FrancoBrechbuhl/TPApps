package com.B3B.farmbros;

import android.content.BroadcastReceiver;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.retrofit.ConsultaRepository;

import java.util.ArrayList;

public class ListaConsultasActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerListaConsultas);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        _CONSULTAS = (ArrayList<Consulta>) ConsultaRepository.getInstance().getListaConsultas();
        if (_CONSULTAS.isEmpty()) {
            ConsultaRepository.getInstance().listarConsultas();
            _CONSULTAS = (ArrayList<Consulta>) ConsultaRepository.getInstance().getListaConsultas();
        }

        mAdapter = new ConsultaViewAdapter(_CONSULTAS,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
