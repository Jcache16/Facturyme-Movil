package com.example.facturyme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class archivar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivar);

        Button btnRegresar = findViewById(R.id.btnRegresarArchivar);
        Button btnUpload = findViewById(R.id.btnUpload);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(archivar.this, menu_principal.class);
                startActivity(regresar);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(archivar.this, "Aún no se pueden subir archivos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}