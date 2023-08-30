package com.example.deepfake4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class RequestList extends AppCompatActivity {

    private UserDatabaseHelper userDataBaseHelper;
    public static final String TABLE_NAME="user";
    SQLiteDatabase database;
    Point size;

    //Button button1;
    ListView listView;

    ArrayList<VideoDTO> list;

    VideoAdapter adapter;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        userDataBaseHelper = UserDatabaseHelper.getInstance(this);
        database = userDataBaseHelper.getWritableDatabase();
        textView = findViewById(R.id.textView);

        ImageButton home;
        home=(ImageButton)findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userDataBaseHelper.close();
                                        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                                        startActivity(intent);
                                    }
        });

        //디바이스 사이즈 구하기
        size = getDeviceSize();

        //객체 초기화
        //button1 = findViewById(R.id.button1);
        listView = findViewById(R.id.listView);

        list = new ArrayList<>();
/*
        Uri videoURI = Uri.parse("android.resource://" + getPackageName() +"/"
                + R.raw.aayfryxljh);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoURI);
        Bitmap bitmap = retriever
                .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        //
        Uri videoURI2 = Uri.parse("android.resource://" + getPackageName() +"/"
                + R.raw.aassnaulhq);
        MediaMetadataRetriever retriever2 = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoURI2);
        Bitmap bitmap2 = retriever
                .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable2 = new BitmapDrawable(getResources(), bitmap2);

//
        Uri videoURI3 = Uri.parse("android.resource://" + getPackageName() +"/"
                + R.raw.sun);
        MediaMetadataRetriever retriever3 = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoURI3);
        Bitmap bitmap3 = retriever
                .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable3 = new BitmapDrawable(getResources(), bitmap3);*/
        selectData(TABLE_NAME);
//리스트뷰에 어댑터를 붙여준다
        listView.setAdapter(adapter);
        //어댑터의 list에 데이터 추가
       // adapter = new VideoAdapter(RequestList.this, list, size);
       // adapter.addDTO(new VideoDTO("가수1", "2021-10-17",40.95656 , "REAL", drawable,"01011111111"));
        //adapter.addDTO(new VideoDTO("가수1", "2021-10-17",40.95656 , "REAL", drawable,"01011111111"));
        //adapter.addDTO(new VideoDTO("가수2", "2021-10-17",78.78954 , "FAKE", drawable2,"01053885418"));
        //adapter.addDTO(new VideoDTO("가수3", "2021-10-17",-1 , "CAN'T FIND FACES", drawable3,""));
        //adapter.addDTO(new VideoDTO("가수3", "010-3333-3333", 25, R.drawable.singer3));
        //adapter.addDTO(new VideoDTO("가수4", "010-4444-4444", 33, R.drawable.singer4));
        //adapter.addDTO(new VideoDTO("가수5", "010-5555-5555", 38, R.drawable.singer5));
/*
        //리스트뷰에 어댑터를 붙여준다
        listView.setAdapter(adapter);

        //리스트뷰의 아이템 클릭했을때 이벤트 추가
        //AdapterView<?> parent : 클릭이 발생한 어댑터뷰
        //View view : 어댑터뷰 내부의, 클릭이 된 바로 그 뷰
        //int position : 어댑터 내부의 그 뷰의 위치(position)
        //long id : 클릭된 아이템의 row id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoDTO dto = (VideoDTO) adapter.getItem(position);
                Toast.makeText(RequestList.this, "\n요청 일자 : " + dto.getPhoneNum() + "\n딥페이크일 확률 : " + dto.getProb() + "\n결과값 : " + dto.getAge(), Toast.LENGTH_SHORT).show();
            }
        });

        //버튼1(추가 버튼)에 기능 추가
        /*button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "가수6";
                String phoneNum = "010-6666-6666";
                double probability = 0.5465239;
                String result = "FAKE";
                int resId = R.drawable.finger;

                VideoDTO dto = new VideoDTO(name, phoneNum, probability, result, resId);
                adapter.addDTO(dto);
                //adapter.addDTO = (new SingerDTO(name, phoneNum, age, resId));
                //adapter.addDTO = (new SingerDTO("가수6", "010-6666-6666", 35, R.mipmap.ic_launcher);

                //리스트뷰 데이터 갱신
                adapter.notifyDataSetChanged();
            }
        });*/
        //리스트뷰의 아이템 클릭했을때 이벤트 추가
        //AdapterView<?> parent : 클릭이 발생한 어댑터뷰
        //View view : 어댑터뷰 내부의, 클릭이 된 바로 그 뷰
        //int position : 어댑터 내부의 그 뷰의 위치(position)
        //long id : 클릭된 아이템의 row id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoDTO dto = (VideoDTO) adapter.getItem(position);
                Toast.makeText(RequestList.this, "\n요청 일자 : " + dto.getPhoneNum() + "\n딥페이크일 확률 : " + dto.getProb() + "\n결과값 : " + dto.getAge(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 디바이스 가로 세로 사이즈 구하기
    // getRealSize()는 status bar 등 system insets을
    // 포함한 스크린 사이즈를 가져오는 방법이고,
    // getSize()는 status bar 등 insets를
    // 제외한 부분에 대한 사이즈만 가져오는 함수이다.
    // 단위는 픽셀
    public Point getDeviceSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        //현재 프로젝트에서는 쓰지 않지만 가로와 세로 길이를 이렇게 빼서 사용한다
        int width = size.x;
        int height = size.y;

        return size;
    }
    public void selectData(String tableName){
        if(database !=null){
            String sql="SELECT URL, DATE, DEEPFAKE, RESULT FROM " +tableName;
            Cursor cursor=database.rawQuery(sql, null);
            adapter = new VideoAdapter(RequestList.this, list, size);
            //adapter.addDTO(new VideoDTO("가수1", "2021-10-17",40.95656 , "REAL", drawable,"01011111111"));
            for(int i=0; i<cursor.getCount(); i++){
                cursor.moveToNext();
                String url=cursor.getString(0);
                String date=cursor.getString(1);
                String deepfake=cursor.getString(2);
                String result=cursor.getString(3);
               // println("["+i+"]"+url+date+deepfake+result);
               // Toast.makeText(this, "INSERT", Toast.LENGTH_LONG).show();

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(this, Uri.parse(URLEncoder.encode(url, "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = retriever
                        .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                adapter.addDTO(new VideoDTO("가수"+i, date,Double.parseDouble(deepfake) , result, drawable,"01011111111"));
            }
            cursor.close();



        }
    }
    /*
    private void println(String data){
        textView.append(data+"\n");
    }*/

}


