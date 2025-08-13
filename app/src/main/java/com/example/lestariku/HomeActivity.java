package com.example.lestariku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView btnLapor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        // Jika user sudah login, langsung ke Home User
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(HomeActivity.this, UserActivity.class));
            finish(); // Supaya tidak bisa kembali ke halaman guest
            return;
        }

        btnLapor = findViewById(R.id.btnLapor);

        btnLapor.setOnClickListener(v -> {
            // Karena ini halaman guest, user pasti null, jadi tampilkan alert login
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Login Diperlukan")
                    .setMessage("Anda harus login terlebih dahulu untuk melaporkan masalah.")
                    .setPositiveButton("Login", (dialog, which) -> {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}
