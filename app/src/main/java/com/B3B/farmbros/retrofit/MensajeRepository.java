package com.B3B.farmbros.retrofit;

import android.util.Log;

import com.B3B.farmbros.domain.Mensaje;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MensajeRepository {
    private static MensajeRepository _INSTANCE;
    public static String _SERVER = /*"http://10.0.2.2:5000";*/"http://192.168.43.100:5000";
    private List<Mensaje> listaMensajes;
    private Mensaje mensaje;

    private Retrofit rf;
    private MensajeRest mensajeRest;

    public MensajeRepository(){

    }

    public static MensajeRepository getInstance(){
        if(_INSTANCE==null){
            _INSTANCE = new MensajeRepository();
            _INSTANCE.configurarRetrofit();
            _INSTANCE.listaMensajes = new ArrayList<>();
        }
        return _INSTANCE;
    }

    private void configurarRetrofit (){
        this.rf = new Retrofit.Builder()
                .baseUrl(_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Retrofit","New instance");
        this.mensajeRest = this.rf.create(MensajeRest.class);
    }

    public void crearMensaje(Mensaje m){
        Call<Mensaje> llamada = this.mensajeRest.crearMensaje(m);
        llamada.enqueue(new Callback<Mensaje>() {
            @Override
            public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                if(response.isSuccessful()){
                    listaMensajes.add(response.body());
                    Log.d("Request to Retrofit","Successful");
                }
            }

            @Override
            public void onFailure(Call<Mensaje> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
    }

    public void listarMensajes(){
        Call<List<Mensaje>> llamada = this.mensajeRest.listarTodosPorUsuarioTextoyHora();
        llamada.enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                if(response.isSuccessful()){
                    listaMensajes.clear();
                    listaMensajes.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
    }

    public void listarMensajesPorEmisoryReceptor(String emisor, String receptor){
        Call<List<Mensaje>> llamada = this.mensajeRest.buscarMensajesPorEmisoryReceptor(emisor, receptor);
        llamada.enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                if(response.isSuccessful()){
                    listaMensajes.clear();
                    listaMensajes.addAll(response.body());
                    Log.d("Request to Retrofit", "Successful");
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
    }

    public void listarMensajesPorReceptoryEmisor(String receptor, String emisor){
        Call<List<Mensaje>> llamada = this.mensajeRest.buscarMensajesPorEmisoryReceptor(receptor, emisor);
        llamada.enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                if(response.isSuccessful()){
                    listaMensajes.addAll(response.body());
                    Log.d("Request to Retrofit", "Successful");
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                Log.d("Request to Retrofit","Fail");
            }
        });
    }

    public List<Mensaje> getListaMensajes() {
        return listaMensajes;
    }
}
