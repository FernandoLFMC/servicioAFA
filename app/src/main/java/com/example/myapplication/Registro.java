 package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.utils.Data;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Registro extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private Button registrar;
    EditText nombre,email,clave,telefono;
    RadioButton R1,R2,comprador,vendedor;
    EditText street;
    //private MapView map;
    //private GoogleMap mMap;
    //Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registrar=(Button) findViewById(R.id.registrar);
        nombre=(EditText) findViewById(R.id.nombre);
        email=(EditText) findViewById(R.id.email);
        telefono=(EditText)findViewById(R.id.telefono);
        clave=(EditText) findViewById(R.id.clave);
        R1=(RadioButton) findViewById(R.id.radiofem);
        R2=(RadioButton) findViewById(R.id.radiomasc);
        comprador=(RadioButton) findViewById(R.id.comprador);
        vendedor=(RadioButton) findViewById(R.id.vendedor);
        registrar.setOnClickListener(this);

        /*map = findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.onResume();
        MapsInitializer.initialize(this);
        map.getMapAsync(this);
         geocoder= new Geocoder(getBaseContext(), Locale.getDefault());
        */
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.registrar){
            setDatos();
        }
    }

    private void setDatos() {

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("nombre",nombre.getText().toString());
        req.put("email",email.getText().toString());
        req.put("password",clave.getText().toString());
        req.put("telefono",telefono.getText().toString());
        if(R1.isChecked()==true){
            req.put("sexo",R1.getText());
        }
        if(R2.isChecked()==true){
            req.put("sexo",R2.getText());
        }

        if(R1.isChecked()==true){
            req.put("tipo",comprador.getText());
        }
        if(R2.isChecked()==true){
            req.put("tipo",vendedor.getText());
        }


        client.post(Data.HOST_USER,req,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String res=response.getString("message");
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                    limpiarForm();
                    Registro.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void limpiarForm() {
        nombre.setText("");
        email.setText("");
        clave.setText("");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {/*
        mMap = googleMap;

        // Add a marker in Sydney and move the camera-19.5722805!4d-65.7550063
        LatLng sydney = new LatLng(-19.5783329, -65.7563853);
        mMap.addMarker(new MarkerOptions().position(sydney).title("lugar").zIndex(17).draggable(true));
        mMap.setMinZoomPreference(16);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            String street_string = getStreet(marker.getPosition().latitude,marker.getPosition().longitude);
            street.setText(street_string);
            }
        });
    }
    public String getStreet (Double lat, Double lon){
            List<Address> address;
         String result="";
        try {
            address=geocoder.getFromLocation(lat,lon,1);
            result += address.get(0).getThoroughfare();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    */}
}
