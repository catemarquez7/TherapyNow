package com.example.therapynow.ui.agenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.therapynow.R;
import com.example.therapynow.model.Turno;
import java.util.List;

public class TurnosAdapter extends RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder> {

    private List<Turno> listaTurnos;

    public TurnosAdapter(List<Turno> listaTurnos) {
        this.listaTurnos = listaTurnos;
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        Turno turno = listaTurnos.get(position);
        holder.tvFecha.setText(turno.getFecha());
        holder.tvHora.setText("Hora: " + turno.getHora() + " hs");
        holder.tvEstado.setText(turno.getEstado().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return listaTurnos.size();
    }

    public static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvHora, tvEstado;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFechaTurno);
            tvHora = itemView.findViewById(R.id.tvHoraTurno);
            tvEstado = itemView.findViewById(R.id.tvEstadoTurno);
        }
    }
}