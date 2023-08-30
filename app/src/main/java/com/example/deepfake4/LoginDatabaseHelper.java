package com.example.deepfake4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import static com.example.deepfake4.Sign.TABLE_NAME;

public class LoginDatabaseHelper extends SQLiteOpenHelper {
    private static LoginDatabaseHelper instance;
    public static synchronized LoginDatabaseHelper getInstance(Context context){
        if(instance==null){
            instance = new LoginDatabaseHelper(context, "Login",null,1);
        }
        return instance;

    }
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="Login.db";
    public static final String TABLAE_NAME="login";
    public static final String COLUMN_NAME="NAME";
    public static final String COLUMN_ID="ID";
    public static final String COLUMN_PW="PW";

    public static final String SQL_CREATE_LOGIN=
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+COLUMN_NAME+" TEXT, "+COLUMN_ID+" TEXT, "+COLUMN_PW+" TEXT"+");";

    private LoginDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_LOGIN);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion>1){
            db.execSQL("DROP TABLE IF EXISTS "+TABLAE_NAME);
            db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLAE_NAME);
        }
    }
}
