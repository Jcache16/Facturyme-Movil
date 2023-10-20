package com.example.facturyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {
    TextInputEditText textInCorreo, textInPassword;
    TextView tvCrearCuenta;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declarar variables
        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        textInCorreo = findViewById(R.id.textInCorreo);
        textInPassword = findViewById(R.id.textInPassword);
        tvCrearCuenta = findViewById(R.id.tvCrearCuenta);

        tvCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irARegistro = new Intent(login_activity.this, signup.class);
                startActivity(irARegistro);
                finish();
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(textInCorreo.getText());
                password = String.valueOf(textInPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(login_activity.this,"Ingresa tu correo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(login_activity.this,"Ingresa tu contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(login_activity.this,"¡Bienvenid@!", Toast.LENGTH_SHORT).show();
                                    Intent irAMenu = new Intent(login_activity.this, menu_principal.class);
                                    startActivity(irAMenu);
                                    finish();
                                }
                                else {
                                    Toast.makeText(login_activity.this,"Error de autenticación, verifica tus credenciales", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}