package com.example.therapynow.ui.articulos;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.therapynow.R;
import com.example.therapynow.model.Articulo;
import com.example.therapynow.utils.AppExecutors;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListaArticulos extends AppCompatActivity {

    private ImageButton btnVolver;
    private RecyclerView rvArticulos;
    private ArticuloAdapter adaptador;
    private List<Articulo> listaArticulos;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulos);

        db = FirebaseFirestore.getInstance();
        listaArticulos = new ArrayList<>();

        btnVolver = findViewById(R.id.btnVolverArticulos);
        rvArticulos = findViewById(R.id.rvArticulos);

        rvArticulos.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new ArticuloAdapter(listaArticulos);
        rvArticulos.setAdapter(adaptador);

        btnVolver.setOnClickListener(v -> finish());

        escucharArticulosEnFirestore();
    }

    private void escucharArticulosEnFirestore() {
        db.collection("articulos")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(ListaArticulos.this, "Error al cargar artículos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        // 🚀 HILO DE FONDO GLOBAL
                        AppExecutors.getInstance().deFondo().execute(() -> {
                            List<Articulo> listaTemporal = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : value) {
                                Articulo art = doc.toObject(Articulo.class);
                                listaTemporal.add(art);
                            }

                            // 📺 HILO DE UI GLOBAL
                            AppExecutors.getInstance().deUI().execute(() -> {
                                listaArticulos.clear();
                                listaArticulos.addAll(listaTemporal);
                                adaptador.notifyDataSetChanged();
                            });
                        });
                    }
                });
    }
}