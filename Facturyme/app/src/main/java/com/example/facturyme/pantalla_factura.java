package com.example.facturyme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class pantalla_factura extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantalla_factura);

        TextView textViewVariable = findViewById(R.id.textViewVariable);
        Button btnRegresarFacturasView = findViewById(R.id.btnRegresarFacturasView);

        // Obtener el título pasado desde la actividad principal
        String titulo = getIntent().getStringExtra("titulo");

        // Mostrar el título en el TextView
        textViewVariable.setText(titulo);

        btnRegresarFacturasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(pantalla_factura.this, facturas.class);
                startActivity(regresar);
            }
        });
    }
}