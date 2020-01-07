package com.B3B.farmbros.retrofit;

import com.B3B.farmbros.domain.Consulta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConsultaRest {
    @POST("consultas/")
    Call<Consulta> crearConsulta(@Body Consulta c);

    @GET("consultas/")
    Call<List<Consulta>> listarTodas();

    @GET("consultas/{id}")
    Call<Consulta> buscarConsultaPorID(@Path("id") Integer id);

    @GET("consultas/")
    Call<List<Consulta>> buscarConsultaPorProductor(@Query("remitenteConsulta") String remitente);

    @PUT("consultas/{ID}")
    Call<Consulta> actualizarConsulta(@Path("ID") Integer id, @Body Consulta consulta);

    @DELETE("consultas/{ID}")
    Call<Void> eliminarConsulta(@Path("ID") Integer id);
}
