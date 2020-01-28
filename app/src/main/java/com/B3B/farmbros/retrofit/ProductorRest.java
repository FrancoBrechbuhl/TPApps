package com.B3B.farmbros.retrofit;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.domain.Ingeniero;
import com.B3B.farmbros.domain.Productor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductorRest {

    @POST("productores/")
    Call<Productor> crearProductor(@Body Productor productor);

    @GET("productores")
    Call<Productor> buscarProductor(@Query("email") String email);
}
