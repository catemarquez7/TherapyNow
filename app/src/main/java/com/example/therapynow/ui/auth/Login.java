package com.example.therapynow.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.therapynow.ui.home.Home;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.example.therapynow.R;
import com.example.therapynow.utils.AppExecutors;

public class Login extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView tvIrARegistro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        inicializarVistas();

        btnLogin.setOnClickListener(v -> intentarLogin());
        tvIrARegistro.setOnClickListener(v -> startActivity(new Intent(Login.this, Registro.class)));
    }

    private void inicializarVistas() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvIrARegistro = findViewById(R.id.tvIrARegistro);
    }

    private void intentarLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mostrarCargando(true);

        AppExecutors.getInstance().deFondo().execute(() -> {
            boolean esValido = validarCampos(email, password);

            AppExecutors.getInstance().deUI().execute(() -> {
                if (!esValido) {
                    mostrarCargando(false);
                    return;
                }
                autenticarConFirebase(email, password);
            });
        });
    }

    private boolean validarCampos(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            AppExecutors.getInstance().deUI().execute(() ->
                    Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show()
            );
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AppExecutors.getInstance().deUI().execute(() ->
                    Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
            );
            return false;
        }
        if (password.length() < 6) {
            AppExecutors.getInstance().deUI().execute(() ->
                    Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            );
            return false;
        }
        return true;
    }

    private void autenticarConFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    mostrarCargando(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "¡Sesión iniciada con éxito!", Toast.LENGTH_SHORT).show();
                        irAHome();
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void irAHome() {
        Intent intent = new Intent(Login.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarCargando(boolean cargando) {
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!cargando);
    }
}