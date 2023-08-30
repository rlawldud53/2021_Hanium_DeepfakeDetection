package com.example.deepfake4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import retrofit2.Response;

public class Sign extends AppCompatActivity {
    Button sign;
    ImageButton back;
    EditText name,email,pw;
    private AlertDialog dialog;
    int check=0;

    private LoginDatabaseHelper loginDatabaseHelper;
    public static final String TABLE_NAME="login";
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        back=(ImageButton)findViewById(R.id.back);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        pw=findViewById(R.id.pw);

        loginDatabaseHelper = LoginDatabaseHelper.getInstance(this);
        database = loginDatabaseHelper.getWritableDatabase();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDatabaseHelper.close();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        sign=(Button)findViewById(R.id.sign);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("") || email.getText().toString().equals("") || pw.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Sign.this);
                    dialog = builder.setMessage("빈 칸일 수 없습니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;

                }
                else{
                    selectData(TABLE_NAME,email.getText().toString());
                    if(check==2){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Sign.this);
                        dialog = builder.setMessage("이미 존재하는 ID 입니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                    else if(check==0){
                        insertData(name.getText().toString(),email.getText().toString(),pw.getText().toString());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Sign.this);
                        dialog = builder.setMessage("회원가입 되었습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }


                }
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
                    check=2;
                    //  println();
                }
            }
            cursor.close();
        }
    }
    private void insertData(String name, String id,String pw){
        if(database!=null){
            String sql="INSERT INTO login(NAME,ID,PW) VALUES(?, ?, ?)";
            Object[] params={name,id,pw};
            database.execSQL(sql,params);

        }

    }
}