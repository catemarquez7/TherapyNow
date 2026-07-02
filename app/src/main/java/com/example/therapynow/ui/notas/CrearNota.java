package com.example.therapynow.ui.notas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therapynow.R;
import com.example.therapynow.model.NotaSesion;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrearNota extends AppCompatActivity {

    private EditText etTitulo, etContenido;
    private Button btnGuardar;
    private ImageButton btnVolver;
    private FirebaseFirestore db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);

        db = FirebaseFirestore.getInstance();

        etTitulo = findViewById(R.id.etTituloNota);
        etContenido = findViewById(R.id.etContenidoNota);
        btnGuardar = findViewById(R.id.btnGuardarNota);
        btnVolver = findViewById(R.id.btnVolverCrear);

        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        btnGuardar.setOnClickListener(v -> prepararYGuardarNota());
    }

    private void prepararYGuardarNota() {
        String titulo = etTitulo.getText().toString().trim();
        String contenido = etContenido.getText().toString().trim();

        if (titulo.isEmpty() || contenido.isEmpty()) {
            Toast.makeText(this, "Por favor, completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnGuardar.setEnabled(false);

        com.example.therapynow.utils.AppExecutors.getInstance().deFondo().execute(() -> {

            String idNota = db.collection("notas_sesion").document().getId();
            String fechaActual = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date());

            NotaSesion nuevaNota = new NotaSesion(idNota, titulo, contenido, fechaActual);

            db.collection("notas_sesion").document(idNota)
                    .set(nuevaNota)
                    .addOnSuccessListener(aVoid -> {
                        com.example.therapynow.utils.AppExecutors.getInstance().deUI().execute(() -> {
                            Toast.makeText(CrearNota.this, "Nota guardada con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    })
                    .addOnFailureListener(e -> {
                        com.example.therapynow.utils.AppExecutors.getInstance().deUI().execute(() -> {
                            btnGuardar.setEnabled(true);
                            Toast.makeText(CrearNota.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}