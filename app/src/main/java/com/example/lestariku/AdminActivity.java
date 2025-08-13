package com.example.lestariku;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Halaman AdminActivity
 * Menampilkan dashboard admin dengan menu laporan dan berita
 * Serta hamburger menu untuk Profil dan Logout
 */
public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvWelcome;
    private DrawerLayout drawerLayout;
    private LinearLayout rightMenuPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Cek session admin, jika belum login arahkan ke LoginActivity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_admin);

        // Inisialisasi view
        tvWelcome = findViewById(R.id.tvAdminWelcome);
        drawerLayout = findViewById(R.id.drawerLayout);
        rightMenuPanel = findViewById(R.id.rightMenuPanel);
        ImageView btnMenu = findViewById(R.id.btnMenu);
        ImageView btnCloseMenu = findViewById(R.id.btnCloseMenu);

        // Tampilkan info admin
        displayAdminInfo();

        // Event tombol hamburger buka menu kanan
        btnMenu.setOnClickListener(v -> openRightMenu());

        // Event tombol X tutup menu
        btnCloseMenu.setOnClickListener(v -> closeRightMenu());

        // Event menu Profil
        rightMenuPanel.findViewById(R.id.menuProfil).setOnClickListener(v ->
                startActivity(new Intent(AdminActivity.this, ProfilActivity.class))
        );

        // Event menu Logout
        rightMenuPanel.findViewById(R.id.menuLogout).setOnClickListener(v ->
                showLogoutDialog()
        );
    }

    /** Buka panel menu kanan */
    private void openRightMenu() {
        rightMenuPanel.setVisibility(View.VISIBLE);
        drawerLayout.openDrawer(rightMenuPanel);
    }

    /** Tutup panel menu kanan */
    private void closeRightMenu() {
        drawerLayout.closeDrawer(rightMenuPanel);
    }

    /** Tampilkan nama admin di header */
    private void displayAdminInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String name = "Admin";

        if (currentUser != null) {
            name = currentUser.getDisplayName();
            if (name == null || name.isEmpty()) {
                String email = currentUser.getEmail();
                if (email != null && email.contains("@")) {
                    name = email.substring(0, email.indexOf("@"));
                }
            }
        }

        tvWelcome.setText("Selamat datang, " + name + "!");
    }

    /** Konfirmasi logout admin */
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Yakin ingin keluar dari akun?")
                .setPositiveButton("Ya", (dialog, which) -> performLogout())
                .setNegativeButton("Batal", null)
                .show();
    }

    /** Proses logout admin */
    private void performLogout() {
        mAuth.signOut();
        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
        finish();
    }

    /** Custom back press: tutup menu dulu atau minimize app */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (rightMenuPanel.getVisibility() == View.VISIBLE) {
            closeRightMenu();
        } else {
            moveTaskToBack(true);
        }
    }
}
