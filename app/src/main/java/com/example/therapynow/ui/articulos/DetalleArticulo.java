package com.example.therapynow.ui.articulos;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therapynow.R;

public class DetalleArticulo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulo);

        ImageButton btnVolver = findViewById(R.id.btnVolverDetalle);
        TextView tvTitulo = findViewById(R.id.tvDetalleTitulo);
        TextView tvContenido = findViewById(R.id.tvDetalleContenido);

        btnVolver.setOnClickListener(v -> finish());

        if (getIntent().getExtras() != null) {
            tvTitulo.setText(getIntent().getStringExtra("TITULO"));
            tvContenido.setText(getIntent().getStringExtra("CONTENIDO"));
        }
    }
}