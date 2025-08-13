package com.example.lestariku;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLogin;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Aksi tombol daftar
        btnRegister.setOnClickListener(v -> registerUser());

        // Aksi pindah ke login
        tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase: buat akun
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        // Data pengguna
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("username", username);
                        userMap.put("email", email);
                        userMap.put("role", "user"); // Role default

                        // Simpan ke database
                        userRef.child(uid).setValue(userMap)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Gagal simpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
