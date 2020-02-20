package com.B3B.farmbros.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.B3B.farmbros.domain.Consulta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsultaRepository {
    private static ConsultaRepository _INSTANCE;
    public static String _SERVER = /*"http://10.0.2.2:5000";*/"http://192.168.43.100:5000";
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

    public void listarConsultas(final Handler h){
        Call<List<Consulta>> llamada = this.consultaRest.listarTodas();
        llamada.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if(response.isSuccessful()){
                    listaConsultas.clear();
                    listaConsultas.addAll(response.body());
                    Message m = new Message();
                    m.arg1 = _GET;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public void crearConsulta (Consulta consulta,final Handler h){
        Call<Consulta> llamada = this.consultaRest.crearConsulta(consulta);
        llamada.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.isSuccessful()){
                    listaConsultas.add(response.body());
                    Log.d("Request to Retrofit","Successful");
                    Message m = new Message();
                    m.arg1 = _POST;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public void buscarConsultaPorIdConsulta(final int id, final Handler h){
        Call<Consulta> llamada = this.consultaRest.buscarConsultaPorIdConsulta(id);
        llamada.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.isSuccessful()){
                    consulta = response.body();
                    Log.d("Request to Retrofit","Successful");
                    Message m = new Message();
                    m.arg1 = _GET;
                    h.sendMessage(m);
                }
                else{
                    consulta = null;
                    Log.d("Request to Retrofit","Null");
                    Message m = new Message();
                    m.arg1 = _ERROR;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {
                consulta = null;
                Log.d("Request to Retrofit","Fail");
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public void listarConsultasPorProductor(String emailProductor, final Handler h){
        Call<List<Consulta>> llamada = this.consultaRest.buscarConsultaPorProductor(emailProductor);
        llamada.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if(response.isSuccessful()){
                    listaConsultas.clear();
                    listaConsultas.addAll(response.body());
                    Message m = new Message();
                    m.arg1 = _GET;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public void listarConsultasPorIngeniero(String emailIngeniero, final Handler h){
        Call<List<Consulta>> llamada = this.consultaRest.buscarConsultaPorIngeniero(emailIngeniero);
        llamada.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if(response.isSuccessful()){
                    listaConsultas.clear();
                    listaConsultas.addAll(response.body());
                    Message m = new Message();
                    m.arg1 = _GET;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public void listarConsultasResueltasPorProductor(String emailProductor, final Handler h){
        Call<List<Consulta>> llamada = this.consultaRest.buscarConsultaPorProductor(emailProductor);
        llamada.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if(response.isSuccessful()){
                    listaConsultas.clear();
                    for(Consulta consulta : response.body()){
                        if(consulta.getEncargadoConsulta() != null){
                            listaConsultas.add(consulta);
                        }
                    }
                    Message m = new Message();
                    m.arg1 = _GET;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public void borrarConsulta (final Consulta consulta){
        Call<Void> llamada = this.consultaRest.eliminarConsulta(consulta.getId());
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

    public void actualizarConsulta(final Consulta consulta){
        Call<Consulta> llamada = this.consultaRest.actualizarConsulta(consulta.getId(), consulta);
        llamada.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.isSuccessful()){
                    Log.d("Request to Retrofit","Successful");
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {
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
