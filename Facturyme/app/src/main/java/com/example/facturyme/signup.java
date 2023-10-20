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

public class signup extends AppCompatActivity {

    TextInputEditText textRegistraCorreo, textRegistraPassword;
    TextView tvRegresarALogin;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btnRegístrarse = findViewById(R.id.btnRegístrarse);

        textRegistraCorreo = findViewById(R.id.textRegistraCorreo);
        textRegistraPassword = findViewById(R.id.textRegistraPassword);
        tvRegresarALogin = findViewById(R.id.tvRegresarALogin);

        tvRegresarALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irARegistro = new Intent(signup.this, login_activity.class);
                startActivity(irARegistro);
                finish();
            }
        });

        btnRegístrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(textRegistraCorreo.getText());
                password = String.valueOf(textRegistraPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signup.this, "Ingresa tu correo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signup.this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(signup.this, "Regístro completado", Toast.LENGTH_SHORT).show();
                                    Intent irALogin = new Intent(signup.this, login_activity.class);
                                    startActivity(irALogin);
                                    finish();
                                } else {
                                    Toast.makeText(signup.this, "Error de autenticación, verifica tus credenciales", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}