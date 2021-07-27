package com.example.toktik.network.call;

import com.example.toktik.network.reception.Reception;
import com.example.toktik.network.reception.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VideoCall {

    @GET("https://beiyou.bytedance.com/api/invoke/video/invoke/video")
    Call<List<Video>> getHomePageVideos();
}
