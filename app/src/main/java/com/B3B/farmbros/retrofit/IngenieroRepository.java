package com.B3B.farmbros.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.B3B.farmbros.domain.Ingeniero;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngenieroRepository {

    private static IngenieroRepository _INSTANCE;
    public static String _SERVER = /*"http://192.168.1.12:5000";*/"http://10.0.2.2:5000";//"http://192.168.43.100:5000";

    /*
    valores de respuesta para el handler
     */
    public final static int _AGREGAR_ING = 100;
    public final static int _MODIFICAR_ING = 101;
    public final static int _BUSCAR_ING = 102;
    public final static int _BORRAR_ING = 103;
    public final static int _ERROR_ING = 199;

    private Ingeniero ingeniero;

    //Retrofit

    private Retrofit rf;
    private IngenieroRest ingenieroRest;

    private IngenieroRepository() {

    }

    public static IngenieroRepository getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new IngenieroRepository();
            _INSTANCE.configurarRetrofit();
        }
        return _INSTANCE;
    }

    private void configurarRetrofit() {
        this.rf = new Retrofit.Builder()
                .baseUrl(_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Retrofit", "New instance");
        this.ingenieroRest = this.rf.create(IngenieroRest.class);
    }

    public void crearIngeniero(Ingeniero ing) {

        Call<Ingeniero> llamada = this.ingenieroRest.crearIngeniero(ing);
        llamada.enqueue(new Callback<Ingeniero>() {
            @Override
            public void onResponse(Call<Ingeniero> call, Response<Ingeniero> response) {
                if (response.isSuccessful()) {
                    ingeniero = response.body();
                    Log.d("Request to Retrofit", "Successful");
                }
            }

            @Override
            public void onFailure(Call<Ingeniero> call, Throwable t) {
                Log.d("Request to Retrofit", "Fail");
            }
        });
    }

    public Ingeniero buscarIngeniero(final String email, final Handler handler) {

        Call<Ingeniero> llamada = this.ingenieroRest.buscarIngeniero(email);
        llamada.enqueue(new Callback<Ingeniero>() {
            @Override
            public void onResponse(Call<Ingeniero> call, Response<Ingeniero> response) {
                if (response.isSuccessful()) {
                    ingeniero = response.body();
                    Log.d("Request to Retrofit", "Successful");
                    Message m = new Message();
                    m.arg1 = _BUSCAR_ING;
                    handler.sendMessage(m);
                } else {
                    ingeniero = null;
                    Log.d("Request to Retrofit", "Null");
                }
            }

            @Override
            public void onFailure(Call<Ingeniero> call, Throwable t) {
                Log.d("Request to Retrofit", "Fail");
            }
        });
        return ingeniero;
    }

    public Ingeniero getIngeniero(){
        return ingeniero;
    }
}
