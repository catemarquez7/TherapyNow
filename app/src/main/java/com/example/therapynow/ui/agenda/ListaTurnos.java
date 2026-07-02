package com.example.therapynow.ui.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.therapynow.R;
import com.example.therapynow.model.Turno;
import com.example.therapynow.utils.AppExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListaTurnos extends AppCompatActivity {

    private ImageButton btnVolver;
    private CalendarView calendarView;
    private RecyclerView rvTurnos;
    private FloatingActionButton fabAgendar;

    private TurnosAdapter adaptador;
    private List<Turno> listaTurnos;
    private FirebaseFirestore db;

    private String fechaParaEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_turnos);

        db = FirebaseFirestore.getInstance();
        listaTurnos = new ArrayList<>();

        fechaParaEnviar = new SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(new Date());

        btnVolver = findViewById(R.id.btnVolverAgenda);
        calendarView = findViewById(R.id.calendarView);
        rvTurnos = findViewById(R.id.rvTurnos);
        fabAgendar = findViewById(R.id.fabAgendar);

        rvTurnos.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new TurnosAdapter(listaTurnos);
        rvTurnos.setAdapter(adaptador);

        btnVolver.setOnClickListener(v -> finish());

        fabAgendar.setOnClickListener(v -> {
            Intent intent = new Intent(ListaTurnos.this, SolicitarTurno.class);
            intent.putExtra("FECHA_SELECCIONADA", fechaParaEnviar);
            startActivity(intent);
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaParaEnviar = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(this, "Fecha seleccionada: " + fechaParaEnviar, Toast.LENGTH_SHORT).show();
        });

        escucharTurnosEnFirestore();
    }

    private void escucharTurnosEnFirestore() {
        db.collection("turnos")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(ListaTurnos.this, "Error al cargar turnos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        AppExecutors.getInstance().deFondo().execute(() -> {

                            List<Turno> listaTemporal = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : value) {
                                Turno turno = doc.toObject(Turno.class);
                                listaTemporal.add(turno);
                            }

                            AppExecutors.getInstance().deUI().execute(() -> {
                                listaTurnos.clear();
                                listaTurnos.addAll(listaTemporal);
                                adaptador.notifyDataSetChanged();
                            });
                        });
                    }
                });
    }
}