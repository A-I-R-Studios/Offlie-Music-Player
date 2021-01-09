package com.air.localmusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.air.localmusicplayer.MainActivity.musicFiles;

public class PlayerActivity extends AppCompatActivity {

    TextView song_name, artist_name,duration_played, duration_total;
    ImageView cover_art, next_button, prevBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int Position= -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else {
            Toast.makeText(this, "unable to change status bar colour", Toast.LENGTH_SHORT).show();
        }

        initValues();
        getIntentMethod();
        song_name.setText(listSongs.get(Position).getTitle());
        String artist = listSongs.get(Position).getArtist();
        if (artist.equals("<unknown>")){
            artist_name.setText("Unknown Artist");

        }else {
            artist_name.setText(artist);


        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!= null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!= null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    private String formattedTime(int mCurrentPosition) {
        String Totalout = "";
        String Totalnew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minute = String.valueOf(mCurrentPosition / 60);
        Totalout = minute + ":" + seconds;
        Totalnew = minute + ":"+"0" + seconds;
        if (seconds.length() == 1){
            return  Totalnew;
        }else {
            return Totalout;
        }


    }

    private void getIntentMethod() {
        Position = getIntent().getIntExtra("position",-1);
        listSongs = musicFiles;
        if (listSongs != null){
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(Position).getPath());
        }

        try {
            stopPlaying();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            metaData(uri);

        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void initValues() {
        song_name = findViewById(R.id.song_Name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPlayed);
        duration_total = findViewById(R.id.durationTotal);
        cover_art = findViewById(R.id.coverArt);
        next_button = findViewById(R.id.id_next);
        prevBtn = findViewById(R.id.id_prev);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.id_shuffle);
        repeatBtn = findViewById(R.id.id_repeat);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekBar);


    }
    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(Position).getDuration())/1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null){
            Glide.with(this).asBitmap().load(art).placeholder(R.drawable.anime_boy).into(cover_art);
        }else {
            Glide.with(this).load(R.drawable.anime_boy).placeholder(R.drawable.anime_boy).into(cover_art);
        }
    }
}