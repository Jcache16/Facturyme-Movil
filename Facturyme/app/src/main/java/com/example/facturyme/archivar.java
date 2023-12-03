package com.example.facturyme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class archivar extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_FILE = 1;
    private EditText editRFC, editNombre, editRSocial, editRFiscal, editCP, editMonto;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivar);

        Button btnRegresar = findViewById(R.id.btnRegresarArchivar);
        Button btnUpload = findViewById(R.id.btnUpload);
        Button btnGuardarFacturaSubida = findViewById(R.id.btnGuardarFacturaSubida);
        editRFC = findViewById(R.id.editRFC);
        editNombre = findViewById(R.id.editNombre);
        editRSocial = findViewById(R.id.editRSocial);
        editRFiscal = findViewById(R.id.editRFiscal);
        editCP = findViewById(R.id.editZIP);
        editMonto = findViewById(R.id.editMonto);

        String UUID = firebaseAuth.getCurrentUser().getUid();

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
                // Abre el explorador de archivos para seleccionar un archivo
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Selecciona todos los tipos de archivos
                startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
            }
        });

        btnGuardarFacturaSubida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(editRFC.getText())){
                    Toast.makeText(archivar.this,"Ingresa tu RFC", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editNombre.getText())){
                    Toast.makeText(archivar.this,"Ingresa tu nombre", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editRSocial.getText())){
                    Toast.makeText(archivar.this,"Ingresa tu rSocial", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editRFiscal.getText())){
                    Toast.makeText(archivar.this,"Ingresa tu rFiscal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editCP.getText())){
                    Toast.makeText(archivar.this,"Ingresa tu Código Postal", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(archivar.this, facturas.class);
                addDataFirebase(UUID, editNombre.getText().toString(), editRFC.getText().toString(), editRSocial.getText().toString(), editRFiscal.getText().toString(), editCP.getText().toString(), Double.valueOf(editMonto.getText().toString()));
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
    private void parseXMLAndFillEditTexts(InputStream is) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            String currentTag = null;
            HashMap<String, String> data = new HashMap<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    currentTag = parser.getName();
                } else if (eventType == XmlPullParser.TEXT) {
                    if (currentTag != null) {
                        data.put(currentTag, parser.getText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    // Procesa los datos del XML y actualiza los EditText correspondientes
                    if (currentTag != null) {
                        switch (currentTag) {
                            case "RFC":
                                editRFC.setText(data.get(currentTag));
                                break;
                            case "Nombre":
                                editNombre.setText(data.get(currentTag));
                                break;
                            case "RSocial":
                                editRSocial.setText(data.get(currentTag));
                                break;
                            case "RFiscal":
                                editRFiscal.setText(data.get(currentTag));
                                break;
                            case "CP":
                                editCP.setText(data.get(currentTag));
                                break;
                            case "Monto":
                                editMonto.setText(data.get(currentTag));
                                break;
                            // Agrega más casos según los datos que necesitas
                        }
                        currentTag = null;
                    }
                }

                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();

            // Procesa el archivo y extrae los datos
            if (selectedFileUri != null) {
                try {
                    InputStream is = getContentResolver().openInputStream(selectedFileUri);
                    // Llama a la función de análisis XML y actualiza los EditText
                    parseXMLAndFillEditTexts(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
