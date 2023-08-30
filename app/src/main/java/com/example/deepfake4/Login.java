package com.example.deepfake4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    Button login;
    ImageButton back;
    private EditText et_id, et_pass;

    private LoginDatabaseHelper loginDatabaseHelper;
    public static final String TABLE_NAME="login";
    SQLiteDatabase database;
    int user;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);

        user=0;

        loginDatabaseHelper = LoginDatabaseHelper.getInstance(this);
        database = loginDatabaseHelper.getWritableDatabase();

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDatabaseHelper.close();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

       // Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.risingcamp.shop/ohouse/users/login/").addConverterFactory(GsonConverterFactory.create()).build();
       // RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);


        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectData(TABLE_NAME,et_id.getText().toString());
                if(user==2){
                    Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                    startActivity(intent);
                    loginDatabaseHelper.close();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    dialog = builder.setMessage("존재하지 않는 ID입니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }


               /* String userEmail = et_id.getText().toString();
                String userPw = et_pass.getText().toString();
                HashMap<String,Object> input=new HashMap<>();
                input.put("userEmail",userEmail);
                input.put("userPw",userPw);
                retrofitAPI.postData(input).enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if(response.isSuccessful()){
                            Post data=response.body();
                            Log.d("test","post성공");
                            Log.d("test",data.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {

                    }
                });
*/
                //login();
// EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                //String userID = et_id.getText().toString();
                //String userPass = et_pass.getText().toString();
/*
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // TODO : 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                            System.out.println("hongchul" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("isSuccess");
                            if (success) { // 로그인에 성공한 경우
                                String userID = jsonObject.getString("userEmail");
                                String userPass = jsonObject.getString("userPw");

                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                                startActivity(intent);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPass", userPass);
                                startActivity(intent);
                            } else { // 로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);
*/
            }

        });
    }

    public void selectData(String tableName, String id2){
        if(database !=null){
            String sql="SELECT ID FROM " +tableName;
            Cursor cursor=database.rawQuery(sql, null);

            for(int i=0; i<cursor.getCount(); i++){
                cursor.moveToNext();
                String id=cursor.getString(0);
                 //println("["+i+"]"+id+"//"+id2);
                // Toast.makeText(this, "INSERT", Toast.LENGTH_LONG).show();
                if(id.equals(id2)){
                        user=2;
                      //  println();
                }
            }
            cursor.close();



        }
    }

   /* private void println(String data){
        textView.append(data+"\n");
    }
/*
    // 서버와 연동하기
    void login() {
        Log.w("login","로그인 하는중");
        try {
            String userEmail = et_id.getText().toString();
            String userPw = et_pass.getText().toString();
            Log.w("앱에서 보낸값",userEmail+", "+userPw);

            CustomTask task = new CustomTask();
            String result = task.execute(userEmail,userPw).get();
            Log.w("받은값",result);

            Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
            startActivity(intent);

        } catch (Exception e) {

        }
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        // doInBackground의 매개변수 값이 여러개일 경우를 위해 배열로
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("https://www.risingcamp.shop/ohouse/users/login");  // 어떤 서버에 요청할지(localhost 안됨.)
                // ex) http://123.456.789.10:8080/hello/android
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");                              //데이터를 POST 방식으로 전송합니다.
                conn.setDoOutput(true);
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                try {


               // JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("userEmail", userEmail);
                sObject.put("userPw", userPw);



         //   Log.d("JSON Test", jArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

                // 서버에 보낼 값 포함해 요청함.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                //sendMsg = "userEmail="+strings[0]+"&userPw="+strings[1]; // GET방식으로 작성해 POST로 보냄 ex) "id=admin&pwd=1234";
                osw.write(sObject);                           // OutputStreamWriter에 담아 전송
                osw.flush();

                // jsp와 통신이 잘 되고, 서버에서 보낸 값 받음.
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                } else {    // 통신이 실패한 이유를 찍기위한 로그
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 서버에서 보낸 값을 리턴합니다.
            return receiveMsg;
        }
    }*/
}
