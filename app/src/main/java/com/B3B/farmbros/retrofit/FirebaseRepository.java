package com.B3B.farmbros.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.domain.Mensaje;
import com.B3B.farmbros.firebase.JSONFirebase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseRepository {
    private static FirebaseRepository _INSTANCE;
    public static String _SERVER = "https://fcm.googleapis.com/fcm/";
    /*
    Retornos de llamadas en el handler
     */
    public static final int _POST = 700;
    public static final int _GET = 701;
    public static final int _UPDATE = 702;
    public static final int _DELETE = 703;
    public static final int _ERROR = 709;

    //Retrofit

    private Retrofit rf;
    private FirebaseRest firebaseRest;

    private FirebaseRepository(){

    }

    public static FirebaseRepository getInstance(){
        if(_INSTANCE==null){
            _INSTANCE = new FirebaseRepository();
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
        this.firebaseRest = this.rf.create(FirebaseRest.class);
    }

    public void sendToSync(JSONFirebase mensaje, final Handler h){
        Call<JSONFirebase> llamada = this.firebaseRest.actualizarFirebase(mensaje);
        llamada.enqueue(new Callback<JSONFirebase>() {
            @Override
            public void onResponse(Call<JSONFirebase> call, Response<JSONFirebase> response) {
                if(response.isSuccessful()){
                    Log.d("Request to Retrofit","Successful");
                    Message m = new Message();
                    m.arg1 = _POST;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<JSONFirebase> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
                Message m = new Message();
                m.arg1 = _ERROR;
                h.sendMessage(m);
            }
        });
    }
}
