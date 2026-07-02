package com.example.therapynow.ui.notas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.therapynow.R;
import com.example.therapynow.model.NotaSesion;
import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {

    private List<NotaSesion> listaNotas;

    public NotaAdapter(List<NotaSesion> listaNotas) {
        this.listaNotas = listaNotas;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        NotaSesion nota = listaNotas.get(position);
        holder.tvTitulo.setText(nota.getTitulo());
        holder.tvFecha.setText(nota.getFecha());
        holder.tvDetalle.setText(nota.getContenido());
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvFecha, tvDetalle;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloNota);
            tvFecha = itemView.findViewById(R.id.tvFechaNota);
            tvDetalle = itemView.findViewById(R.id.tvContenidoNota);
        }
    }
}