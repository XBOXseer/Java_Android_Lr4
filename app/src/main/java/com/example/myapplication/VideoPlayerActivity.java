package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import android.widget.MediaController;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView fileNameView;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private Uri videoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnChoose = findViewById(R.id.btnChooseFile);
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPause = findViewById(R.id.btnPause);
        Button btnStop = findViewById(R.id.btnStop);
        fileNameView = findViewById(R.id.txtFileName);
        videoView = findViewById(R.id.videoView);

        videoView.setMediaController(new MediaController(this));

        btnChoose.setOnClickListener(v -> pickVideoFile());

        btnPlay.setOnClickListener(v -> {
            if (videoUri != null) {
                videoView.start();
            }
        });

        btnPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        });

        btnStop.setOnClickListener(v -> {
            videoView.stopPlayback();
            videoView.setVideoURI(videoUri); // reload video
        });

        btnBack.setOnClickListener(v -> {
            videoView.stopPlayback();
            finish();
        });

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        videoUri = result.getData().getData();
                        fileNameView.setText(videoUri.getLastPathSegment());
                        videoView.setVideoURI(videoUri);
                    }
                });
    }

    private void pickVideoFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        filePickerLauncher.launch(intent);
    }
}
