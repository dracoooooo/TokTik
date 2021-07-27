package com.example.toktik.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.toktik.R;
import com.example.toktik.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.toktik.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {
    String TAG = "Main_activity";

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_record, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // 使用新的listener覆盖了原来的navigation
        findViewById(R.id.navigation_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "navigation_record clicked");
                dispatchTakeVideoIntent();
            }
        });
    }

    // 录像
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            String videoPath = videoUri.getPath();
            media.setDataSource(videoPath);
            Bitmap bitmap = media.getFrameAtTime();

            File targetDir = getExternalFilesDir(Environment.DIRECTORY_ALARMS);
            if(!targetDir.exists()){
                targetDir.mkdir();
            }

            File saveImg = new File(targetDir, videoUri.getLastPathSegment().substring(0, videoUri.getLastPathSegment().length() - 4) + ".jpg");

            try {
                FileOutputStream imgOut = new FileOutputStream(saveImg);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imgOut);
                imgOut.flush();
                imgOut.close();
            }catch (Exception e){
                Log.d(TAG, e.getMessage());
            }

        }
    }

    private void dispatchTakeVideoIntent() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        File targetDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if(!targetDir.exists()){
            targetDir.mkdir();
        }

        long timeStamp = System.currentTimeMillis();

        String filePath = targetDir + File.separator + "VID_" + String.valueOf(timeStamp) + ".mp4"; // 保存路径
        Uri uri = Uri.fromFile(new File(filePath));   // 将路径转换为Uri对象
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);  // 跳转
    }

}