package com.example.facturyme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class opciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        Button btnRegresar = findViewById(R.id.btnRegresarOpciones);
        ListView lvOpciones = findViewById(R.id.lvOpciones);


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(opciones.this, menu_principal.class);
                startActivity(regresar);
            }
        });

        //PASO 2: Crear el arreglo
        final String[] arregloOpciones= {
                "Opción 1",
                "Opción 2",
                "Opción 3"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arregloOpciones);
        // PASO 4: Asignar el adaptador
        lvOpciones.setAdapter(adapter);

        //PASO 5: Agregar evento a cada evento
        lvOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(opciones.this, "Opción 1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(opciones.this, "Opción 2", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(opciones.this, "Opción 3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}