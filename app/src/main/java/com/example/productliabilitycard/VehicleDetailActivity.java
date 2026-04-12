package com.example.productliabilitycard;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class VehicleDetailActivity extends AppCompatActivity {

    // ── Photo area ──
    private FrameLayout layoutPhotoBox;
    private LinearLayout layoutPhotoPlaceholder;
    private ImageView ivDocumentPhoto;
    private FrameLayout layoutIsTesterBox;
    private LinearLayout layoutIsTesterPlaceholder;
    private ImageView ivIsTesterImage;
    private FrameLayout layoutBrakeStickerBox;
    private LinearLayout layoutBrakeStickerPlaceholder;
    private ImageView ivBrakeStickerImage;

    // ── All scan-result TextViews ──
    private TextView tvScan1, tvScan2, tvScan3;
    private TextView tvBarcode1, tvBarcode2;
    private TextView tvFaultScan;
    private TextView tvStamp;

    private static final int PHOTO_TARGET_DOCUMENT = 0;
    private static final int PHOTO_TARGET_IS_TESTER = 1;
    private static final int PHOTO_TARGET_BRAKE_STICKER = 2;
    private int currentPhotoTarget = PHOTO_TARGET_DOCUMENT;

    // ── Tracks which TextView should receive the next scan result ──
    private TextView currentScanTarget = null;

    // ── Activity-result launchers ──
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        // ── Top bar ──
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // ── Bind vehicle-info TextViews that exist in the layout ──
        VehicleModel vehicle = getIntent().getParcelableExtra("vehicle");

        if (vehicle != null) {
            String cabinType = valueOrDash(vehicle.getVehicleType());
            setTextSafe(R.id.tvDetailCardNumber,    vehicle.getCardNumber());
            setTextSafe(R.id.tvDetailIssueDateTop,  vehicle.getIssueDate());
            setTextSafe(R.id.tvFDokProdNo,          vehicle.getCardNumber());
            setTextSafe(R.id.tvKommNo,              "-");
            setTextSafe(R.id.tvDetailVinTop,        vehicle.getVin());
            setTextSafe(R.id.tvDetailType,          cabinType);
            setTextSafe(R.id.tvDetailIssueDate,     vehicle.getIssueDate());
            setTextSafe(R.id.tvDetailVin,           vehicle.getVin());

            // Top summary row values expected by the reference form.
            setTextSafe(R.id.tvPackingMonth, extractPackingMonth(vehicle.getIssueDate()));
            setTextSafe(R.id.tvLocalProd, vehicle.getCardNumber());
            setTextSafe(R.id.tvPaint, "-");
            setTextSafe(R.id.tvInterior, "-");
        }

        findViewById(R.id.navMenu).setOnClickListener(v -> {
            Intent intent = new Intent(VehicleDetailActivity.this, VehicleMenuActivity.class);
            intent.putExtra("vehicle", vehicle);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.navForm).setOnClickListener(v -> {
            // Already on form/detail page.
        });
        findViewById(R.id.navScan).setOnClickListener(v -> {
            currentScanTarget = null;
            openBarcodeScanner();
        });

        // ── Bind scan TextViews ──
        tvScan1      = findViewById(R.id.tvScan1);
        tvScan2      = findViewById(R.id.tvScan2);
        tvScan3      = findViewById(R.id.tvScan3);
        tvBarcode1   = findViewById(R.id.tvBarcode1);
        tvBarcode2   = findViewById(R.id.tvBarcode2);
        tvFaultScan  = findViewById(R.id.tvFaultScan);
        tvStamp      = findViewById(R.id.tvStamp);

        // ── Photo area ──
        layoutPhotoBox         = findViewById(R.id.layoutPhotoBox);
        layoutPhotoPlaceholder = findViewById(R.id.layoutPhotoPlaceholder);
        ivDocumentPhoto        = findViewById(R.id.ivDocumentPhoto);
        layoutIsTesterBox = findViewById(R.id.layoutIsTesterBox);
        layoutIsTesterPlaceholder = findViewById(R.id.layoutIsTesterPlaceholder);
        ivIsTesterImage = findViewById(R.id.ivIsTesterImage);
        layoutBrakeStickerBox = findViewById(R.id.layoutBrakeStickerBox);
        layoutBrakeStickerPlaceholder = findViewById(R.id.layoutBrakeStickerPlaceholder);
        ivBrakeStickerImage = findViewById(R.id.ivBrakeStickerImage);

        // ── Wire scan click-listeners ──
        setScanListener(tvScan1);
        setScanListener(tvScan2);
        setScanListener(tvScan3);
        setScanListener(tvBarcode1);
        setScanListener(tvBarcode2);
        setScanListener(tvFaultScan);
        setScanListener(tvStamp);

        // ── Module barcode tap-areas → map to result fields ──
        findViewById(R.id.barcodeArea1).setOnClickListener(v -> {
            currentScanTarget = tvBarcode1;
            openBarcodeScanner();
        });
        findViewById(R.id.barcodeArea2).setOnClickListener(v -> {
            currentScanTarget = tvBarcode2;
            openBarcodeScanner();
        });

        // ── Section-5 quick-action icon buttons ──
        findViewById(R.id.btnScanBarcode).setOnClickListener(v -> {
            currentScanTarget = null;           // result shown as Toast
            openBarcodeScanner();
        });
        findViewById(R.id.btnUploadImage).setOnClickListener(v -> {
            currentPhotoTarget = PHOTO_TARGET_DOCUMENT;
            openGallery();
        });

        // ── Large photo-box tap → dialog ──
        layoutPhotoBox.setOnClickListener(v -> {
            currentPhotoTarget = PHOTO_TARGET_DOCUMENT;
            showPhotoDialog();
        });
        layoutIsTesterBox.setOnClickListener(v -> {
            currentPhotoTarget = PHOTO_TARGET_IS_TESTER;
            showPhotoDialog();
        });
        layoutBrakeStickerBox.setOnClickListener(v -> {
            currentPhotoTarget = PHOTO_TARGET_BRAKE_STICKER;
            showPhotoDialog();
        });

        // ── Dynamic & Fault Record buttons ──
        findViewById(R.id.btnDynamic).setOnClickListener(v ->
                Toast.makeText(this, "Dynamic", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnFaultRecord).setOnClickListener(v ->
                Toast.makeText(this, "Fault Record", Toast.LENGTH_SHORT).show());

        // ── Activity-result launchers ──
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Thumbnail returned in extras
                        if (result.getData() != null && result.getData().getExtras() != null) {
                            android.graphics.Bitmap bmp =
                                    (android.graphics.Bitmap) result.getData().getExtras().get("data");
                            if (bmp != null) showPhotoBitmap(bmp);
                        }
                    }
                });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) showPhotoUri(uri);
                    }
                });
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

    private String valueOrDash(String value) {
        return (value == null || value.trim().isEmpty()) ? "-" : value;
    }

    // ────────────────────────────────────────
    // Helpers
    // ────────────────────────────────────────

    /** Sets text on a view by id, ignoring if not found. */
    private void setTextSafe(int viewId, String text) {
        View v = findViewById(viewId);
        if (v instanceof TextView) ((TextView) v).setText(text);
    }

    /**
     * Attaches a click-listener to a scan field.
     * Tapping the field records it as currentScanTarget then launches the scanner.
     */
    private void setScanListener(TextView tv) {
        if (tv == null) return;
        tv.setOnClickListener(v -> {
            currentScanTarget = tv;
            openBarcodeScanner();
        });
    }

    // ────────────────────────────────────────
    // Photo-box dialog
    // ────────────────────────────────────────

    private void showPhotoDialog() {
        String[] options = {
                getString(R.string.scan_barcode_action),
                getString(R.string.take_photo),
                getString(R.string.from_gallery),
                getString(R.string.cancel)
        };
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.choose_action))
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            currentScanTarget = null;
                            openBarcodeScanner();
                            break;
                        case 1: openCamera();  break;
                        case 2: openGallery(); break;
                        default: dialog.dismiss();
                    }
                })
                .show();
    }

    // ────────────────────────────────────────
    // Scanner / Camera / Gallery launchers
    // ────────────────────────────────────────

    private void openBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Arahkan kamera ke barcode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        galleryLauncher.launch(intent);
    }

    // ────────────────────────────────────────
    // Display helpers
    // ────────────────────────────────────────

    private void showPhotoUri(Uri uri) {
        if (currentPhotoTarget == PHOTO_TARGET_IS_TESTER) {
            ivIsTesterImage.setImageURI(uri);
            ivIsTesterImage.setVisibility(View.VISIBLE);
            layoutIsTesterPlaceholder.setVisibility(View.GONE);
            return;
        }
        if (currentPhotoTarget == PHOTO_TARGET_BRAKE_STICKER) {
            ivBrakeStickerImage.setImageURI(uri);
            ivBrakeStickerImage.setVisibility(View.VISIBLE);
            layoutBrakeStickerPlaceholder.setVisibility(View.GONE);
            return;
        }
        ivDocumentPhoto.setImageURI(uri);
        ivDocumentPhoto.setVisibility(View.VISIBLE);
        layoutPhotoPlaceholder.setVisibility(View.GONE);
    }

    private void showPhotoBitmap(android.graphics.Bitmap bmp) {
        if (currentPhotoTarget == PHOTO_TARGET_IS_TESTER) {
            ivIsTesterImage.setImageBitmap(bmp);
            ivIsTesterImage.setVisibility(View.VISIBLE);
            layoutIsTesterPlaceholder.setVisibility(View.GONE);
            return;
        }
        if (currentPhotoTarget == PHOTO_TARGET_BRAKE_STICKER) {
            ivBrakeStickerImage.setImageBitmap(bmp);
            ivBrakeStickerImage.setVisibility(View.VISIBLE);
            layoutBrakeStickerPlaceholder.setVisibility(View.GONE);
            return;
        }
        ivDocumentPhoto.setImageBitmap(bmp);
        ivDocumentPhoto.setVisibility(View.VISIBLE);
        layoutPhotoPlaceholder.setVisibility(View.GONE);
    }

    // ────────────────────────────────────────
    // Barcode result callback
    // ────────────────────────────────────────

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String content = result.getContents();
            if (content != null) {
                if (currentScanTarget != null) {
                    // Put result directly into the field that was tapped
                    currentScanTarget.setText(content);
                    currentScanTarget.setHint("");
                    currentScanTarget = null;
                } else {
                    Toast.makeText(this, "Barcode: " + content, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Scan dibatalkan", Toast.LENGTH_SHORT).show();
                currentScanTarget = null;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

