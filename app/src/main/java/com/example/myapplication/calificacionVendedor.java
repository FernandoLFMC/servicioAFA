package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class calificacionVendedor extends AppCompatActivity {

    private EditText et2;
    private EditText et1;
    private EditText et3;
    private TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion_vendedor);

        et1 = (EditText)findViewById(R.id.txt_responsable);
        et2 = (EditText)findViewById(R.id.txt_estadoproducto);
        et3 = (EditText)findViewById(R.id.txt_precio);
        tv1 = (TextView)findViewById(R.id.tv_estatus);
    }

    public void estatus(View view){
        String responsable_String = et1.getText().toString();
        String estadoproducto_String = et2.getText().toString();
        String precio_String =  et3.getText().toString();

        int responsable_int = Integer.parseInt(responsable_String);
        int estadoproducto_int = Integer.parseInt(estadoproducto_String);
        int precio_int = Integer.parseInt(precio_String);

        int promedio = (responsable_int + estadoproducto_int + precio_int) / 3;

        if(promedio >=6){
            tv1.setText("Estatus bueno con " + promedio);
        } else if(promedio <=5){
            tv1.setText("Estatus malo con " + promedio);
        }
    }
}




