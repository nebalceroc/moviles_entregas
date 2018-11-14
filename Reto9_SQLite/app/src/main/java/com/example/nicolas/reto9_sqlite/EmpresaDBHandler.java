package com.example.nicolas.reto9_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmpresaDBHandler extends SQLiteOpenHelper{


    private static final String DATABASE_NAME = "empresa.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_EMPRESA = "empresa";
    public static final String COLUMN_ID = "empId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PYS = "pys";
    public static final String COLUMN_C = "c";
    public static final String COLUMN_M = "m";
    public static final String COLUMN_F = "f";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EMPRESA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_URL + " TEXT, " +
                    COLUMN_NUMBER + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PYS + " TEXT, " +
                    COLUMN_C + " INTEGER, " +
                    COLUMN_M + " INTEGER, " +
                    COLUMN_F + " INTEGER " +
                    ")";


    public EmpresaDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EMPRESA);
        db.execSQL(TABLE_CREATE);
    }
}