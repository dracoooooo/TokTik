package com.example.toktik.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.toktik.R;
import com.example.toktik.activity.VideoActivity;
import com.example.toktik.network.reception.Video;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListHolder> {

    private List<Video> videoList;

    public VideoListAdapter(){
        this.videoList = new ArrayList<>();
    }

    public void refresh(List<Video> videoList) {
        this.videoList.clear();
        if (videoList != null) {
            this.videoList.addAll(videoList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public VideoListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        VideoListHolder holder = new VideoListHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click_video", String.valueOf(holder.getLayoutPosition()));
                Intent intent = new Intent(view.getContext(), VideoActivity.class);
                intent.putParcelableArrayListExtra("videoList", (ArrayList<? extends Parcelable>) videoList);
                intent.putExtra("currentIndex", holder.getLayoutPosition());
                view.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideoListHolder holder, int position) {
        holder.bind(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoListHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        private final ImageView avatar;

        public VideoListHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            avatar = itemView.findViewById(R.id.avatar);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public ImageView getAvatar() {
            return avatar;
        }

        public void bind(final Video video){
            Glide.with(imageView).load(video.getThumbnails()).centerCrop().into(imageView);
            Glide.with(avatar).load(video.getAvatar()).apply(RequestOptions.circleCropTransform()).into(avatar);
        }
    }

}
