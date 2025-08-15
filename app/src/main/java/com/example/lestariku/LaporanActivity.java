package com.example.lestariku;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class LaporanActivity extends AppCompatActivity {

    private TextInputEditText etDate, etLocation, etReport;
    private Button btnSubmit;

    private FirebaseAuth auth;
    private DatabaseReference dbRef;

    private String userName = "";
    private String userUid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        etReport = findViewById(R.id.etReport);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setEnabled(false); // Tombol disable dulu sampai data user terbaca

        loadUserData();

        etDate.setOnClickListener(v -> showDatePicker());
        btnSubmit.setOnClickListener(v -> submitReport());
    }

    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userUid = user.getUid();
            dbRef.child("users").child(userUid).child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                userName = snapshot.getValue(String.class);
                            } else {
                                userName = "Anonim";
                            }
                            btnSubmit.setEnabled(true); // Aktifkan tombol setelah data ada
                            Log.d("LaporanActivity", "Nama user: " + userName);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            userName = "Anonim";
                            btnSubmit.setEnabled(true);
                            Log.e("LaporanActivity", "Gagal membaca nama: " + error.getMessage());
                        }
                    });
        } else {
            Toast.makeText(this, "User belum login!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%02d/%02d/%04d",
                            selectedDay, selectedMonth + 1, selectedYear);
                    etDate.setText(date);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void submitReport() {
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String reportText = etReport.getText().toString().trim();

        if (date.isEmpty() || location.isEmpty() || reportText.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua field!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reportsRef = dbRef.child("reports");
        String reportId = reportsRef.push().getKey();

        com.example.lestariku.model.Report laporan = new com.example.lestariku.model.Report(userUid, userName, date, location, reportText);

        if (reportId != null) {
            reportsRef.child(reportId).setValue(laporan)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Laporan berhasil dikirim!", Toast.LENGTH_SHORT).show();
                        etDate.setText("");
                        etLocation.setText("");
                        etReport.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal mengirim laporan: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e("LaporanActivity", "Error: ", e);
                    });
        }
    }
}
