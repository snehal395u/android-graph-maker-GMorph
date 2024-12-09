package com.example.g_morph;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView, textView1;
    private static final int SPLASH_TIMEOUT = 5000; // 15 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);

        // Using a Handler to delay the opening of MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity
                Intent mainIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(mainIntent);
                // Close this activity
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
