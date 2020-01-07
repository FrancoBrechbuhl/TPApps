package com.B3B.farmbros.retrofit;

import android.util.Log;

import com.B3B.farmbros.domain.Consulta;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsultaRepository {
    private static ConsultaRepository _INSTANCE;
    public static String _SERVER = "http://10.0.2.2:5000/";
    private List<Consulta> listaConsultas;
    private Consulta consulta;

    //Retrofit

    private Retrofit rf;
    private ConsultaRest consultaRest;

    private ConsultaRepository(){

    }

    public static ConsultaRepository getInstance(){
        if(_INSTANCE==null){
            _INSTANCE = new ConsultaRepository();
            _INSTANCE.configurarRetrofit();
            _INSTANCE.listaConsultas = new ArrayList<>();
        }
        return _INSTANCE;
    }

    private void configurarRetrofit (){
        this.rf = new Retrofit.Builder()
                .baseUrl(_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Retrofit","New instance");
        this.consultaRest = this.rf.create(ConsultaRest.class);
    }

    public void listarConsultas(){
        Call<List<Consulta>> llamada = this.consultaRest.listarTodas();
        llamada.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if(response.isSuccessful()){
                    listaConsultas.clear();
                    listaConsultas.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
    }
}
