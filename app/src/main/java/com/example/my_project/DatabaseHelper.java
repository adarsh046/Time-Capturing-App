package com.example.my_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Time.db";
    public static final String TABLE_NAME="Addtime";
    public static final String COL_1="TIME_IN";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       //db.execSQL("create table "+ TABLE_NAME +"("+COL_1+"TEXT)");
        db.execSQL("create table Addtime (TIME_IN TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF exists "+TABLE_NAME);
        onCreate(db);

    }
    public boolean insertdata(String time){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,time);
        long result=db.insert(TABLE_NAME,null,contentValues);
        db.close();
        if(result==-1)
            return false;
        else
            return true;

    }
}
