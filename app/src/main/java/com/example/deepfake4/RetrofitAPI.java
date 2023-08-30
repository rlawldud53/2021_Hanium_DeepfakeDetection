package com.example.deepfake4;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @GET("/posts")
    Call<List<Post>> getData(@FieldMap HashMap<String, Object> param);
    @POST("/posts")
    Call<Post> postData(@Body HashMap<String, Object> param);
}
