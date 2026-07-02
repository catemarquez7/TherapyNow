package com.example.therapynow.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.therapynow.R;
import com.example.therapynow.ui.auth.Login;

public class Perfil extends AppCompatActivity {

    private ImageButton btnVolver;
    private Button btnCerrarSesion;
    private TextView tvNombre, tvEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();

        btnVolver = findViewById(R.id.btnVolverPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        tvNombre = findViewById(R.id.tvNombrePerfil);
        tvEmail = findViewById(R.id.tvEmailPerfil);

        FirebaseUser usuarioActual = mAuth.getCurrentUser();
        if (usuarioActual != null) {
            tvEmail.setText(usuarioActual.getEmail());
            if (usuarioActual.getDisplayName() != null && !usuarioActual.getDisplayName().isEmpty()) {
                tvNombre.setText(usuarioActual.getDisplayName());
            } else {
                tvNombre.setText("Paciente de TherapyNow");
            }
        }

        btnVolver.setOnClickListener(v -> finish());

        btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();

            Intent intent = new Intent(Perfil.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}