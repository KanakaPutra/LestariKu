package com.example.lestariku;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView tvWelcome = findViewById(R.id.tvUserWelcome);
        tvWelcome.setText("Selamat datang, Pengguna!");
    }
}
