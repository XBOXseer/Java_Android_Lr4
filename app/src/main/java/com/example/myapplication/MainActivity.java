package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAudio = findViewById(R.id.btnAudio);
        Button btnVideo = findViewById(R.id.btnVideo);

        btnAudio.setOnClickListener(v -> startActivity(new Intent(this, AudioPlayerActivity.class)));
        btnVideo.setOnClickListener(v -> startActivity(new Intent(this, VideoPlayerActivity.class)));
    }
}
