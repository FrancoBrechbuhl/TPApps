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
    Call<Consulta> buscarConsultaPorIdConsulta(@Path("id") Integer id);

    @GET("consultas/")
    Call<List<Consulta>> buscarConsultasPorProductor(@Query("remitenteConsulta.email") String remitente);

    @GET("consultas/")
    Call<List<Consulta>> buscarConsultasPorProductoryAsunto(@Query("remitenteConsulta.email") String remitente, @Query("asuntoConsulta") String asunto);

    @GET("consultas/")
    Call<List<Consulta>> buscarConsultasPorIngeniero(@Query("encargadoConsulta.email") String encargado);

    @GET("consultas/")
    Call<List<Consulta>> buscarConsultasPorIngenieroyAsunto(@Query("encargadoConsulta.email") String encargado, @Query("asuntoConsulta") String asunto);

    @GET("consultas/")
    Call<List<Consulta>> buscarConsultasPorAsunto(@Query("asuntoConsulta") String asunto);

    @PUT("consultas/{id}")
    Call<Consulta> actualizarConsulta(@Path("id") Integer id, @Body Consulta consulta);

    @DELETE("consultas/{ID}")
    Call<Void> eliminarConsulta(@Path("ID") Integer id);
}
