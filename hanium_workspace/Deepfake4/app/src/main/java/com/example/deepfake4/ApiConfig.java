package com.example.deepfake4;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiConfig {

    @Multipart
    @POST("search2")
    Call<ServerResponse> upload(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map
    );
}
