package com.example.productliabilitycard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.Arrays;
import java.util.Collections;

public class VehicleListActivity extends AppCompatActivity {

    private static final String EXTRA_MENU_GROUP = "menu_group";

    private ListView lvProcesses;
    private LinearLayout btnScan;
    private TextView tvBarcodeResult;
    private TextView btnLogout;
    private TextView tvHeaderTitle;
    private TextView tvSectionTitle;

    private String currentMenuGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        lvProcesses = findViewById(R.id.lvProcesses);
        btnScan = findViewById(R.id.btnScan);
        tvBarcodeResult = findViewById(R.id.tvBarcodeResult);
        btnLogout = findViewById(R.id.btnLogout);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        tvSectionTitle = findViewById(R.id.tvSectionTitle);

        currentMenuGroup = getIntent().getStringExtra(EXTRA_MENU_GROUP);

        configureHeader();
        setupProcessList();

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

    private void configureHeader() {
        if (currentMenuGroup == null) {
            tvHeaderTitle.setText("Daftar Proses Produksi");
            tvSectionTitle.setText("Pilih area proses di bawah ini");
        } else {
            tvHeaderTitle.setText("Sub Proses " + currentMenuGroup);
            tvSectionTitle.setText("Pilih sub proses untuk lanjut ke Product Liability Card");
        }
    }

    private void setupProcessList() {
        final java.util.List<String> items;

        if (currentMenuGroup == null) {
            items = Arrays.asList("Welding", "Painting", "Cabin", "EOL", "Audit");
        } else if ("Welding".equals(currentMenuGroup)) {
            items = Arrays.asList("TA", "TO");
        } else if ("EOL".equals(currentMenuGroup)) {
            items = Arrays.asList("EOL 1", "EOL 2");
        } else {
            items = Collections.emptyList();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items
        );
        lvProcesses.setAdapter(adapter);

        lvProcesses.setOnItemClickListener((parent, view, position, id) -> {
            String selected = items.get(position);

            if (currentMenuGroup == null && ("Welding".equals(selected) || "EOL".equals(selected))) {
                openSubMenu(selected);
                return;
            }

            String process = currentMenuGroup == null ? selected : currentMenuGroup;
            String subProcess = currentMenuGroup == null ? null : selected;
            openProductLiabilityCard(process, subProcess);
        });
    }

    private void openSubMenu(String group) {
        Intent intent = new Intent(VehicleListActivity.this, VehicleListActivity.class);
        intent.putExtra(EXTRA_MENU_GROUP, group);
        String barcodeResult = tvBarcodeResult.getText().toString();
        if (!barcodeResult.isEmpty()) {
            intent.putExtra("barcode_result", barcodeResult.replace("ID: ", ""));
        }
        startActivity(intent);
    }

    private void openProductLiabilityCard(String process, String subProcess) {
        Intent intent = new Intent(VehicleListActivity.this, VehicleMenuActivity.class);
        intent.putExtra("vehicle", buildDefaultVehicle());
        intent.putExtra("selected_process", process);
        if (subProcess != null) {
            intent.putExtra("selected_subprocess", subProcess);
            intent.putExtra("selected_menu", process + " - " + subProcess);
        } else {
            intent.putExtra("selected_menu", process);
        }
        startActivity(intent);
    }

    private VehicleModel buildDefaultVehicle() {
        return new VehicleModel(
                "PLC-2024-001", "B 1234 CD", "Mitsubishi Pajero Sport", "SUV",
                "2022", "MMBGBKL40NF001234", "4M41U-001234",
                "Budi Santoso", "0812-3456-7890", "Jl. Sudirman No.45, Jakarta Pusat",
                "Tanggung Gugat Produk", "Rp 500.000.000", "01 Jan 2024", "31 Des 2025", "Aktif"
        );
    }
}

