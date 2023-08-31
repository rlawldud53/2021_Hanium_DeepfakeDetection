package com.example.deepfake4;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import java.util.ArrayList;


import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class VideoAdapter extends BaseAdapter {
    Context context;
    ArrayList<VideoDTO> list;
    Point size;

    LayoutInflater inflater;
    AlertDialog dialog;

    public VideoAdapter(Context context, ArrayList<VideoDTO> list, Point size) {
        this.context = context;
        this.list = list;
        this.size = size;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //인플레이터는 시스템에서 가져온다.
    }
    //리스트(list)에 항목을 추가해 줄 메서드 작성
    public void addDTO(VideoDTO dto) {
        list.add(dto);
    }

    //리스트의 항목을 삭제할 메서드 작성
    public void delDTO(int position) {
        list.remove(position);
    }

    //리스트의 항목을 모두 삭제할 메서드 작성
    public void removeDTOs() {
        list.clear();
    }

    //getCount() : 리스트에서 항목을 몇개나 가져와서 몇개의 화면을 만들 것인지 정하는 메서드
    @Override
    public int getCount() {
        return list.size();
    }

    //getItem() : 리스트에서 해당하는 인덱스의 데이터(사진, 이름, 전번)를 모두 가져오는 메서드
    //Object를 알아서 캐스팅해서 사용하라는 의미로 반환 타입이 Object
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SingerViewHolder viewHolder;

        //화면 구성
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.request_list, parent, false);
            viewHolder = new SingerViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvPhoneNum = convertView.findViewById(R.id.tvPhoneNum);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.result = convertView.findViewById(R.id.result);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SingerViewHolder) convertView.getTag();
        }

        //DTO에서 데이터를 찾음
        VideoDTO dto = list.get(position);
        String name = dto.getName();
        String phoneNum = dto.getPhoneNum();
        double prob = dto.getProb();
        String result = dto.getAge();
        Drawable resId = dto.getResId();


        //XML의 화면에 찾은 데이터 표시
        viewHolder.tvName.setText("요청 일자 : " + phoneNum);
        //viewHolder.tvName.setText(dto.getName()); // 이렇게 써도 같음
        viewHolder.tvPhoneNum.setText("딥페이크일 확률 : " + Double.toString(prob));
        viewHolder.result.setText("판별 결과 : " + result );
        viewHolder.imageView.setImageDrawable(resId);

        //이미지만 클릭했을때 기능 추가
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result == "CAN'T FIND FACES"){
                    Toast.makeText(context, "\n요청 일자 : " + dto.getPhoneNum() + "\n딥페이크일 확률 : " + "없음" + "\n결과값 : " + "NO FACES" , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "\n요청 일자 : " + dto.getPhoneNum() + "\n딥페이크일 확률 : " + dto.getProb() + "\n결과값 : " + dto.getAge() , Toast.LENGTH_SHORT).show();
                }
                //방법 ① : 이미지뷰 추가하여 직접 붙임
                //popUpImg(list.get(position).getResId());

                //방법 ② : 미리 만들어둔 XML과 팝업창을 연결해서 보여줌
                popupImgXml(list.get(position).getResId(), list.get(position).getName());
            }
        });

        return convertView;
    }

    //따로 새 자바 파일을 만들지 않고 XML의 내용을 볼 수 있게끔 만든 클래스
    public class SingerViewHolder {
        public ImageView imageView;
        public TextView tvName, tvPhoneNum,result;
        public VideoView vv;
        public MediaController mediacontroller;
    }

    //방법 ① : 이미지 뷰를 만들어서 직접 이미지 넣기
    public void popUpImg(int resId) {
        ImageView image = new ImageView(context);
        image.setImageResource(resId);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("이미지 띄우기");
        builder.setView(image);

        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    //방법 ② : XML 불러오기
    public void popupImgXml(Drawable resId, String name) {
        //일단 res에 popupimg.xml 만든다
        //그 다음 화면을 inflate 시켜 setView 한다

        //팝업창에 xml 붙이기///////////////
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_request, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        //TextView textView = view.findViewById(R.id.textView);
        Button button = view.findViewById(R.id.button);
        RadioGroup radiogroup = view.findViewById(R.id.radioGroup);
        RadioGroup radioGroup2 = view.findViewById(R.id.radioGroup2);
        imageView.setImageDrawable(resId);
        //textView.setTextSize(35);
        //textView.setText(name + "\n");
        //textView.append(name + "\n" + name + "\n" + name + "\n" + name + "\n" + name + "\n" + name + "\n" + name + "\n" + name + "\n");
        /////////////////////////

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("리뷰 작성")
                .setView(view);

        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("filename",name);
                context.startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radiogroup.getCheckedRadioButtonId() == -1 || radioGroup2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(context, "결과값을 체크해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "성공적으로 전송되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog = builder.create();
        dialog.show();

        //디바이스 사이즈를 받아 팝업 크기창을 조절한다.
        int sizeX = size.x;
        int sizeY = size.y;

        //AlertDialog에서 위치 크기 수정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.x = (int) Math.round(sizeX * 0.005); // X위치
        params.y = (int) Math.round(sizeY * 0.01); // Y위치
        params.width = (int) Math.round(sizeX * 0.9);
        params.height = (int) Math.round(sizeY * 0.8);
        dialog.getWindow().setAttributes(params);
    }
}
