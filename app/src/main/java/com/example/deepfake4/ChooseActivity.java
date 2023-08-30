package com.example.deepfake4;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Point;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton image, video, request, trending;
    ListView listView;
    ArrayList<VideoDTO> list;
    VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        video = (ImageButton) findViewById(R.id.video);
        request = (ImageButton) findViewById(R.id.request);
        trending = (ImageButton) findViewById(R.id.trending);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            video.setEnabled(false);
            request.setEnabled(false);
            trending.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {
            video.setEnabled(true);
            request.setEnabled(true);
            trending.setEnabled(true);
        }
        video.setOnClickListener(this);
        request.setOnClickListener(this);
        trending.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                video.setEnabled(true);
                request.setEnabled(true);
                trending.setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video:
                Intent intent2 = new Intent(this, VideoActivity.class);
                startActivity(intent2);
                break;
            case R.id.request:
                Intent intent3 = new Intent(this, RequestList.class);
                startActivity(intent3);
                break;
            case R.id.trending:
                Intent intent4 = new Intent(this, TrendingList.class);
                startActivity(intent4);
                break;

        }

    }
}