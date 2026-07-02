package com.example.therapynow.ui.agenda;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therapynow.R;
import com.example.therapynow.model.Turno;
import com.example.therapynow.utils.AppExecutors;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SolicitarTurno extends AppCompatActivity {

    private ImageButton btnVolver;
    private TextView tvFechaSeleccionada;
    private Spinner spinnerHorarios;
    private Button btnConfirmar;
    private FirebaseFirestore db;
    private String fechaRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_turno);

        db = FirebaseFirestore.getInstance();

        btnVolver = findViewById(R.id.btnVolverSolicitar);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        spinnerHorarios = findViewById(R.id.spinnerHorarios);
        btnConfirmar = findViewById(R.id.btnConfirmarTurno);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (getIntent().getExtras() != null) {
            fechaRecibida = getIntent().getStringExtra("FECHA_SELECCIONADA");
        } else {
            fechaRecibida = format.format(new Date());
        }

        tvFechaSeleccionada.setText("Fecha elegida: " + fechaRecibida);

        configurarSpinnerHorarios();

        btnVolver.setOnClickListener(v -> finish());

        btnConfirmar.setOnClickListener(v -> {
            if (esFechaPasada(fechaRecibida, format)) {
                Toast.makeText(this, "No podés seleccionar una fecha anterior a hoy", Toast.LENGTH_LONG).show();
            } else {
                guardarTurnoEnFirestore();
            }
        });
    }

    private boolean esFechaPasada(String fechaStr, SimpleDateFormat format) {
        try {
            Date fechaTurno = format.parse(fechaStr);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date hoy = cal.getTime();

            return fechaTurno != null && fechaTurno.before(hoy);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void configurarSpinnerHorarios() {
        List<String> horarios = new ArrayList<>();
        horarios.add("09:00 hs");
        horarios.add("10:30 hs");
        horarios.add("14:00 hs");
        horarios.add("15:30 hs");
        horarios.add("17:00 hs");
        horarios.add("18:30 hs");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarios.setAdapter(adapter);
    }

    private void guardarTurnoEnFirestore() {
        String horarioSeleccionado = spinnerHorarios.getSelectedItem().toString().replace(" hs", "");

        btnConfirmar.setEnabled(false);

        AppExecutors.getInstance().deFondo().execute(() -> {
            String idTurno = db.collection("turnos").document().getId();
            Turno nuevoTurno = new Turno(idTurno, fechaRecibida, horarioSeleccionado, "Pendiente");

            db.collection("turnos").document(idTurno)
                    .set(nuevoTurno)
                    .addOnSuccessListener(aVoid -> {
                        AppExecutors.getInstance().deUI().execute(() -> {
                            Toast.makeText(SolicitarTurno.this, "¡Turno reservado con éxito!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    })
                    .addOnFailureListener(e -> {
                        AppExecutors.getInstance().deUI().execute(() -> {
                            btnConfirmar.setEnabled(true);
                            Toast.makeText(SolicitarTurno.this, "Error al reservar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    });
        });
    }
}