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
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.therapynow.R;
import com.example.therapynow.model.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Registro extends AppCompatActivity {

    private TextInputEditText etNombre, etEmail, etPassword, etConfirmPassword;
    private Button btnRegistrar;
    private ProgressBar progressBar;
    private TextView tvIrALogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        executorService = Executors.newSingleThreadExecutor();

        inicializarVistas();

        btnRegistrar.setOnClickListener(v -> intentarRegistro());
        tvIrALogin.setOnClickListener(v -> finish()); // Vuelve al Login
    }

    private void inicializarVistas() {
        etNombre = findViewById(R.id.etNombreRegistro);
        etEmail = findViewById(R.id.etEmailRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);
        etConfirmPassword = findViewById(R.id.etConfirmPasswordRegistro);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        progressBar = findViewById(R.id.progressBarRegistro);
        tvIrALogin = findViewById(R.id.tvIrALogin);
    }

    private void intentarRegistro() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        mostrarCargando(true);

        executorService.execute(() -> {
            boolean esValido = validarCampos(nombre, email, password, confirmPassword);
            runOnUiThread(() -> {
                if (!esValido) {
                    mostrarCargando(false);
                    return;
                }
                crearUsuarioEnFirebase(nombre, email, password);
            });
        });
    }

    private boolean validarCampos(String nombre, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            runOnUiThread(() -> Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show());
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            runOnUiThread(() -> Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show());
            return false;
        }
        if (password.length() < 6) {
            runOnUiThread(() -> Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show());
            return false;
        }
        if (!password.equals(confirmPassword)) {
            runOnUiThread(() -> Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show());
            return false;
        }
        return true;
    }

    private void crearUsuarioEnFirebase(String nombre, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        guardarUsuarioEnFirestore(uid, nombre, email);
                    } else {
                        mostrarCargando(false);
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarUsuarioEnFirestore(String uid, String nombre, String email) {
        Usuario nuevoUsuario = new Usuario(uid, nombre, email, "paciente");

        db.collection("usuarios")
                .document(uid)
                .set(nuevoUsuario)
                .addOnCompleteListener(task -> {
                    mostrarCargando(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                        irAHome();
                    } else {
                        Toast.makeText(this, "Error al guardar perfil en la base de datos", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void irAHome() {
        Intent intent = new Intent(Registro.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarCargando(boolean cargando) {
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnRegistrar.setEnabled(!cargando);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}