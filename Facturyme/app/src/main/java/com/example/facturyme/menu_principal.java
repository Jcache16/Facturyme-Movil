package com.example.facturyme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu_principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Declarar boton
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        Button btnFacturas = findViewById(R.id.btnFacturas);
        Button btnOpciones = findViewById(R.id.btnOpciones);
        Button btnArchivar = findViewById(R.id.btnArchivar);
        Button btnPerfil = findViewById(R.id.btnPerfil);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Paso 1, creo el intent
                Intent cerrarSes = new Intent(menu_principal.this, MainActivity.class);
                //Paso 2, inicio el intent
                startActivity(cerrarSes);
            }
        });

        btnFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irFacturas = new Intent(menu_principal.this, facturas.class);
                startActivity(irFacturas);
            }
        });
        btnOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irOpciones = new Intent(menu_principal.this, opciones.class);
                startActivity(irOpciones);
            }
        });
        btnArchivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irArchivar = new Intent(menu_principal.this, archivar.class);
                startActivity(irArchivar);
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irPerfil = new Intent(menu_principal.this, perfil.class);
                startActivity(irPerfil);
            }
        });
    }
}