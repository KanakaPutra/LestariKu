package com.example.lestariku;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi UI
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Login button
        btnLogin.setOnClickListener(v -> loginUser());

        // Pindah ke Register
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validasi
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong");
            return;
        }

        // Login ke Firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        // Ambil data pengguna dari Realtime Database
                        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String role = snapshot.child("role").getValue(String.class);

                                    if ("admin".equalsIgnoreCase(role)) {
                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                    }
                                    finish(); // Tutup halaman login
                                } else {
                                    Toast.makeText(LoginActivity.this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
