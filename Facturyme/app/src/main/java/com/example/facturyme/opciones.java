package com.example.facturyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class opciones extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                "Mostrar mi información personal",
                "IMPORTANTE: Eliminar perfil actual de Facturyme"
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
                        mostrarDatosUsuario();
                        Toast.makeText(opciones.this, "Información personal", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        eliminarPerfilYCerrarSesion();
                        Toast.makeText(opciones.this, "Cerrando sesión... ¡Te extrañaremos!", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(opciones.this, "Opción 3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
    private void mostrarDatosUsuario() {
        // Obtener el UID del usuario actual
        String uid = firebaseAuth.getCurrentUser().getUid();

        // Realizar una consulta a Firebase para obtener los datos del usuario
        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Obtener datos del documento
                                String nombreUsuario = document.getString("name");
                                String correoUsuario = document.getString("RFC");

                                // Crear un AlertDialog para mostrar los datos del usuario
                                AlertDialog.Builder builder = new AlertDialog.Builder(opciones.this);
                                builder.setTitle("Datos del Usuario");

                                // Usar un LayoutInflater para inflar el diseño personalizado del diálogo
                                LayoutInflater inflater = LayoutInflater.from(opciones.this);
                                View dialogView = inflater.inflate(R.layout.dialog_mostrar_datos_usuario, null);
                                builder.setView(dialogView);

                                // Configurar los TextViews en el diseño personalizado
                                TextView txtNombreUsuario = dialogView.findViewById(R.id.txtNombreUsuario);
                                TextView txtCorreoUsuario = dialogView.findViewById(R.id.txtCorreoUsuario);

                                // Mostrar los datos del usuario en los TextViews
                                txtNombreUsuario.setText("Nombre: " + nombreUsuario);
                                txtCorreoUsuario.setText("RFC: " + correoUsuario);

                                // Configurar el botón "Cerrar"
                                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                // Mostrar el AlertDialog
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else {
                                Log.d("opciones", "No such document");
                            }
                        } else {
                            Log.d("opciones", "get failed with ", task.getException());
                        }
                    }
                });
    }
    private void eliminarPerfilYCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(opciones.this);
        builder.setTitle("Eliminar Perfil");
        builder.setMessage("¿Estás seguro de que quieres eliminar tu perfil? Esta acción es irreversible.");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminar el perfil actual
                eliminarPerfilFirebase();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void eliminarPerfilFirebase() {
        // Obtener el usuario actual
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Eliminar el usuario de Firebase Authentication
        if (user != null) {
            user.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Cerrar sesión
                            firebaseAuth.signOut();

                            // Redirigir a MainActivity
                            Intent intent = new Intent(opciones.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Eliminar actividades anteriores
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("opciones", "Error al eliminar el perfil", e);
                            // Aquí puedes agregar código adicional para manejar el error
                            Toast.makeText(opciones.this, "Error al eliminar el perfil", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}