package com.example.nicolas.reto9_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class EmpresaOperations {
    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            EmpresaDBHandler.COLUMN_ID,
            EmpresaDBHandler.COLUMN_NAME,
            EmpresaDBHandler.COLUMN_URL,
            EmpresaDBHandler.COLUMN_NUMBER,
            EmpresaDBHandler.COLUMN_EMAIL,
            EmpresaDBHandler.COLUMN_PYS,
            EmpresaDBHandler.COLUMN_C,
            EmpresaDBHandler.COLUMN_M,
            EmpresaDBHandler.COLUMN_F

    };

    public EmpresaOperations(Context context){
        dbhandler = new EmpresaDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();


    }
    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }
    public Empresa addEmpresa(Empresa Empresa){
        ContentValues values  = new ContentValues();
        values.put(EmpresaDBHandler.COLUMN_NAME,Empresa.getName());
        values.put(EmpresaDBHandler.COLUMN_URL,Empresa.getUrl());
        values.put(EmpresaDBHandler.COLUMN_NUMBER, Empresa.getNumber());
        values.put(EmpresaDBHandler.COLUMN_EMAIL, Empresa.getEmail());
        values.put(EmpresaDBHandler.COLUMN_PYS, Empresa.getPys());
        values.put(EmpresaDBHandler.COLUMN_C, Empresa.getC());
        values.put(EmpresaDBHandler.COLUMN_M, Empresa.getM());
        values.put(EmpresaDBHandler.COLUMN_F, Empresa.getF());
        long insertid = database.insert(EmpresaDBHandler.TABLE_EMPRESA,null,values);
        Empresa.setEmpId(insertid);
        return Empresa;

    }

    // Getting single Employee
    public Empresa getEmpresa(long id) {
        open();
        Cursor cursor = database.query(EmpresaDBHandler.TABLE_EMPRESA,allColumns,EmpresaDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Empresa e = new Empresa(Long.parseLong(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),"1".equals(String.valueOf(cursor.getInt(6))),"1".equals(String.valueOf(cursor.getInt(7))),"1".equals(String.valueOf(cursor.getInt(8))));
        // return Employee
        return e;
    }

    public List<Empresa> getFilteredEmpresas(String filtro) {
        Cursor cursor = database.query(EmpresaDBHandler.TABLE_EMPRESA,allColumns,null,null,null, null, null);
        List<Empresa> empresas = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Empresa empresa = new Empresa();
                empresa.setEmpId(cursor.getLong(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_ID)));
                empresa.setName(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_NAME)));
                empresa.setUrl(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_URL)));
                empresa.setNumber(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_NUMBER)));
                empresa.setEmail(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_EMAIL)));
                empresa.setPys(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_PYS)));
                Log.e("ASD1", String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_C))));
                Log.e("ASD2", String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_M))));
                Log.e("ASD3", String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_F))));
                empresa.setC("1".equals(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_C)))));
                empresa.setM("1".equals(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_M)))));
                empresa.setF("1".equals(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_F)))));
                boolean f;
                switch (filtro){
                    case "C":
                        if(empresa.getC()){
                            empresas.add(empresa);
                        }
                        break;
                    case "M":
                        if(empresa.getM()){
                            empresas.add(empresa);
                        }
                        break;
                    case "F":
                        if(empresa.getF()){
                            empresas.add(empresa);
                        }
                        break;
                }

            }
        }
        // return All Employees
        return empresas;
    }

    public List<Empresa> getAllEmpresas() {
        Cursor cursor = database.query(EmpresaDBHandler.TABLE_EMPRESA,allColumns,null,null,null, null, null);
        List<Empresa> empresas = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Empresa empresa = new Empresa();
                empresa.setEmpId(cursor.getLong(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_ID)));
                empresa.setName(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_NAME)));
                empresa.setUrl(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_URL)));
                empresa.setNumber(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_NUMBER)));
                empresa.setEmail(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_EMAIL)));
                empresa.setPys(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_PYS)));
                Log.e("ASD1", String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_C))));
                Log.e("ASD2", String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_M))));
                Log.e("ASD3", String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_F))));
                empresa.setC("1".equals(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_C)))));
                empresa.setM("1".equals(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_M)))));
                empresa.setF("1".equals(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_F)))));
                empresas.add(empresa);
            }
        }
        // return All Employees
        return empresas;
    }

    // Updating Employee
    public int updateEmpresa(Empresa empresa) {

        ContentValues values = new ContentValues();
        values.put(EmpresaDBHandler.COLUMN_NAME, empresa.getNumber());
        values.put(EmpresaDBHandler.COLUMN_URL, empresa.getUrl());
        values.put(EmpresaDBHandler.COLUMN_NUMBER, empresa.getNumber());
        values.put(EmpresaDBHandler.COLUMN_EMAIL, empresa.getEmail());
        values.put(EmpresaDBHandler.COLUMN_PYS, empresa.getPys());
        values.put(EmpresaDBHandler.COLUMN_C, Integer.valueOf(empresa.getC() ? 1 : 0));
        values.put(EmpresaDBHandler.COLUMN_M, Integer.valueOf(empresa.getM() ? 1 : 0));
        values.put(EmpresaDBHandler.COLUMN_F, Integer.valueOf(empresa.getF() ? 1 : 0));

        // updating row
        return database.update(EmpresaDBHandler.TABLE_EMPRESA, values,
                EmpresaDBHandler.COLUMN_ID + "=?",new String[] { String.valueOf(empresa.getEmpId())});
    }

    // Deleting Employee
    public void removeEmpresa(Empresa empresa) {

        database.delete(EmpresaDBHandler.TABLE_EMPRESA, EmpresaDBHandler.COLUMN_ID + "=" + empresa.getEmpId(), null);
    }



}
