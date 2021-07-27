package com.example.toktik.ui.video;

import android.animation.Animator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.toktik.R;
import com.example.toktik.network.reception.Video;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;


public class VideoFragment extends Fragment {
    String TAG = "VideoFragment";

    Video video;
    private boolean isVideoPlaying;
    private boolean isLikeThisVideo;

    private GestureDetector gestureDetector;
    private PlayerView playerView;
    private SimpleExoPlayer player;

    private TextView nickName;
    private TextView description;
    private TextView likes;
    private ImageView avatar;
    private LottieAnimationView likeAnimationView;
    private LottieAnimationView pausePlayAnimationView;
    private LottieAnimationView likeButtonAnimationView;

    public VideoFragment(Video video){
        this.video = video;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        player.seekTo(0);
        player.play();
        isVideoPlaying = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
        isVideoPlaying = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        // 准备播放器
        playerView = view.findViewById(R.id.player);
        player = new SimpleExoPlayer.Builder(view.getContext()).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(video.getFeedurl());
        player.setMediaItem(mediaItem);
        player.prepare();

        // 初始化
        nickName = view.findViewById(R.id.nickname);
        description = view.findViewById(R.id.description);
        avatar = view.findViewById(R.id.avatar);
        likes = view.findViewById(R.id.likes);

        // 绑定video和view
        nickName.setText("@" + video.getNickname());
        description.setText(video.getDescription());
        likes.setText(String.valueOf(video.getLikecount()));

        // 使用glide加载头像
        Glide.with(view.getContext()).load(video.getAvatar()).apply(RequestOptions.circleCropTransform()).into(avatar);

        // 设置单击暂停\播放和双击点赞
        gestureDetector = new GestureDetector(view.getContext(), new GestureListener());
        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        // 点赞动画
        likeAnimationView = view.findViewById(R.id.animationView);
        likeAnimationView.setVisibility(LottieAnimationView.INVISIBLE);
        likeAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                likeAnimationView.setVisibility(View.INVISIBLE);
                likeAnimationView.setFrame(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        // 暂停动画
        pausePlayAnimationView = view.findViewById(R.id.animationView2);
        pausePlayAnimationView.setVisibility(View.INVISIBLE);
        pausePlayAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("animation_test", "end");
                pausePlayAnimationView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                pausePlayAnimationView.setVisibility(View.GONE);
            }
        });

        // 点赞按钮动画
        isLikeThisVideo = false; // todo 从后端拿数据
        likeButtonAnimationView = view.findViewById(R.id.animationViewSmall);
        likeButtonAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "like button clicked");
                if(isLikeThisVideo){
                    setDislike();
                }
                else {
                    setLike();
                }
            }
        });
        return view;
    }

    private void playPauseAnimation(){
        pausePlayAnimationView.setVisibility(View.VISIBLE);
        pausePlayAnimationView.setMinAndMaxFrame(0, 33);
        pausePlayAnimationView.playAnimation();
    }

    private void playPlayAnimation(){
        pausePlayAnimationView.setVisibility(View.VISIBLE);
        pausePlayAnimationView.setMinAndMaxFrame(33, 67);
        pausePlayAnimationView.playAnimation();

    }

    private void setLike(){
        likeButtonAnimationView.playAnimation();
        isLikeThisVideo = true;
        // todo 发消息给后端
    }

    private void setDislike(){
        likeButtonAnimationView.setFrame(0);
        isLikeThisVideo = false;
        // todo 发消息给后端

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            likeAnimationView.setVisibility(LottieAnimationView.VISIBLE);
            likeAnimationView.playAnimation();
            setLike();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(isVideoPlaying){
                player.pause();
                playPauseAnimation();
                isVideoPlaying = false;
            }
            else {
                player.play();
                playPlayAnimation();
                isVideoPlaying = true;
            }
            return true;
        }
    }
}