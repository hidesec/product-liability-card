package com.example.productliabilitycard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class VehicleMenuActivity extends AppCompatActivity {

    private TextView tvMenuPlate;
    private TextView tvMenuVehicleName;
    private TextView tvMenuCardNumber;
    private TextView tvInfoModel;
    private TextView tvInfoType;
    private TextView tvInfoPackingMonth;
    private TextView tvInfoVinNo;
    private TextView tvInfoFDokNo;
    private TextView tvInfoPaintCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_menu);

        tvMenuPlate = findViewById(R.id.tvMenuPlate);
        tvMenuVehicleName = findViewById(R.id.tvMenuVehicleName);
        tvMenuCardNumber = findViewById(R.id.tvMenuCardNumber);
        tvInfoModel = findViewById(R.id.tvInfoModel);
        tvInfoType = findViewById(R.id.tvInfoType);
        tvInfoPackingMonth = findViewById(R.id.tvInfoPackingMonth);
        tvInfoVinNo = findViewById(R.id.tvInfoVinNo);
        tvInfoFDokNo = findViewById(R.id.tvInfoFDokNo);
        tvInfoPaintCode = findViewById(R.id.tvInfoPaintCode);

        VehicleModel vehicle = getIntent().getParcelableExtra("vehicle");
        String selectedProcess = getIntent().getStringExtra("selected_process");
        String selectedSubProcess = getIntent().getStringExtra("selected_subprocess");
        String selectedMenuFromList = getIntent().getStringExtra("selected_menu");

        if (vehicle != null) {
            tvMenuPlate.setText(vehicle.getPlateNumber() != null ? vehicle.getPlateNumber() : vehicle.getVin());
            tvMenuVehicleName.setText(vehicle.getVehicleName());
            tvMenuCardNumber.setText("No. Kartu: " + vehicle.getCardNumber());

            tvInfoModel.setText(vehicle.getVehicleName());
            tvInfoType.setText(vehicle.getVehicleType());
            tvInfoPackingMonth.setText(extractPackingMonth(vehicle.getIssueDate()));
            tvInfoVinNo.setText(vehicle.getVin());
            tvInfoFDokNo.setText(vehicle.getCardNumber());
            tvInfoPaintCode.setText("-");
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.navMenu).setOnClickListener(v -> {
            // Already on menu page.
        });
        findViewById(R.id.navForm).setOnClickListener(v -> {
            Intent intent = new Intent(VehicleMenuActivity.this, VehicleDetailActivity.class);
            intent.putExtra("vehicle", vehicle);
            putSelectionExtras(intent, selectedProcess, selectedSubProcess, selectedMenuFromList);
            startActivity(intent);
        });
        findViewById(R.id.navScan).setOnClickListener(v -> {
            Intent intent = new Intent(VehicleMenuActivity.this, VehicleDetailActivity.class);
            intent.putExtra("vehicle", vehicle);
            intent.putExtra("selected_menu", "1. Barcode");
            putSelectionExtras(intent, selectedProcess, selectedSubProcess, "1. Barcode");
            startActivity(intent);
        });

        int[] cardIds = {
            R.id.cardProductionOrder, R.id.cardBarcode, R.id.cardIncoming, R.id.cardLocalJobCard,
            R.id.cardHankOnParts, R.id.cardStation00, R.id.cardStation01, R.id.cardStation02,
            R.id.cardStation03, R.id.cardStation04, R.id.cardStation05, R.id.cardStation06,
            R.id.cardStation07, R.id.cardStation08, R.id.cardStation09, R.id.cardQGate01,
            R.id.cardTechnicStation, R.id.cardStation12, R.id.cardStation13, R.id.cardStation14,
            R.id.cardStation15, R.id.cardStation16, R.id.cardStation17, R.id.cardStation18,
            R.id.cardStation19, R.id.cardQGate02, R.id.cardSACockpit, R.id.cardSAFrontDoor,
            R.id.cardSAFrontModule, R.id.cardSAEngineTransmission, R.id.cardSAWheel, R.id.cardCheckEngine,
            R.id.cardWheelAlignment, R.id.cardFinishLine, R.id.cardChassisSubAssembly, R.id.cardAfterRoller,
            R.id.cardRectification, R.id.cardShortageParts, R.id.cardPaintFinish, R.id.cardFinishArea
        };

        String[] cardLabels = {
            "Production Order", "1. Barcode", "Incoming Inspection", "Local Job Card",
            "Hank On Parts", "Station 00", "Station 01", "Station 02",
            "Station 03", "Station 04", "Station 05", "Station 06",
            "Station 07", "Station 08", "Station 09", "Q-Gate 01",
            "Technic Station", "Station 12", "Station 13", "Station 14",
            "Station 15", "Station 16", "Station 17", "Station 18",
            "Station 19", "Q-Gate 02", "SA Cockpit", "SA Front Door",
            "SA Front Module", "SA Engine Transmission", "SA Wheel", "Check Engine",
            "Wheel Alignment", "Finish Line", "Chassis Sub Assembly", "After Roller",
            "Rectification/Rework", "Shortage Parts", "Paint Finish", "Finish Area"
        };

        for (int i = 0; i < cardIds.length; i++) {
            final String label = cardLabels[i];
            LinearLayout card = findViewById(cardIds[i]);
            card.setOnClickListener(v -> {
                Intent intent = new Intent(VehicleMenuActivity.this, VehicleDetailActivity.class);
                intent.putExtra("vehicle", vehicle);
                intent.putExtra("selected_menu", label);
                putSelectionExtras(intent, selectedProcess, selectedSubProcess, label);
                startActivity(intent);
            });
        }
    }

    private void putSelectionExtras(Intent intent, String process, String subProcess, String selectedMenu) {
        if (process != null) {
            intent.putExtra("selected_process", process);
        }
        if (subProcess != null) {
            intent.putExtra("selected_subprocess", subProcess);
        }
        if (selectedMenu != null) {
            intent.putExtra("selected_menu", selectedMenu);
        }
    }

    private String extractPackingMonth(String issueDate) {
        if (issueDate == null || issueDate.trim().isEmpty()) {
            return "-";
        }
        String[] parts = issueDate.trim().split("\\s+");
        if (parts.length >= 3) {
            return parts[1] + " " + parts[2];
        }
        return issueDate;
    }
}
