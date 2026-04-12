package com.example.productliabilitycard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private final Context context;
    private final List<VehicleModel> vehicleList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(VehicleModel vehicle);
    }

    public VehicleAdapter(Context context, List<VehicleModel> vehicleList) {
        this.context = context;
        this.vehicleList = vehicleList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vehicle_card, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        VehicleModel vehicle = vehicleList.get(position);
        holder.tvPlatNumber.setText(vehicle.getPlateNumber());
        holder.tvVehicleName.setText(vehicle.getVehicleName());
        holder.tvVehicleType.setText(vehicle.getVehicleType());
        holder.tvCardNumber.setText(vehicle.getCardNumber());
        holder.tvExpiryDate.setText(vehicle.getExpiryDate());

        if (vehicle.getStatus().equalsIgnoreCase("Aktif")) {
            holder.tvStatus.setText("  Aktif  ");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_active, null));
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_active);
        } else {
            holder.tvStatus.setText("  Kadaluarsa  ");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_expired, null));
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_expired);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(vehicle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlatNumber, tvVehicleName, tvVehicleType, tvCardNumber, tvExpiryDate, tvStatus;

        VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlatNumber = itemView.findViewById(R.id.tvPlatNumber);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
            tvCardNumber = itemView.findViewById(R.id.tvCardNumber);
            tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}

