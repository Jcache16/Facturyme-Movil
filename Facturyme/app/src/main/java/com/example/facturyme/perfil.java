package com.example.facturyme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class perfil extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Button btnRegresar = findViewById(R.id.btnRegresarPerfil);
        EditText name = findViewById(R.id.nameProfile);
        EditText rfc = findViewById(R.id.RFCProfile);
        Button btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        Switch switchEditar = findViewById(R.id.switchEditar);

        // Deshabilitar la edición por defecto
        name.setEnabled(false);
        rfc.setEnabled(false);

        getDataFirebase(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), name, rfc);

        switchEditar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Alternar la habilitación/deshabilitación de la edición
                name.setEnabled(isChecked);
                rfc.setEnabled(isChecked);
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(perfil.this, menu_principal.class);
                startActivity(regresar);
            }
        });

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los nuevos valores de los campos
                String nuevoNombre = name.getText().toString();
                String nuevoRFC = rfc.getText().toString();

                // Guardar los cambios en Firebase
                guardarCambiosFirebase(firebaseAuth.getCurrentUser().getUid(), nuevoNombre, nuevoRFC);
            }
        });
    }

    public void getDataFirebase(String UUID, EditText name, EditText rfc) {
        DocumentReference docRef = db.collection("users").document(UUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("name"));
                        rfc.setText(document.getString("RFC"));
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void guardarCambiosFirebase(String UUID, String nuevoNombre, String nuevoRFC) {
        DocumentReference docRef = db.collection("users").document(UUID);
        docRef
                .update("name", nuevoNombre, "RFC", nuevoRFC)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("perfil", "Cambios guardados con éxito");
                            Toast.makeText(perfil.this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("perfil", "Error al guardar cambios", task.getException());
                            Toast.makeText(perfil.this, "Error al guardar cambios", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}