package com.germanco.opentable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroUsuario extends AppCompatActivity {
    EditText email, password, confirmPassword;
    Button registerButton;
    Intent intent;
    String correo, contraseña, confirmaContraseña;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        //INICIALIZACIÓN DE COMPONENTES
        email=(EditText)findViewById(R.id.correoRegistro);
        password=(EditText)findViewById(R.id.contraseñaRegistro);
        confirmPassword=(EditText)findViewById(R.id.confirmaRegistro);
        registerButton=(Button)findViewById(R.id.botonRegistro);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registraUsuarioPorCorreo();
            }
        });

    }

    private void registraUsuarioPorCorreo(){
        firebaseAuth=FirebaseAuth.getInstance();
        ProgressDialog progressDialog= new ProgressDialog(this);
        correo= email.getText().toString().trim();
        contraseña= password.getText().toString().trim();
        confirmaContraseña= confirmPassword.getText().toString().trim();
        if(TextUtils.isEmpty(correo)==true){
            Toast.makeText(this,"No has introducido tu email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(contraseña)==true){
            Toast.makeText(this,"No has introducido tu contraseña",Toast.LENGTH_LONG).show();
            return;
        }

        if(contraseña.equals(confirmaContraseña) && contraseña.length()>8){
            progressDialog.setMessage("Registrando usuario...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistroUsuario.this,"Registro Completado",Toast.LENGTH_LONG).show();
                        intent= new Intent(RegistroUsuario.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(RegistroUsuario.this,"Registro Incorrecto. Intenta de nuevo",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }else {
            Toast.makeText(RegistroUsuario.this, "Contraseña incorrecta. Intenta de nuevo", Toast.LENGTH_LONG).show();
        }
    }

}
