package com.example.toktik.ui.home;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toktik.activity.MainActivity;
import com.example.toktik.network.call.VideoCall;
import com.example.toktik.network.reception.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeViewModel extends ViewModel {

    private String TAG = "HomeViewModel";

    private MutableLiveData<List<Video>> mVideos;
    private VideoCall videoCall;

    public HomeViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        videoCall = retrofit.create(VideoCall.class);
    }

    public LiveData<List<Video>> getHomePageVideos(){
        if(mVideos == null){
            mVideos = new MutableLiveData<List<Video>>();
            loadVideos();
        }
        return mVideos;
    }

    private void loadVideos(){
        Call<List<Video>> call = videoCall.getHomePageVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                mVideos.setValue(response.body());
                Log.d(TAG, "get video list success");
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.d(TAG, "get video list failed");
            }
        });
    }
}