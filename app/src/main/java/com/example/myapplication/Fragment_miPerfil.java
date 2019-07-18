package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.utils.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class Fragment_miPerfil extends Fragment {
    View view;
    TextView textViewNombre,textViewEmail,textViewTelefono,textViewSexo,textViewTipo, textViewMGusta, textViewNGusta;
    EditText editTextNombre, editTextTelefono;
    ImageView avatar;
    Button btnGuardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_perfil, container, false);
        ///anything
        loadComponents();

        getData();

        return view;
    }

    private void loadComponents() {
        textViewNombre = view.findViewById(R.id.textViewDescripcion);
        textViewEmail = view.findViewById(R.id.textViewPrecio);
        textViewTelefono = view.findViewById(R.id.textViewStock);
        textViewSexo = view.findViewById(R.id.textViewEstado);
        textViewTipo = view.findViewById(R.id.textViewTipo);
        textViewNGusta = view.findViewById(R.id.textViewNGusta);
        textViewMGusta = view.findViewById(R.id.textViewMGusta);
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextTelefono = view.findViewById(R.id.editTextTelefono);

        avatar = view.findViewById(R.id.foto);

        btnGuardar = view.findViewById(R.id.btnChat);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });
    }

    private void actualizarDatos() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if (editTextTelefono.getText().toString().isEmpty()|| editTextNombre.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Los campos no pueden estar vacios",Toast.LENGTH_LONG).show();
            return;
        }
        params.put("nombre",editTextNombre.getText().toString());
        params.put("telefono",editTextTelefono.getText().toString());

        client.patch(Data.HOST_USER + Data.ID_USER, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("message") != null){
                        Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        //volver al fragmento de incio
                        getFragmentManager().popBackStack();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        //Toast.makeText(getContext(), "ok getdata", Toast.LENGTH_SHORT).show();
        client.get(Data.HOST_USER+Data.ID_USER,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(getContext(), errorResponse.getString("error"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    //
                    e.printStackTrace();
                }
            }
        });


    }

    private void setData(JSONObject response) {
        try {
            textViewEmail.setText(textViewEmail.getText() + response.getString("email"));
            textViewSexo.setText(textViewSexo.getText() + response.getString("sexo"));
            textViewTipo.setText(textViewTipo.getText() + response.getString("tipo"));

            textViewMGusta.setText(textViewMGusta.getText() + response.getString("mgusta"));
            textViewNGusta.setText(textViewNGusta.getText() + response.getString("nmgusta"));
            editTextNombre.setText(response.getString("nombre"));
            editTextTelefono.setText(response.getString("telefono"));


            if (response.getString("avatar") != null ){
                Glide.with(getContext()).load(Data.IP+response.getString("avatar")).into(avatar);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}

