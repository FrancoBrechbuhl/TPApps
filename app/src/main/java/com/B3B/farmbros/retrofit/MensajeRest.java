package com.B3B.farmbros.retrofit;

import com.B3B.farmbros.domain.Mensaje;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MensajeRest {
    @POST("mensajes/")
    Call<Mensaje> crearMensaje(@Body Mensaje m);

    @GET("mensajes/")
    Call<List<Mensaje>> buscarMensajesPorEmisoryReceptor(@Query("emailRemitente") String remitente, @Query("emailReceptor") String receptor);

    @GET("mensajes/")
    Call<List<Mensaje>> listarTodosPorReceptor(@Query("emailReceptor") String receptor);
}
