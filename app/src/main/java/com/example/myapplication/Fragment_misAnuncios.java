package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.ProductoAdapter;
import com.example.myapplication.items.ItemProducto;
import com.example.myapplication.utils.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Fragment_misAnuncios extends Fragment {
    View view;
    ArrayList<ItemProducto> listData;
    RecyclerView recyclerProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_anuncios, container, false);
        ///anything
        loadComponents();

        getData();

        return view;
    }

    private void loadComponents() {
        recyclerProductos = view.findViewById(R.id.recyclerProductos);
        listData = new ArrayList<>();
        recyclerProductos.setLayoutManager(
                new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }
    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        Toast.makeText(getContext(), "ok getdata", Toast.LENGTH_SHORT).show();
        client.get(Data.HOST_PRODUCT_VENDEDOR + Data.ID_USER,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++){
                        ItemProducto item = new ItemProducto();
                        JSONObject obj = array.getJSONObject(i);
                        item.setId(obj.getString("_id"));

                        item.setDescripcion(obj.getString("descripcion"));
                        item.setPrecio(obj.getDouble("precio"));
                        item.setStock(obj.getInt("stock"));
                        item.setFoto(obj.getString("foto"));
                        listData.add(item);
                    }

                    /*
                    *  if (response.getString("message") != null){
                        editNombre.setText(response.getString("nombre"));
                        textEmail.setText(response.getString("email"));
                        textTipo.setText(response.getString("tipo"));

                        //// guardar datos

                        btnGUardar = find...
                        btnGuardar.setOn....

                        onClick View{
                            Asyn client

                            client.patch(Dta_USER + DAta .USEER_Id, handler {
                                if message
                                toast dse cambio los datos
                            })
                        }
                    }
                    * */
                    loadData();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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

    private void loadData() {;
        Log.d(TAG, "loadData: ok");

        ProductoAdapter adapter = new ProductoAdapter(getContext(), listData);
        recyclerProductos.setAdapter(adapter);

    }


}
