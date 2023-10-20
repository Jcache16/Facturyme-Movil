package com.example.facturyme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declarar boton
        Button btnIniciarApp = findViewById(R.id.btnIniciarApp);

        btnIniciarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Colocar la acción que queremos que realice el botón
                Toast.makeText(getApplicationContext(), "Bienvenido:)", Toast.LENGTH_SHORT).show();
                //Crear un INTENT para acceder a la interfaz del Login
                //Paso 1, creo el intent
                Intent pasarLogin = new Intent(getApplicationContext(), login_activity.class);
                //Paso 2, inicio el intent
                startActivity(pasarLogin);
            }
        });
    }
}