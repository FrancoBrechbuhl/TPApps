package com.B3B.farmbros.retrofit;

import com.B3B.farmbros.domain.Ingeniero;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IngenieroRest {

    @POST("ingenieros/")
    Call<Ingeniero> crearIngeniero(@Body Ingeniero ingeniero);

    @GET("ingenieros/")
    Call<List<Ingeniero>> buscarIngeniero(@Query("email") String email);

    @PUT("ingenieros/{id}")
    Call<Ingeniero> actualizarIngeniero(@Path("id") Integer id, @Body Ingeniero ing);

}
