package com.example.login;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.login.portdate.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.mime.Header;

public class registro extends AppCompatActivity implements View.OnClickListener {
    private Button registrar;
    EditText nombre,apellido,telf,email,password;
    RadioButton R1,R2,comprador,vendedor;
    EditText street;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registrar=(Button) findViewById(R.id.registros);



        nombre=(EditText) findViewById(R.id.nombre);
        apellido =(EditText) findViewById(R.id.apellido);
        email=(EditText) findViewById(R.id.email);
        telf=(EditText)findViewById(R.id.telf);
        password=(EditText) findViewById(R.id.password);
        R1=(RadioButton) findViewById(R.id.femenino);
        R2=(RadioButton) findViewById(R.id.masculino);
        comprador=(RadioButton) findViewById(R.id.comprador);
        vendedor=(RadioButton) findViewById(R.id.vendedor);
        registrar.setOnClickListener(this);


            }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.registros){
            setDatos();
        }
    }

    private void setDatos() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("nombre",nombre.getText().toString());
        req.put("apellido",nombre.getText().toString());
        req.put("email",email.getText().toString());
        req.put("password",password.getText().toString());
        req.put("telf",telf.getText().toString());
        if(R1.isChecked()==true){
            req.put("sexo",R1.getText());
        }
        if(R2.isChecked()==true){
            req.put("sexo",R2.getText());
        }
        req.put("direccion",street.getText().toString());

        if(R1.isChecked()==true){
            req.put("tipo",comprador.getText());
        }
        if(R2.isChecked()==true){
            req.put("tipo",vendedor.getText());
        }

        client.post(Data.HOST_USER,req,new JsonHttpResponseHarndler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String res=response.getString("message");
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                    limpiarForm();
                    registro.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void limpiarForm() {
        nombre.setText("");
        email.setText("");
        password.setText("");

    }




}
