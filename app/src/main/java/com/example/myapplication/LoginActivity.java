package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.utils.Data;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient client;
    TextView registra;
    EditText em,pass;
    Button accion;
    public static final int SIGN_IN_CODE = 777;
    private int GOOGLE_CODE=11235;
    private SignInButton signInButton;
    boolean newUser = true;


    //loginpreference
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        client =new GoogleApiClient.Builder(this)
          .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();

        signInButton=(SignInButton)findViewById(R.id.googlebuton);
        signInButton.setSize(SignInButton.COLOR_DARK);
        signInButton.setOnClickListener(this);

        //em=findViewById(R.id.emaillogin);
        //pass=findViewById(R.id.passwordlogin);
        loadComponents();
    }

    private void loadComponents() {

        em=findViewById(R.id.emaillogin);
        pass=findViewById(R.id.passwordlogin);
        accion=findViewById(R.id.accion);
        accion.setOnClickListener(this);

        registra=findViewById(R.id.registra);
        registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg=new Intent(LoginActivity.this,Registro.class);
                LoginActivity.this.startActivity(intentReg);
            }

        });


        SharedPreferences preferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String emailPref = preferences.getString("email","");
        String passwordPref = preferences.getString("password","");

        if (!emailPref.equals("") && !passwordPref.equals("") ){
            login(emailPref,passwordPref);
            Toast.makeText(this, "Shared data Ok", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Shared data not found", Toast.LENGTH_SHORT).show();


        }

        SignInButton googlebtn =(SignInButton)this.findViewById(R.id.googlebuton);
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent =  Auth.GoogleSignInApi.getSignInIntent(client);
             startActivityForResult(intent,GOOGLE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            goMainScreen(result);
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen(GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        String correo=account.getEmail();
        Toast.makeText(this, ""+correo, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams p=new RequestParams();
        p.put("email",correo);
        client.post(Data.HOST_USER+"/logingoogle",p,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String res=response.getString("message");
                    Data.HOST_USER=res;
                    Intent in=new Intent(getApplicationContext(),menuLateral.class);
                    startActivity(in);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.accion){
            //Intent in = new Intent(getApplicationContext(),MainActivity.class);
            login("", "");
        }else if(v.getId()==R.id.googlebuton){
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
            startActivityForResult(intent, SIGN_IN_CODE);
        }

    }

    private void login(final String emailPref, final String passwordPref) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        if (!emailPref.equals("")){

            params.put("email",emailPref);
            params.put("password",passwordPref);
        }else{
            params.put("email",em.getText().toString());
            params.put("password",pass.getText().toString());
        }



            client.post(Data.HOST_USER_LOGIN, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getString("token") != null){
                            Data.TOKEN = response.getString("token");
                            Data.ID_USER = response.getString("id");
                            if (emailPref.equals("")){
                                setShared(em.getText().toString(),pass.getText().toString());
                            }

                            Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent intent;

                            if (response.getString("tipo").equals("vendedor")){
                                intent = new Intent(LoginActivity.this, menuLateral.class);

                            }else{
                                intent = new Intent(LoginActivity.this, InicioCompradorActivity.class);
                            }

                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, response.getString("msn"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    try {
                        Toast.makeText(LoginActivity.this, errorResponse.getString("error"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

    private void setShared(String emailPref, String passwordPref) {

        SharedPreferences preferences = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        preferences.edit().putString("email",emailPref).apply();
        preferences.edit().putString("password",passwordPref).apply();
    }
}

