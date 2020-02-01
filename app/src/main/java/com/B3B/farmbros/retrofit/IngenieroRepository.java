package com.B3B.farmbros.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.B3B.farmbros.domain.Ingeniero;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngenieroRepository {

    private static IngenieroRepository _INSTANCE;
    public static String _SERVER = "http://10.0.2.2:5000/";

    /*
    Retornos de llamadas en el handler
     */
    public static final int _POST = 100;
    public static final int _GET = 101;
    public static final int _GET_FAIL = 111;
    public static final int _UPDATE = 102;
    public static final int _DELETE = 103;
    public static final int _ERROR = 109;

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

    /*public Ingeniero buscarIngeniero(final String email) {

        Call<Ingeniero> llamada = this.ingenieroRest.buscarIngeniero(email);
        llamada.enqueue(new Callback<Ingeniero>() {
            @Override
            public void onResponse(Call<Ingeniero> call, Response<Ingeniero> response) {
                if (response.isSuccessful()) {
                    ingeniero = response.body();
                    Log.d("Request to Retrofit", "Successful");
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
    }*/

    public void buscarIngeniero(final String email, final Handler h){
        Call<List<Ingeniero>> llamada = this.ingenieroRest.buscarIngeniero(email);
        llamada.enqueue(new Callback<List<Ingeniero>>() {
            @Override
            public void onResponse(Call<List<Ingeniero>> call, Response<List<Ingeniero>> response) {
                if(response.isSuccessful()){
                    Log.d("Retrofit:","Respuesta Exitosa buscarIngeniero");
                    if (response.body().size() > 0) ingeniero = response.body().get(0);
                    else ingeniero = null;
                    Message m = new Message();
                    m.arg1 = _GET;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Ingeniero>> call, Throwable t) {
                Log.d("Fallo Retrofit:","buscarIngeniero - arg = " + email);
                Message m = new Message();
//                m.arg1 = _GET_FAIL;
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public Ingeniero getIngeniero() {
        return ingeniero;
    }
}
