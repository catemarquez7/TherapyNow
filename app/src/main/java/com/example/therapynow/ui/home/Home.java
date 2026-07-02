package com.example.therapynow.ui.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therapynow.ui.notas.ListaNota;
import com.example.therapynow.ui.articulos.ListaArticulos;
import com.example.therapynow.ui.perfil.Perfil;
import com.google.android.material.card.MaterialCardView;
import com.example.therapynow.R;
import com.example.therapynow.ui.agenda.ListaTurnos;



public class Home extends AppCompatActivity {

    private MaterialCardView cardAgenda, cardNotas, cardArticulos, cardPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardAgenda = findViewById(R.id.cardAgenda);
        cardNotas = findViewById(R.id.cardNotas);
        cardArticulos = findViewById(R.id.cardArticulos);
        cardPerfil = findViewById(R.id.cardPerfil);

        cardAgenda.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, ListaTurnos.class));
        });

        cardNotas.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, ListaNota.class));
        });

        cardArticulos.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, ListaArticulos.class));
        });

        cardPerfil.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Perfil.class));
        });
    }
}