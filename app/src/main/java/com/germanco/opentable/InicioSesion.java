package com.germanco.opentable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesion extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    LoginButton loginButton;
    CallbackManager callbackManager;
    Intent intent;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    GoogleApiClient googleApiClient;
    SignInButton signInButton;
    Button registroButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        registroButton=(Button) findViewById(R.id.irRegistro);
        callbackManager=CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.facebookInicio);
        signInButton=(SignInButton)findViewById(R.id.googleInicio);
        firebaseAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesionFacebook();
            }
        });

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario=firebaseAuth.getCurrentUser();
                if(usuario!=null){
                    Toast.makeText(InicioSesion.this, "Sesión iniciada con éxito",Toast.LENGTH_LONG).show();
                    intent= new Intent(InicioSesion.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,777);
            }
        });

        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(InicioSesion.this, RegistroUsuario.class);
                startActivity(intent);
            }
        });


    }

    public void iniciarSesionFacebook(){
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                FacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(InicioSesion.this, "Inicio de sesión cancelado",Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(InicioSesion.this,"Algo ha salido mal. Intenta de nuevo",Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if(requestCode==777){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            Intent intent= new Intent(InicioSesion.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Sesión iniciada con éxito",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"No se pudo iniciar sesión",Toast.LENGTH_LONG).show();
        }
    }

    private void FacebookAccessToken(AccessToken token){
        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(InicioSesion.this,"Ha ocurrido un error. Intenta de nuevo",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
