package com.example.toktik.ui.user;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.widget.VideoView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toktik.activity.MainActivity;
import com.example.toktik.network.reception.User;
import com.example.toktik.network.reception.Video;
import com.google.android.exoplayer2.upstream.DataSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserCenterViewModel extends ViewModel {

    File videoDir;
    File firstFrameDir;
    private MutableLiveData<User> mUser;
    private MutableLiveData<List<Video>> mVideos;

    public UserCenterViewModel() {
    }

    public MutableLiveData<User> getmUser() {
        if(mUser == null){
            mUser = new MutableLiveData<>();
            loadUser();
        }
        return mUser;
    }

    public MutableLiveData<List<Video>> getmVideos() {
        if(mVideos == null){
            mVideos = new MutableLiveData<>();
            loadMyVideos();
        }
        return mVideos;
    }

    public void loadUser(){
        User u = new User();
        u.setId("001");
        u.setNickname("dracoooooooooo");
        u.setAvatar("https://draco-picbed.oss-cn-shanghai.aliyuncs.com/img/头像.jpg");
        mUser.setValue(u);
    }

    public void loadMyVideos(){
        if(!videoDir.exists()){
            videoDir.mkdir();
            return;
        }
        if(!firstFrameDir.exists()){
            videoDir.mkdir();
            return;
        }


        File[] videoFiles = videoDir.listFiles();
        File[] firstFrames = firstFrameDir.listFiles();

        ArrayList videos = new ArrayList<Video>();

        for(int i = 0; i < videoFiles.length; ++i){
            Video v = new Video();
            v.setFeedurl(videoFiles[i].getAbsolutePath());
            v.setDescription("todo");
            v.set_id("0");
            v.setAvatar(mUser.getValue().getAvatar());
            v.setLikecount(0);
            v.setThumbnails(firstFrames[i].getAbsolutePath());
            v.setNickname(mUser.getValue().getNickname());
            videos.add(v);
        }

        mVideos.setValue(videos);
    }

    public void setVideoDir(File videoDir) {
        this.videoDir = videoDir;
    }

    public void setFirstFrameDir(File firstFrameDir) {
        this.firstFrameDir = firstFrameDir;
    }
}