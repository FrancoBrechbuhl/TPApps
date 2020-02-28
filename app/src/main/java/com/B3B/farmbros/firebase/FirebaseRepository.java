package com.B3B.farmbros.firebase;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseRepository {
    private static FirebaseRepository _INSTANCE;
    public static String _SERVER = "https://fcm.googleapis.com/";

    /*
    Retornos de llamadas en el handler
     */
    public static final int _POST = 700;
    public static final int _GET = 701;
    public static final int _GET_FAIL = 711;
    public static final int _UPDATE = 702;
    public static final int _DELETE = 703;
    public static final int _ERROR = 709;


    //Retrofit

    private Retrofit rf;
    private FirebaseRest firebaseRest;

    private FirebaseRepository() {

    }

    public static FirebaseRepository getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new FirebaseRepository();
            _INSTANCE.configurarRetrofit();
        }
        return _INSTANCE;
    }

    private void configurarRetrofit() {
        this.rf = new Retrofit.Builder()
                .baseUrl(_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.firebaseRest = this.rf.create(FirebaseRest.class);
        Log.d("Retrofit", "New instance");
    }

    public void enviarNotificacion(NotificacionJSON jsonObject) {

        Call<NotificacionJSON> llamada = this.firebaseRest.enviarNotificacion(jsonObject);
        Log.d("Headers", llamada.request().headers().toString());
        Log.d("Body", llamada.request().body().toString());
        llamada.enqueue(new Callback<NotificacionJSON>() {
            @Override
            public void onResponse(Call<NotificacionJSON> call, Response<NotificacionJSON> response) {
                if (response.isSuccessful()) {
                    Log.d("Request to Firebase", "Successful");
                }
                else {
                    Log.d("Request to Firebase", "Error");
                }
                Log.d("Code", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<NotificacionJSON> call, Throwable t) {
                Log.d("Request to Firebase", "Fail");
            }
        });
    }

}
