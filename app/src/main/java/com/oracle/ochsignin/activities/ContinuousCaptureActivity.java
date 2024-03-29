package com.oracle.ochsignin.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.oracle.ochsignin.R;

import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private CompoundBarcodeView barcodeView;
    private CaptureManager capture;

    private BarcodeCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_scan);

        callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String successMessage = getResources().getString(R.string.sign_in_success);
                Integer resultId = Integer.valueOf(result.getText());

                if (resultId == 151972) {
                    successMessage += "Jonathan";
                    barcodeView.setStatusText(successMessage);
                } else if (resultId == 909524550) {
                    successMessage += "Fangdi";
                    barcodeView.setStatusText(successMessage);
                }

                //Added preview of scanned barcode
                ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
                imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        };

        barcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}