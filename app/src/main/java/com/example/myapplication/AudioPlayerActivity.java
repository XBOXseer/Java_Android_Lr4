package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AudioPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private boolean isPaused = false;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private Button btnPlay, btnPause, btnStop;
    private TextView fileNameView;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        seekBar = findViewById(R.id.seekBar);
        Button btnChoose = findViewById(R.id.btnChooseFile);
        Button btnBack = findViewById(R.id.btnBack);
        fileNameView = findViewById(R.id.txtFileName);

        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer != null && isPaused) {
                mediaPlayer.start();
                isPaused = false;
            }
        });

        btnPause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPaused = true;
            }
        });

        btnStop.setOnClickListener(v -> stopPlayback());

        btnBack.setOnClickListener(v -> {
            stopPlayback();
            finish();
        });

        btnChoose.setOnClickListener(v -> pickAudioFile());

        // Register file picker callback
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri audioUri = result.getData().getData();
                        String name = audioUri.getLastPathSegment();
                        fileNameView.setText(name);
                        playAudio(audioUri);
                    }
                });
    }

    private void pickAudioFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        filePickerLauncher.launch(intent);
    }

    private void playAudio(Uri audioUri) {
        stopPlayback();
        mediaPlayer = MediaPlayer.create(this, audioUri);
        if (mediaPlayer == null) {
            Toast.makeText(this, "Не вдалося відтворити файл", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            seekBar.setMax(mp.getDuration());
            mp.start();
            updateSeekBar();
        });

        mediaPlayer.setOnCompletionListener(mp -> stopPlayback());
    }

    private void updateSeekBar() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(updateSeekBar);
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            seekBar.setProgress(0);
            isPaused = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayback();
    }
}
