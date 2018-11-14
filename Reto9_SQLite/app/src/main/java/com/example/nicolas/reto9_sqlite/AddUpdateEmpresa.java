package com.example.nicolas.reto9_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddUpdateEmpresa extends AppCompatActivity{

    private static final String EXTRA_EMP_ID = "com.example.nicolas.empId";
    private static final String EXTRA_ADD_UPDATE = "com.example.nicolas.add_update";
    private CheckBox cBox,fBox,mBox;
    private EditText nameEditText;
    private EditText urlEditText;
    private EditText emailEditText;
    private EditText numberEditText;
    private EditText pysEditText;
    private Button addUpdateButton;
    private Empresa newEmpresa;
    private Empresa oldEmpresa;
    private String mode;
    private long empId;
    private EmpresaOperations empresaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_employee);
        newEmpresa = new Empresa();
        oldEmpresa = new Empresa();
        nameEditText = (EditText)findViewById(R.id.edit_text_first_name);
        urlEditText = (EditText)findViewById(R.id.edit_text_last_name);
        emailEditText = (EditText) findViewById(R.id.edit_text_hire_date);
        numberEditText = (EditText) findViewById(R.id.edit_text_number);
        pysEditText = (EditText)findViewById(R.id.edit_text_dept);
        cBox = (CheckBox)findViewById(R.id.checkbox_c);
        mBox = (CheckBox)findViewById(R.id.checkbox_m);
        fBox = (CheckBox)findViewById(R.id.checkbox_f);

        addUpdateButton = (Button)findViewById(R.id.button_add_update_employee);
        empresaData = new EmpresaOperations(this);
        empresaData.open();


        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if(mode.equals("Update")){

            addUpdateButton.setText("Update Empresa");
            empId = getIntent().getLongExtra(EXTRA_EMP_ID,0);

            initializeEmpresa(empId);

        }

        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("Add")) {
                    newEmpresa.setName(nameEditText.getText().toString());
                    newEmpresa.setUrl(urlEditText.getText().toString());
                    newEmpresa.setEmail(emailEditText.getText().toString());
                    newEmpresa.setNumber(numberEditText.getText().toString());
                    newEmpresa.setPys(pysEditText.getText().toString());
                    newEmpresa.setC(cBox.isChecked());
                    newEmpresa.setM(mBox.isChecked());
                    newEmpresa.setF(fBox.isChecked());
                    empresaData.addEmpresa(newEmpresa);
                    Log.e("ASD",newEmpresa.toString());
                    Toast t = Toast.makeText(AddUpdateEmpresa.this, "Empresa "+ newEmpresa.getName() + "has been added successfully !", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateEmpresa.this,MainActivity.class);
                    startActivity(i);
                }else {
                    oldEmpresa.setName(nameEditText.getText().toString());
                    oldEmpresa.setUrl(urlEditText.getText().toString());
                    oldEmpresa.setEmail(emailEditText.getText().toString());
                    oldEmpresa.setNumber(numberEditText.getText().toString());
                    oldEmpresa.setPys(pysEditText.getText().toString());
                    oldEmpresa.setC(cBox.isChecked());
                    oldEmpresa.setM(mBox.isChecked());
                    oldEmpresa.setF(fBox.isChecked());
                    empresaData.updateEmpresa(oldEmpresa);
                    Toast t = Toast.makeText(AddUpdateEmpresa.this, "Employee "+ oldEmpresa.getName() + " has been updated successfully !", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateEmpresa.this,MainActivity.class);
                    startActivity(i);

                }


            }
        });


    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_c:
                if (checked){

                }else{

                }
                break;
            case R.id.checkbox_m:
                if (checked){

                }else{

                }
                break;
            case R.id.checkbox_f:
                if (checked){

                }else{

                }
                break;
        }
    }

    private void initializeEmpresa(long empId) {
        oldEmpresa = empresaData.getEmpresa(empId);
        nameEditText.setText(oldEmpresa.getName());
        urlEditText.setText(oldEmpresa.getUrl());
        emailEditText.setText(oldEmpresa.getEmail());
        numberEditText.setText(oldEmpresa.getNumber());
        pysEditText.setText(oldEmpresa.getPys());
        cBox.setChecked(oldEmpresa.getC());
        mBox.setChecked(oldEmpresa.getM());
        fBox.setChecked(oldEmpresa.getF());
    }
}
