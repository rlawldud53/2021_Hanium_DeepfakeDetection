package com.example.deepfake4;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "https://www.risingcamp.shop/ohouse/users/login";
    private Map<String, String> map;


    public LoginRequest(String userEmail, String userPw, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("userEmail",userEmail);
        map.put("userPw", userPw);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}