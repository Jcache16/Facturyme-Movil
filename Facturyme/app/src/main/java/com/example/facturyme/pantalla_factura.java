package com.example.facturyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class pantalla_factura extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantalla_factura);

        TextView textViewVariable = findViewById(R.id.textViewVariable);
        Button btnRegresarFacturasView = findViewById(R.id.btnRegresarFacturasView);
        EditText editRFC = findViewById(R.id.editRFC);
        EditText editName = findViewById(R.id.editNombre);
        EditText editRSocial = findViewById(R.id.editRSocial);
        EditText editRFiscal = findViewById(R.id.editRFiscal);
        EditText editZIP = findViewById(R.id.editZIP);
        EditText editMonto = findViewById(R.id.editMonto);
        Button btnBorrarFactura = findViewById(R.id.btnBorrarFactura);

        // Obtener el título pasado desde la actividad principal
        String titulo = getIntent().getStringExtra("titulo");

        // Mostrar el título en el TextView
        textViewVariable.setText(titulo);

        getDataFirebase(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), titulo, editName, editRFC, editRSocial, editRFiscal, editZIP, editMonto);

        btnBorrarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(pantalla_factura.this);
                builder.setTitle("Confirmar eliminación");
                builder.setMessage("¿Seguro que quieres eliminar esta factura?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lógica para eliminar la factura
                        deleteInvoice(getIntent().getStringExtra("titulo"));
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada, simplemente cerrar el cuadro de diálogo
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        btnRegresarFacturasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(pantalla_factura.this, facturas.class);
                startActivity(regresar);
            }
        });
    }


    private void deleteInvoice(String nombreFactura) {
        // Elimina la factura de la base de datos por su nombre
        String UUID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        db.collection("users").document(UUID).collection("facturas")
                .whereEqualTo("Name", nombreFactura)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            document.getReference().delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Factura eliminada con éxito de la base de datos
                                            // Ahora, actualiza la ListView en la actividad facturas
                                            Intent actualizarLista = new Intent("ACTUALIZAR_FACTURAS");
                                            LocalBroadcastManager.getInstance(pantalla_factura.this).sendBroadcast(actualizarLista);

                                            // Luego, regresa a la actividad facturas
                                            Intent regresar = new Intent(pantalla_factura.this, facturas.class);
                                            startActivity(regresar);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("TAG", "Error al eliminar factura: " + e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error al buscar la factura a eliminar: " + e.getMessage());
                    }
                });
    }
    public void getDataFirebase(String UUID, String title, EditText name, EditText rfc, EditText rSocial, EditText rFiscal, EditText zip, EditText monto) {
        db.collection("users").document(UUID).collection("facturas").whereEqualTo("Name", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        name.setText(document.getString("Name"));
                        rfc.setText(document.getString("RFC"));
                        rSocial.setText(document.getString("RSocial"));
                        rFiscal.setText(document.getString("RFiscal"));
                        zip.setText(document.getString("ZIP"));
                        monto.setText("$ " + (document.getDouble("monto")));
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}