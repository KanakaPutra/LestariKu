package com.example.lestariku;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView tvWelcome = findViewById(R.id.tvAdminWelcome);
        tvWelcome.setText("Selamat datang, Admin!");
    }
}
