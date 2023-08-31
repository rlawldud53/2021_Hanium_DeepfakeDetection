package com.example.deepfake4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.deepfake4.VideoActivity.TABLE_NAME;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static UserDatabaseHelper instance;
    public static synchronized UserDatabaseHelper getInstance(Context context){
        if(instance==null){
            instance = new UserDatabaseHelper(context, "User",null,1);
        }
        return instance;

    }
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="User.db";
    public static final String TABLAE_NAME="user";
    public static final String COLUMN_URL="URL";
    public static final String COLUMN_DATE="DATE";
    public static final String COLUMN_DEEPFAKE="DEEPFAKE";
    public static final String COLUMN_RESULT="RESULT";
    public static final String COLUMN_PHONE="PHONE";

    public static final String SQL_CREATE_USER=
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+COLUMN_URL+" TEXT, "+COLUMN_DATE+" TEXT, "+COLUMN_DEEPFAKE+" TEXT, "+COLUMN_RESULT+" TEXT, "+COLUMN_PHONE+" TEXT"+");";

    private UserDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_USER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion>1){
            db.execSQL("DROP TABLE IF EXISTS "+TABLAE_NAME);
            db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLAE_NAME);
        }
    }
}
