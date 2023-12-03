package com.example.facturyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class facturas extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);

        ListView lvFacturas = findViewById(R.id.lvFacturas);
        Button btnRegresar = findViewById(R.id.btnRegresarFacturas);
        Button btnAdd = findViewById(R.id.btnNuevaFactura);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(facturas.this, nuevaFactura.class);
                startActivity(intent);
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(facturas.this, menu_principal.class);
                startActivity(regresar);
            }
        });

        ArrayList<String> arrayFacturas = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayFacturas);
        lvFacturas.setAdapter(adapter);
        getData((firebaseAuth.getCurrentUser()).getUid(), adapter, arrayFacturas);


        //PASO 5: Agregar evento a cada evento
        lvFacturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                for (int i = 0; i < arrayFacturas.size(); i++) {
                    Toast.makeText(facturas.this, (String) arrayFacturas.get(position), Toast.LENGTH_SHORT).show();
                    Intent intent0 = new Intent(facturas.this, pantalla_factura.class);
                    String selectedItem0 = (String) arrayFacturas.get(position);
                    intent0.putExtra("titulo", selectedItem0);
                    startActivity(intent0);
                }
            }
        });

// Registrar un receptor de difusión local
        BroadcastReceiver actualizarFacturasReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Actualiza la ListView al recibir la difusión
                getData((firebaseAuth.getCurrentUser()).getUid(), adapter, arrayFacturas);
            }
        };
        IntentFilter filter = new IntentFilter("ACTUALIZAR_FACTURAS");
        LocalBroadcastManager.getInstance(this).registerReceiver(actualizarFacturasReceiver, filter);

    }

    public void getData(String UUID, ArrayAdapter<String> adapter, ArrayList<String> facturas) {
        db.collection("users").document(UUID).collection("facturas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        facturas.add(document.getString("Name"));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}