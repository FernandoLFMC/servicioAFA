package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.utils.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;



public class DetalleProductoCompradorFragment extends Fragment implements  View.OnClickListener{
    View view;
    TextView textViewDescripcion,textViewEstado,textViewPrecio, textViewStock;
    ImageView foto;
    Button btnChat,btnCita;
    String idProducto,vendedor;
    int stock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detalle_producto_comprador, container, false);
        loadComponents();
        Bundle bundle = getArguments();

        //assert bundle != null;
        idProducto = bundle.getString("idProducto","id");

        getData();


        return view;
    }


    private void loadComponents() {
        textViewDescripcion = view.findViewById(R.id.textViewDescripcion);
        textViewPrecio = view.findViewById(R.id.textViewPrecio);
        textViewStock = view.findViewById(R.id.textViewStock);
        textViewEstado = view.findViewById(R.id.textViewEstado);

        foto = view.findViewById(R.id.foto);

        btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);
        btnCita = view.findViewById(R.id.btnCita);
        btnCita.setOnClickListener(this);
    }
    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        //Toast.makeText(getContext(), "ok getdata", Toast.LENGTH_SHORT).show();
        client.get(Data.HOST_PRODUCT + idProducto,new JsonHttpResponseHandler(){
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
            textViewDescripcion.setText(textViewDescripcion.getText() + response.getString("descripcion"));
            textViewPrecio.setText(textViewPrecio.getText() + response.getString("precio"));
            textViewStock.setText(textViewStock.getText() + response.getString("stock"));

            textViewEstado.setText(textViewEstado.getText() + response.getString("estado"));
            stock = response.getInt("stock");
            vendedor = response.getString("vendedor");


            if (response.getString("foto") != null ){
                Glide.with(getContext()).load(Data.IP+response.getString("foto")).into(foto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCita){
            CitaFragment fragment = new CitaFragment();
            Bundle bundle = new Bundle();
            bundle.putString("vendedor",vendedor);
            bundle.putString("idProducto",idProducto);
            bundle.putInt("stock",stock);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.esenarioComprador,fragment).addToBackStack(null).commit();
        }else{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.esenarioComprador,new CitaFragment()).addToBackStack(null).commit();
        }
    }
}
