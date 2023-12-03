package com.example.facturyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class menu_principal extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);

        String userId = firebaseAuth.getCurrentUser().getUid();


        // Realizar consulta a Firestore para obtener todas las facturas del usuario
        db.collection("users").document(userId).collection("facturas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalAmount = 0.0;

                            // Iterar sobre las facturas y sumar los montos
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double monto = document.getDouble("monto");
                                totalAmount += monto;
                            }

                            // Mostrar el total en el TextView
                            String totalAmountString = String.format("$%.2f", totalAmount);
                            totalAmountTextView.setText(totalAmountString);
                        } else {
                            Log.w("menu_principal", "Error al obtener facturas", task.getException());
                        }
                    }
                });
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Paso 1, creo el intent
                Intent cerrarSes = new Intent(menu_principal.this, MainActivity.class);
                //Paso 2, inicio el intent
                firebaseAuth.signOut();
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