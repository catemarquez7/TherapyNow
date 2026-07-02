package com.example.therapynow.ui.notas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.therapynow.R;
import com.example.therapynow.model.NotaSesion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListaNota extends AppCompatActivity {

    private ImageButton btnVolver;
    private RecyclerView rvNotas;
    private NotaAdapter adaptador;
    private List<NotaSesion> listaNotas;
    private FirebaseFirestore db;
    private FloatingActionButton fabAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_nota);

        db = FirebaseFirestore.getInstance();
        listaNotas = new ArrayList<>();

        btnVolver = findViewById(R.id.btnVolverNotas);
        rvNotas = findViewById(R.id.rvNotas);
        fabAgregar = findViewById(R.id.fabAgregarNota);

        rvNotas.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new NotaAdapter(listaNotas);
        rvNotas.setAdapter(adaptador);

        btnVolver.setOnClickListener(v -> finish());

        if (fabAgregar != null) {
            fabAgregar.setOnClickListener(v -> {
                Intent intent = new Intent(ListaNota.this, CrearNota.class);
                startActivity(intent);
            });
        }

        escucharNotasEnFirestore();
    }

    private void escucharNotasEnFirestore() {
        db.collection("notas_sesion")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(ListaNota.this, "Error al cargar notas", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        com.example.therapynow.utils.AppExecutors.getInstance().deFondo().execute(() -> {

                            List<NotaSesion> listaTemporal = new ArrayList<>();
                            for (com.google.firebase.firestore.QueryDocumentSnapshot doc : value) {
                                NotaSesion nota = doc.toObject(NotaSesion.class);
                                listaTemporal.add(nota);
                            }

                            com.example.therapynow.utils.AppExecutors.getInstance().deUI().execute(() -> {
                                listaNotas.clear();
                                listaNotas.addAll(listaTemporal);
                                adaptador.notifyDataSetChanged();
                            });
                        });
                    }
                });
    }
}