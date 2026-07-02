package com.example.therapynow;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.therapynow.ui.auth.Login;
import com.example.therapynow.ui.home.Home;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Control de sesión activa de Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, Home.class));
        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
        finish();
    }
}