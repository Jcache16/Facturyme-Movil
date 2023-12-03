package com.example.facturyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class nuevaFactura extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);

        Button btnRegresar = findViewById(R.id.btnRegresarFacturasView);
        Button btnAdd = findViewById(R.id.btnAdd);
        EditText editRFC = findViewById(R.id.editRFC);
        EditText editName = findViewById(R.id.editNombre);
        EditText editRSocial = findViewById(R.id.editRSocial);
        EditText editRFiscal = findViewById(R.id.editRFiscal);
        EditText editZIP = findViewById(R.id.editZIP);
        EditText editMonto = findViewById(R.id.editMonto);

        String UUID = firebaseAuth.getCurrentUser().getUid();


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(nuevaFactura.this, facturas.class);
                startActivity(regresar);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(editRFC.getText())){
                    Toast.makeText(nuevaFactura.this,"Ingresa tu RFC", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editName.getText())){
                    Toast.makeText(nuevaFactura.this,"Ingresa tu nombre", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editRSocial.getText())){
                    Toast.makeText(nuevaFactura.this,"Ingresa tu rSocial", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editRFiscal.getText())){
                    Toast.makeText(nuevaFactura.this,"Ingresa tu rFiscal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editZIP.getText())){
                    Toast.makeText(nuevaFactura.this,"Ingresa tu ZIP", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(nuevaFactura.this, facturas.class);
                addDataFirebase(UUID, editName.getText().toString(), editRFC.getText().toString(), editRSocial.getText().toString(), editRFiscal.getText().toString(), editZIP.getText().toString(), Double.valueOf(editMonto.getText().toString()));
                startActivity(intent);}
        });
    }

    public void addDataFirebase(String UUID, String name, String RFC, String rSocial, String rFiscal, String zip, Double monto) {
        Map<String, Object> data = new HashMap<>();
        data.put("Name", name);
        data.put("RFC", RFC);
        data.put("RSocial", rSocial);
        data.put("RFiscal", rFiscal);
        data.put("ZIP", zip);
        data.put("monto", monto);

        db.collection("users").document(UUID).collection("facturas").document()
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }
}