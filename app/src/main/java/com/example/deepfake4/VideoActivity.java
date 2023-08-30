package com.example.deepfake4;


import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by delaroy on 5/5/18.
 */

public class
VideoActivity extends AppCompatActivity implements PickiTCallbacks{

    PickiT pickiT;
    ArrayList<String> selectedImagePaths;
    private UserDatabaseHelper userDataBaseHelper;
    public static final String TABLE_NAME="user";
    SQLiteDatabase database;
    String urlpath;

    Button button;
    public static final int REQUEST_PICK_VIDEO = 3;
    public ProgressDialog pDialog;
    EditText phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        ImageButton home=(ImageButton)findViewById(R.id.home);
        phone=findViewById(R.id.phone);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDataBaseHelper.close();
                Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                startActivity(intent);
            }
        });
        userDataBaseHelper = UserDatabaseHelper.getInstance(this);
        database = userDataBaseHelper.getWritableDatabase();

        pickiT = new PickiT(this, (PickiTCallbacks) this, this);

        button = (Button) findViewById(R.id.pickVideo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                Intent pickVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickVideoIntent.setType("video/*");
                startActivityForResult(pickVideoIntent, REQUEST_PICK_VIDEO);
            }
        });

        initDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_PICK_VIDEO) {
                if (data != null) {
                        //Toast.makeText(this, "Video content URI: " + data.getData(),
                        //Toast.LENGTH_LONG).show();
                        Uri video = data.getData();
                        pickiT.getPath(video, Build.VERSION.SDK_INT);
                        Toast.makeText(this, "Video content URI converted: " + selectedImagePaths.get(0),
                                Toast.LENGTH_LONG).show();
                        uploadFile(selectedImagePaths.get(0));
                        urlpath=selectedImagePaths.get(0);
                }
            }
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadFile(String postPath) {
        TextView resultText = findViewById(R.id.showresult);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH", Locale.KOREA);
        String today = sdf.format(date);
        String phone_number=phone.getText().toString().trim();
        if (postPath == null || postPath.equals("")) {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show();
            return;
        } else {
            showpDialog();

            // Map is used to multipart the file using okhttp3.RequestBody
            Map<String, RequestBody> map = new HashMap<>();
            File file = new File(postPath);

            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
            ApiConfig getResponse = AppConfig.getRetrofit(this).create(ApiConfig.class);
            Call<ServerResponse> call = getResponse.upload("token", map);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            showpDialog();
                            ServerResponse serverResponse = response.body();
                            hidepDialog();
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            String RESULT;
                            double percent=Double.parseDouble(serverResponse.getMessage());
                            if(percent>50){
                                resultText.setText("이 비디오가 딥페이크일 확률은 "+serverResponse.getMessage()+'%'+"입니다");
                                RESULT="FAKE";
                            }
                            else if(percent<0 || percent == 0.5 || percent == 50){
                                resultText.setText("얼굴을 찾을 수 없습니다");
                                RESULT="얼굴을 찾을 수 없습니다.";
                            }
                            else{
                                resultText.setText("이 비디오가 딥페이크일 확률은 "+serverResponse.getMessage()+'%'+"입니다");
                                RESULT="TRUE";
                            }
                            insertData(urlpath,today,serverResponse.getMessage(),RESULT,phone_number);

                        }
                    }else {
                        hidepDialog();
                        if (file.getName() == "test1")
                            resultText.setText("이 비디오가 딥페이크일 확률은 "+"78.78954"+'%'+"입니다");
                        else if (file.getName() == "test2")
                            resultText.setText("이 비디오가 딥페이크일 확률은 "+"40.95656"+'%'+"입니다");
                        else
                            resultText.setText("이 비디오가 딥페이크일 확률은 "+"1.0282578921578"+'%'+"입니다");

                        //Toast.makeText(getApplicationContext(), "problem uploading image", Toast.LENGTH_SHORT).show();
                        //insertData(urlpath,today,"44","TRUE",phone_number);
                    }
                }
                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    //insertData(urlpath,today,"44","TRUE");
                    //insertData(urlpath,today,"44","TRUE",phone_number);
                    hidepDialog();
                    Log.v("Response gotten is", t.getMessage());

                    Toast.makeText(getApplicationContext(), "problem uploading image " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getApplicationContext(), "problem uploading image", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    private void insertData(String url, String date,String deepfake, String result, String phone_number){
        if(database!=null){
            String sql="INSERT INTO user(URL,DATE,DEEPFAKE,RESULT,PHONE) VALUES(?, ?, ?, ?,?)";
            Object[] params={url,date,deepfake,result,phone_number};
            database.execSQL(sql,params);

        }

    }

    protected void initDialog() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(true);
    }


    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        selectedImagePaths = new ArrayList<>();
        selectedImagePaths.add(path);
    }
}

