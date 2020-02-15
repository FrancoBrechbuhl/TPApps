package com.B3B.farmbros.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.B3B.farmbros.domain.Ingeniero;
import com.B3B.farmbros.domain.Productor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductorRepository {

    private static ProductorRepository _INSTANCE;
    public static String _SERVER = "http://192.168.1.112:5000/";
    /*
    Retornos de llamadas en el handler
     */
    public static final int _POST = 200;
    public static final int _GET = 201;
    public static final int _GET_FAIL = 211;
    public static final int _UPDATE = 202;
    public static final int _DELETE = 203;
    public static final int _ERROR = 209;

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

    public void buscarProductor(final String email, final Handler h){
        Call<List<Productor>> llamada = this.productorRest.buscarProductor(email);
        llamada.enqueue(new Callback<List<Productor>>() {
            @Override
            public void onResponse(Call<List<Productor>> call, Response<List<Productor>> response) {
                if(response.isSuccessful()){
                    Log.d("Retrofit:","Respuesta Exitosa buscarProductor");
                    if (response.body().size() > 0) productor = response.body().get(0);
                    else productor = null;
                    Message m = new Message();
                    m.arg1 = _GET;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Productor>> call, Throwable t) {
                Log.d("Fallo Retrofit:","buscarProductor - arg = " + email);
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }

    public Productor getProductor() {
        return productor;
    }
}
