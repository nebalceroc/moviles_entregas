package com.example.nicolas.reto9_sqlite;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

public class ViewAllEmpresas extends ListActivity{

    private EmpresaOperations empresaOps;
    List<Empresa> empresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_employees);
        empresaOps = new EmpresaOperations(this);
        empresaOps.open();
        empresas = empresaOps.getAllEmpresas();
        empresaOps.close();
        ArrayAdapter<Empresa> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, empresas);
        setListAdapter(adapter);
    }
}
