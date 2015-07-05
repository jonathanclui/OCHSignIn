package com.oracle.ochsignin.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.oracle.ochsignin.R;
import com.oracle.ochsignin.models.OCHEmployee;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class SignInActivity extends AppCompatActivity {

    private static String ORACLE_ID_FORMAT = "CODE_39";

    // UI Elements
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private Button mScanButton;

    // Java Elements
    Set<OCHEmployee> validEmployees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Setup UI Components
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_coordinator);
        mScanButton = (Button) findViewById(R.id.button_scan_barcode);
        setUpToolbar();

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode(v);
            }
        });

        ParseQuery<OCHEmployee> employeeQuery = ParseQuery.getQuery("OCHEmployee");
        employeeQuery.orderByDescending("employeeId");

        validEmployees = new HashSet<>();

        try {
            validEmployees = new HashSet<OCHEmployee>(employeeQuery.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the ZXing class to scan a barcode of type CODE39
     * @param v
     */
    private void scanBarcode(View v) {
        try {

            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt(getResources().getString(R.string.scan_prompt));
            integrator.setBeepEnabled(true);
            integrator.initiateScan();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process activity result from CaptureActivity
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String resultContents = result.getContents();
                String resultFormat = result.getFormatName();
                int resultId = -1;

                // Try Parsing the resultContents into an integer
                try {
                    resultId = Integer.valueOf(resultContents);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (!resultFormat.equals(ORACLE_ID_FORMAT)) {
                    Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.sign_in_error),
                            Snackbar.LENGTH_LONG).show();
                } else if(resultId != -1) {
                    Iterator employeesIter = validEmployees.iterator();
                    while (employeesIter.hasNext()) {
                        OCHEmployee emp = (OCHEmployee) employeesIter.next();
                        if (emp.getEmployeeId() == resultId) {
                            String successMessage = getResources().getString(R.string.sign_in_success);
                            successMessage += emp.getEmployeeName();
                            Snackbar.make(mCoordinatorLayout, successMessage, Snackbar.LENGTH_LONG).show();

                            emp.setAttended(true);
                            emp.saveInBackground();
                            break;
                        }
                    }
                } else {
                    Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.sign_in_error),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Set up the toolbar object
     */
    private void setUpToolbar() {
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(R.string.activity_sign_in);
        }
    }
}
