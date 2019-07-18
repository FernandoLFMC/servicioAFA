package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Camara extends AppCompatActivity {
    private  final int  CODE=100;
    private  final int  CODE_Permiso=101;
    private ImageView IMG;
    private ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        btn =findViewById(R.id.camera);

        IMG=findViewById(R.id.image);
         btn.setVisibility(View.INVISIBLE);
         if(reviewPermissions()){
             btn.setVisibility(View.VISIBLE);
         }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Camara.this.startActivityForResult(camara,CODE);
            }
        });
    }

    private boolean reviewPermissions() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if(this.checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){

       return true;
        }
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},CODE_Permiso);

        return  false;
    }
    private Bitmap saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper Cw=new ContextWrapper(getApplicationContext());
        File directory =Cw.getDir("imageDir",Context.MODE_PRIVATE);
        File mypath=new File(directory,"profile.jpg");
        FileOutputStream fos = null;
        try {
            fos =new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG,100,fos);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

                try {
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String path = directory.getAbsolutePath() + "/profile.jpg";
          return  BitmapFactory.decodeFile(path);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(CODE_Permiso==requestCode){
            if(permissions.length==3){
                btn.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CODE){
        Bitmap img =(Bitmap)data.getExtras().get("data");
        Bitmap ss= saveToInternalStorage(img);
        IMG.setImageBitmap(ss);

        }
    }
}
///para llamar a la actividad de la camara
  //Intent camera =new Intent("de donde se quiere llamar a esta accion".this,Camara.class );
   //"de dinde se esta llamando".this.startActiviti(Camara);

///mgksdfglksndfglkdlkfgnlkdfglldkfnglk