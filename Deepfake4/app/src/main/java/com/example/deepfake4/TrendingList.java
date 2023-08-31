package com.example.deepfake4;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TrendingList extends AppCompatActivity {
    //Button button1;
    ListView listView;

    ArrayList<VideoDTO> list;
    ArrayList<VideoDTO> arraylist;

    VideoAdapter adapter;
    //SearchAdapter searchadapter;

    // 검색어 입력할 input창
    EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending);

        editSearch = (EditText) findViewById(R.id.editSearch);

        //디바이스 사이즈 구하기
        Point size = getDeviceSize();

        //객체 초기화
        //button1 = findViewById(R.id.button1);

        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        //썸네일 추출
        Uri videoURI = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.aayfryxljh);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoURI);
        Bitmap bitmap = retriever
                .getFrameAtTime(100000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        //
        Uri videoURI2 = Uri.parse("android.resource://" + getPackageName() +"/"
                + R.raw.aassnaulhq);
        MediaMetadataRetriever retriever2 = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoURI2);
        Bitmap bitmap2 = retriever
                .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable2 = new BitmapDrawable(getResources(), bitmap2);

        //  데이터 저장
        //list.add(new VideoDTO("가수1", "2021-10-07", 0.4078954, "REAL", drawable));
        //list.add(new VideoDTO("가수2", "2021-10-08", 0.7895656, "FAKE", drawable));


        //searchadapter = new SearchAdapter(list,this);
        // 서치 어댑터 연결
        //listView.setAdapter(searchadapter);


        //어댑터의 list에 데이터 추가
        adapter = new VideoAdapter(TrendingList.this, list, size);
        adapter.addDTO(new VideoDTO("가수1", "2021-10-17",40.95656 , "REAL", drawable,"01011111111"));
        adapter.addDTO(new VideoDTO("가수2", "2021-10-17",78.78954 , "FAKE", drawable2,"01053885418"));
        //adapter.addDTO(new VideoDTO("가수3", "010-3333-3333", 25, R.drawable.singer3));
        //adapter.addDTO(new VideoDTO("가수4", "010-4444-4444", 33, R.drawable.singer4));
        //adapter.addDTO(new VideoDTO("가수5", "010-5555-5555", 38, R.drawable.singer5));

        //리스트뷰에 어댑터를 붙여준다
        listView.setAdapter(adapter);

        //리스트 복사본
        arraylist = new ArrayList<VideoDTO>();
        arraylist.addAll(list);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        //리스트뷰의 아이템 클릭했을때 이벤트 추가
        //AdapterView<?> parent : 클릭이 발생한 어댑터뷰
        //View view : 어댑터뷰 내부의, 클릭이 된 바로 그 뷰
        //int position : 어댑터 내부의 그 뷰의 위치(position)
        //long id : 클릭된 아이템의 row id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoDTO dto = (VideoDTO) adapter.getItem(position);
                Toast.makeText(TrendingList.this, "\n요청 일자 : " + dto.getPhoneNum() + "\n딥페이크일 확률 : " + dto.getProb() + "\n결과값 : " + dto.getAge(), Toast.LENGTH_SHORT).show();
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
    }

    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < arraylist.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).getPhone().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
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
}


