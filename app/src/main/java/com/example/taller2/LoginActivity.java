package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private EditText correoText;
    private EditText passText;
    private Button registratBtn;
    private Button iniciarBtn;
    private FirebaseAuth firebaseAuth;
    public static final int SIGN_IN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Correo electronico
        firebaseAuth = FirebaseAuth.getInstance();

        correoText = (EditText) findViewById(R.id.correoTextEdit);
        passText = (EditText) findViewById(R.id.passTextEdt);
        registratBtn = (Button) findViewById(R.id.registrarButton);
        iniciarBtn = (Button) findViewById(R.id.registrarButton);

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        signInButton = (SignInButton) findViewById((R.id.signButton));
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
    }

    //Correo
    public void registrarUsuario(){
        String email = correoText.getText().toString().trim();
        String pass = passText.getText().toString().trim();

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Ingrese credenciales",Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Registro con éxito",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Registro errone",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void inicarSesion() {
        final String email = correoText.getText().toString().trim();
        String pass = passText.getText().toString().trim();

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Ingrese credenciales",Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Bienvenido",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplication(),Login2Activity.class);
                    intent.putExtra(Login2Activity.user, email);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "No se pudo inicar sesion",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void Registrar(View view) {
        registrarUsuario();
    }

    public void inicarSesion(View view) {
        inicarSesion();
    }


    //Google
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignResult(result);
        }
    }

    private void handleSignResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            goMainActivity();
        }else{
            Toast.makeText(this,"Se inicio sesion",Toast.LENGTH_LONG).show();
        }
    }

    private void goMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
