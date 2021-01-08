package com.air.localmusicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles> musicFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Songs");
        setSupportActionBar(toolbar);

        Check_Permission();
        musicFiles = getAllAudioFiles(this);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1){
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.Accent));
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.Accent));
                    toolbar.setTitle("Albums");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.Accent));
                    }else {
                        Toast.makeText(MainActivity.this, "unable to change status bar colour", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.primary));
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.primary));
                    toolbar.setTitle("Songs");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.primary));
                    }else {
                        Toast.makeText(MainActivity.this, "unable to change status bar colour", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1){
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.Accent));
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.Accent));
                    toolbar.setTitle("Albums");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.Accent));
                    }else {
                        Toast.makeText(MainActivity.this, "unable to change status bar colour", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.primary));
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.primary));
                    toolbar.setTitle("Songs");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.primary));
                    }else {
                        Toast.makeText(MainActivity.this, "unable to change status bar colour", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void Check_Permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }else {
            musicFiles = getAllAudioFiles(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    musicFiles = getAllAudioFiles(this);
                }else {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
                    }
                }
            }
    }

    public static ArrayList<MusicFiles> getAllAudioFiles(Context context){
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, //Path
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(path,title,artist,album,duration);

                //take log for check files
                Log.e("path: "+path,"album :"+album);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;

    }
    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle("Songs");
    }
}