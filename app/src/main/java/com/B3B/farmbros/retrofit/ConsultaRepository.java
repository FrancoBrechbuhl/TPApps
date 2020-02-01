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
    public static String _SERVER = /*"http://192.168.1.12:5000";*/"http://10.0.2.2:5000";//"http://192.168.43.100:5000";
    private List<Consulta> listaConsultas;
    private Consulta consulta;
    /*
    Retornos de llamadas en el handler
     */
    public static final int _POST = 300;
    public static final int _GET = 301;
    public static final int _UPDATE = 302;
    public static final int _DELETE = 303;
    public static final int _ERROR = 309;

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

    public void crearConsulta (Consulta consulta){
        Call<Consulta> llamada = this.consultaRest.crearConsulta(consulta);
        llamada.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.isSuccessful()){
                    listaConsultas.add(response.body());
                    Log.d("Request to Retrofit","Successful");
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
    }

    public Consulta buscarConsultaPorID(final int id){
        Call<Consulta> llamada = this.consultaRest.buscarConsultaPorID(id);
        llamada.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.isSuccessful()){
                    consulta = response.body();
                    Log.d("Request to Retrofit","Successful");
                }
                else{
                    consulta = null;
                    Log.d("Request to Retrofit","Null");
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
        return consulta;
    }

    public void borrarConsulta (final Consulta consulta){
        Call<Void> llamada = this.consultaRest.eliminarConsulta(consulta.getIdConsulta());
        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("Request to Retrofit","Successful");
                    listaConsultas.remove(consulta);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
    }

    public List<Consulta> getListaConsultas() {
        return listaConsultas;
    }

    public Consulta getConsulta(){
        return consulta;
    }
}
