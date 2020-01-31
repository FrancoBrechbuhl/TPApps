package com.B3B.farmbros.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.B3B.farmbros.domain.Productor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductorRepository {

    private static ProductorRepository _INSTANCE;
    public static String _SERVER = /*"http://192.168.1.12:5000";*/"http://10.0.2.2:5000";//"http://192.168.43.100:5000";

    /*
        valores de respuesta para el handler
         */
    public final static int _AGREGAR_PROD = 200;
    public final static int _MODIFICAR_PROD = 201;
    public final static int _BUSCAR_PROD = 202;
    public final static int _BORRAR_PROD = 203;
    public final static int _ERROR_PROD = 299;

    private Productor productor;

    //Retrofit

    private Retrofit rf;
    private ProductorRest productorRest;

    private ProductorRepository(){

    }

    public static ProductorRepository getInstance(){
        if(_INSTANCE==null){
            _INSTANCE = new ProductorRepository();
            _INSTANCE.configurarRetrofit();
        }
        return _INSTANCE;
    }

    private void configurarRetrofit (){
        this.rf = new Retrofit.Builder()
                .baseUrl(_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Retrofit","New instance");
        this.productorRest = this.rf.create(ProductorRest.class);
    }

    public void crearProductor(Productor prod) {

        Call<Productor> llamada = this.productorRest.crearProductor(prod);
        llamada.enqueue(new Callback<Productor>() {
            @Override
            public void onResponse(Call<Productor> call, Response<Productor> response) {
                if (response.isSuccessful()) {
                    productor = response.body();
                    Log.d("Request to Retrofit", "Successful");
                }
            }

            @Override
            public void onFailure(Call<Productor> call, Throwable t) {
                Log.d("Request to Retrofit", "Fail");
            }
        });
    }

    public Productor buscarProductor(final String email,final Handler handler) {

        Call<Productor> llamada = this.productorRest.buscarProductor(email);
        llamada.enqueue(new Callback<Productor>() {
            @Override
            public void onResponse(Call<Productor> call, Response<Productor> response) {
                if (response.isSuccessful()) {
                    productor = response.body();
                    Log.d("Request to Retrofit", "Successful");
                    Message m = new Message();
                    m.arg1 = _BUSCAR_PROD;
                    handler.sendMessage(m);
                } else {
                    productor = null;
                    Log.d("Request to Retrofit", "Null");
                }
            }

            @Override
            public void onFailure(Call<Productor> call, Throwable t) {
                Log.d("Request to Retrofit", "Fail");
            }
        });
        return productor;
    }

    public Productor getProductor() {
        return productor;
    }
}
