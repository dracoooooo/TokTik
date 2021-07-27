package com.example.toktik.ui.user;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.toktik.R;
import com.example.toktik.databinding.FragmentUserBinding;
import com.example.toktik.network.reception.User;
import com.example.toktik.network.reception.Video;
import com.example.toktik.ui.home.VideoListAdapter;

import java.io.File;
import java.util.List;

public class UserCenterFragment extends Fragment {

    private UserCenterViewModel userCenterViewModel;
    private FragmentUserBinding binding;
    private RecyclerView recycleView;
    VideoListAdapter videoListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userCenterViewModel = new ViewModelProvider(this).get(UserCenterViewModel.class);
        userCenterViewModel.setVideoDir(this.getActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES));
        userCenterViewModel.setFirstFrameDir(this.getActivity().getExternalFilesDir(Environment.DIRECTORY_ALARMS));
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView avatar = binding.avatar;
        final TextView uid = binding.uid;
        final TextView nickname = binding.nickname;

//        Glide.with(avatar).load("https://draco-picbed.oss-cn-shanghai.aliyuncs.com/img/background.jpg").centerCrop().into(background);

        userCenterViewModel.getmUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Glide.with(avatar).load(user.getAvatar()).apply(RequestOptions.circleCropTransform()).into(avatar);
                nickname.setText(user.getNickname());
                uid.setText("uid: " + user.getId());
            }
        });

        recycleView = root.findViewById(R.id.my_videos);
        recycleView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        recycleView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // 不显示默认的分隔线
            }
        });

        videoListAdapter = new VideoListAdapter();
        recycleView.setAdapter(videoListAdapter);

        userCenterViewModel.getmVideos().observe(getViewLifecycleOwner(), new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videoList) {
                videoListAdapter.refresh(videoList);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        userCenterViewModel.loadUser();
        userCenterViewModel.loadMyVideos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}