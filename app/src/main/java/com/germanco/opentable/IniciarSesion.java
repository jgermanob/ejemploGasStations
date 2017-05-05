package com.germanco.opentable;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.concurrent.Executor;


public class IniciarSesion{
    Boolean usuarioCreado;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    IniciarSesion(Context context){}

    public boolean registraUsuarioConCorreo(final Context context, EditText correo, EditText contraseña, EditText confirmaContraseña){
        String email, password, confirmPassword;
        usuarioCreado=false;
        ProgressDialog progressDialog= new ProgressDialog(context);
        email=correo.getText().toString().trim();
        password=contraseña.getText().toString().trim();
        confirmPassword=confirmaContraseña.getText().toString().trim();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(context,"No has introducido correctamente tus datos",Toast.LENGTH_LONG).show();
        }else {
            if(password.equals(confirmPassword) && password.length()>8){
                progressDialog.setMessage("Registrando...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Executor) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            usuarioCreado=true;
                        }else{
                            Toast.makeText(context,"Algo ha salido mal. Intenta de nuevo",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
            }else{
                Toast.makeText(context,"No has introducido correctamente tus datos",Toast.LENGTH_LONG).show();

            }
        }
        return usuarioCreado;
    }
}
