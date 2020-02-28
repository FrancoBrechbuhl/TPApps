package com.B3B.farmbros.firebase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseRest {
    @Headers({
            "Authorization: key=AIzaSyBBUDcFybSu7wY38cA8MTGIUg-IW9tmvJI",
            "Content-Type: application/json"
    })
    @POST("fcm/send")
    Call<NotificacionJSON> enviarNotificacion(@Body NotificacionJSON notificacion);
}
