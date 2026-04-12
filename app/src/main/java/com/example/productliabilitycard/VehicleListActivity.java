package com.example.productliabilitycard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.List;

public class VehicleListActivity extends AppCompatActivity {

    private RecyclerView rvVehicles;
    private LinearLayout btnScan;
    private TextView tvBarcodeResult;
    private TextView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        rvVehicles = findViewById(R.id.rvVehicles);
        btnScan = findViewById(R.id.btnScan);
        tvBarcodeResult = findViewById(R.id.tvBarcodeResult);
        btnLogout = findViewById(R.id.btnLogout);

        String barcodeResult = getIntent().getStringExtra("barcode_result");
        if (barcodeResult != null) {
            tvBarcodeResult.setText("ID: " + barcodeResult);
        }

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(VehicleListActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnScan.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(VehicleListActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Arahkan kamera ke barcode kendaraan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(false);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        });

        List<VehicleModel> vehicleList = buildVehicleList();

        VehicleAdapter adapter = new VehicleAdapter(this, vehicleList);
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
        rvVehicles.setAdapter(adapter);

        adapter.setOnItemClickListener(vehicle -> {
            Intent intent = new Intent(VehicleListActivity.this, VehicleMenuActivity.class);
            intent.putExtra("vehicle", vehicle);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                tvBarcodeResult.setText("ID: " + result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private List<VehicleModel> buildVehicleList() {
        List<VehicleModel> list = new ArrayList<>();
        list.add(new VehicleModel(
                "PLC-2024-001", "B 1234 CD", "Mitsubishi Pajero Sport", "SUV",
                "2022", "MMBGBKL40NF001234", "4M41U-001234",
                "Budi Santoso", "0812-3456-7890", "Jl. Sudirman No.45, Jakarta Pusat",
                "Tanggung Gugat Produk", "Rp 500.000.000", "01 Jan 2024", "31 Des 2025", "Aktif"
        ));
        list.add(new VehicleModel(
                "PLC-2024-002", "B 5678 EF", "Mitsubishi Outlander PHEV", "SUV",
                "2023", "MMBGBKL40NF005678", "4B12-005678",
                "Siti Rahayu", "0821-5678-9012", "Jl. Gatot Subroto No.12, Jakarta Selatan",
                "Tanggung Gugat Produk", "Rp 500.000.000", "15 Feb 2024", "14 Feb 2026", "Aktif"
        ));
        list.add(new VehicleModel(
                "PLC-2024-003", "B 9012 GH", "Mitsubishi Xpander", "MPV",
                "2021", "MMBGBKL40NF009012", "4A92-009012",
                "Agus Purnomo", "0857-9012-3456", "Jl. Thamrin No.78, Jakarta Pusat",
                "Tanggung Gugat Produk", "Rp 300.000.000", "10 Mar 2023", "09 Mar 2024", "Kadaluarsa"
        ));
        return list;
    }
}

