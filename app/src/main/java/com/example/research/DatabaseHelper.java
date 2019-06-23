package com.example.research;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "User";
    private static final String COL1 ="ID";
    private static final String COL2="email";
    private static final String COL3="password";
    private static final String COL4="firstq";
    private static final String COL5="secondq";

    public DatabaseHelper( Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable=
                "CREATE TABLE "+TABLE_NAME+
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COL2+" TEXT,"+COL3+" TEXT,"+COL4+" TEXT,"+
                        COL5+" TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }

    public boolean addUsers(String... details){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,details[0]);
        contentValues.put(COL3,details[1]);
        contentValues.put(COL4,details[2]);
        contentValues.put(COL5,details[3]);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result ==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }
}
