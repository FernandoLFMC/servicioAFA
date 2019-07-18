package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myapplication.utils.Data;
import com.example.myapplication.utils.Methods;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;


public class Fragment_registroProducto extends Fragment implements View.OnClickListener{
    View view;
    EditText editDescripcion, editPrecio, editStock;
    RadioButton disponible,agotado;
    ImageView imageFoto;
    Button btnFoto, registrar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registro_producto, container, false);
        loadComponents();
        return view;
    }

    private void loadComponents() {
        editDescripcion = view.findViewById(R.id.editDescripcion);
        editPrecio = view.findViewById(R.id.editPrecio);
        editStock = view.findViewById(R.id.editStock);

        disponible = view.findViewById(R.id.disponible);
        agotado = view.findViewById(R.id.agotado);

        imageFoto = view.findViewById(R.id.imageFoto);
        btnFoto = view.findViewById(R.id.btnFoto);
        registrar = view.findViewById(R.id.registrar);

        btnFoto.setOnClickListener(this);
        registrar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFoto){
            Methods.validarPermisos(getContext(),getActivity());
            cargarImagen();
        }else{
            registrarProducto();
        }
    }

    private void registrarProducto() {
        //revisar campos
        if (editDescripcion.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "LOs campos no deben estar vacios", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        File file = new File(path);
        try {
            params.put("foto", file,"image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        params.put("descripcion",editDescripcion.getText().toString());
        params.put("precio",editPrecio.getText().toString());
        params.put("stock",editStock.getText().toString());
        params.put("vendedor",Data.ID_USER);

        if(disponible.isChecked()){
            params.put("estado",disponible.getText().toString().toLowerCase());
        }
        if(agotado.isChecked()){
            params.put("estado",agotado.getText().toString().toLowerCase());
        }

        client.post(Data.HOST_PRODUCT,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("message") != null){
                        Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    //Agregar cargar el fragment de productos
                    Fragment_misAnuncios nextFrag= new Fragment_misAnuncios();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.esenario, nextFrag, "Fragment mis anuncios")
                            .addToBackStack(null)
                            .commit();
                } catch (JSONException e) {
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

    //DESDE AQUI VA LA PARTE DE LA FOTO
    final int COD_GALERIA=10;
    final int COD_CAMERA=20;
    String path;

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFoto();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_GALERIA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }
    private void tomarFoto() {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Methods.FileAndPath fileAndPath= Methods.createFile(path);
        File file = fileAndPath.getFile();
        path = fileAndPath.getPath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileuri = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", file);

            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
            //BuildConfig.APPLICATION_ID + ".provider"
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(camera, COD_CAMERA);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_GALERIA:
                    Uri imgPath=data.getData();
                    imageFoto.setImageURI(imgPath);
                    path = Methods.getRealPathFromURI(Objects.requireNonNull(getContext()),imgPath);
                    Toast.makeText(getContext(), path, Toast.LENGTH_SHORT).show();
                    break;
                case COD_CAMERA:
                    loadImageCamera();


                    break;
            }
        }
    }

    private void loadImageCamera() {
        Bitmap img = BitmapFactory.decodeFile(path);
        if(img != null) {
            imageFoto.setImageBitmap(img);

        }
    }

}
