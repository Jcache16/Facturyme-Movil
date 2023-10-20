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

public class facturas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);

        ListView lvFacturas = findViewById(R.id.lvFacturas);
        Button btnRegresar = findViewById(R.id.btnRegresarFacturas);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(facturas.this, menu_principal.class);
                startActivity(regresar);
            }
        });


        //PASO 2: Crear el arreglo
        final String[] arregloFacturas= {
                "Factura #12345",
                "Detalle de compra",
                "Resumen de gastos",
                "Factura de servicios",
                "Factura de venta",
                "Estado de cuenta",
                "Factura de pedido"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arregloFacturas);
        // PASO 4: Asignar el adaptador
        lvFacturas.setAdapter(adapter);

        //PASO 5: Agregar evento a cada evento
        lvFacturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(facturas.this, "Factura #12345", Toast.LENGTH_SHORT).show();
                        Intent intent0 = new Intent(facturas.this, pantalla_factura.class);
                        String selectedItem0 = arregloFacturas[position];
                        intent0.putExtra("titulo", selectedItem0);
                        startActivity(intent0);
                        break;
                    case 1:
                        Toast.makeText(facturas.this, "Detalle de compra", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(facturas.this, pantalla_factura.class);
                        String selectedItem1 = arregloFacturas[position];
                        intent1.putExtra("titulo", selectedItem1);
                        startActivity(intent1);
                        break;
                    case 2:
                        Toast.makeText(facturas.this, "Resumen de gastos", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(facturas.this, pantalla_factura.class);
                        String selectedItem2 = arregloFacturas[position];
                        intent2.putExtra("titulo", selectedItem2);
                        startActivity(intent2);
                        break;
                    case 3:
                        Toast.makeText(facturas.this, "Factura de servicios", Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(facturas.this, pantalla_factura.class);
                        String selectedItem3 = arregloFacturas[position];
                        intent3.putExtra("titulo", selectedItem3);
                        startActivity(intent3);
                        break;
                    case 4:
                        Toast.makeText(facturas.this, "Factura de venta", Toast.LENGTH_SHORT).show();
                        Intent intent4 = new Intent(facturas.this, pantalla_factura.class);
                        String selectedItem4 = arregloFacturas[position];
                        intent4.putExtra("titulo", selectedItem4);
                        startActivity(intent4);
                        break;
                    case 5:
                        Toast.makeText(facturas.this, "Estado de cuenta", Toast.LENGTH_SHORT).show();
                        Intent intent5 = new Intent(facturas.this, pantalla_factura.class);
                        String selectedItem5 = arregloFacturas[position];
                        intent5.putExtra("titulo", selectedItem5);
                        startActivity(intent5);
                        break;
                    case 6:
                        Toast.makeText(facturas.this, "Factura de pedido", Toast.LENGTH_SHORT).show();
                        Intent intent7 = new Intent(facturas.this, pantalla_factura.class);
                        String selectedItem7 = arregloFacturas[position];
                        intent7.putExtra("titulo", selectedItem7);
                        startActivity(intent7);
                        break;
                }
            }
        });
    }
}