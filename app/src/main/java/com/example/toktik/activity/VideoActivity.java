package com.example.toktik.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.toktik.R;
import com.example.toktik.network.reception.Video;
import com.example.toktik.ui.video.VideoFragment;

import java.util.List;

public class VideoActivity extends FragmentActivity {
    List<Video> videoList;
    int currentVideoIndex;
    FragmentStateAdapter pagerAdapter;

    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoList = getIntent().getParcelableArrayListExtra("videoList");
        currentVideoIndex = getIntent().getIntExtra("currentIndex", 0);

        viewPager2 = findViewById(R.id.view_pager2);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        VideoPagerAdapter screenSlidePagerAdapter = new VideoPagerAdapter(this);
        viewPager2.setAdapter(screenSlidePagerAdapter);

        viewPager2.setCurrentItem(currentVideoIndex);
    }

    private class VideoPagerAdapter extends FragmentStateAdapter {
        public VideoPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new VideoFragment(videoList.get(position));
        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }
    }
}