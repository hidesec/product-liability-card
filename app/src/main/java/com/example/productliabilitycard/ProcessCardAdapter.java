package com.example.productliabilitycard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProcessCardAdapter extends RecyclerView.Adapter<ProcessCardAdapter.ProcessViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    private final List<String> items;
    private final OnItemClickListener listener;

    public ProcessCardAdapter(List<String> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_process_card, parent, false);
        return new ProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
        String item = items.get(position);
        holder.tvProcessName.setText(item);
        holder.cardView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ProcessViewHolder extends RecyclerView.ViewHolder {
        TextView tvProcessName;
        CardView cardView;
        ProcessViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProcessName = itemView.findViewById(R.id.tvProcessName);
            cardView = (CardView) itemView;
        }
    }
}

