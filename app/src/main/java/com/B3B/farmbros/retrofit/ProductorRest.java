package com.B3B.farmbros.retrofit;

import com.B3B.farmbros.domain.Productor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductorRest {

    @POST("productores/")
    Call<Productor> crearProductor(@Body Productor productor);

    @GET("productores/")
    Call<List<Productor>> buscarProductor(@Query("email") String email);

    @PUT("productores/{id}")
    Call<Productor> actualizarProductor(@Path("id") Integer id, @Body Productor prod);
}
