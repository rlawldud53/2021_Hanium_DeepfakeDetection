package com.example.deepfake4;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.VideoView;
import android.os.Bundle;
import android.widget.MediaController;

public class VideoPlayer extends AppCompatActivity {
    VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra("filename");
        Log.i("My",name);
        setContentView(R.layout.video_player);

        vv = findViewById(R.id.vv);
        Uri videoUri;
        // 내 res 폴더 안에 동영상
        if(name.equals("가수1"))
            videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fake_video_1);
        else if(name.equals("가수2"))
            videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.aassnaulhq);
        else
            videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fake_video_2);
        vv.setMediaController(new MediaController(this));
        vv.setVideoURI(videoUri);

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //비디오 시작
                vv.start();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        //비디오 일시 정지
        if(vv!=null && vv.isPlaying()) vv.pause();
    }
    //액티비티가 메모리에서 사라질때..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        if(vv!=null) vv.stopPlayback();
    }
}