package com.example.nicolas.reto9_sqlite;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class ViewAllEmpresas extends ListActivity implements AdapterView.OnItemSelectedListener {

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

        Spinner spinner = (Spinner) findViewById(R.id.tipo_list);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.tipo_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter2);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        String f = "C";
        switch (pos){
            case 1:
                f = "C";
                break;
            case 2:
                f = "M";
                break;
            case 3:
                f = "F";
                break;

        }


        empresaOps.open();
        empresas = empresaOps.getFilteredEmpresas(f);
        empresaOps.close();
        ArrayAdapter<Empresa> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, empresas);
        setListAdapter(adapter);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
