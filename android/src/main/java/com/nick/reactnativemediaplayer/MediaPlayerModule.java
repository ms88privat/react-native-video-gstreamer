package com.nick.reactnativemediaplayer;

import android.content.Context;
import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.ViewManager;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaPlayerModule extends ReactContextBaseJavaModule {

    private Context mContext;
    private MediaPlayerPackage mediaPlayerPackage = null;
    public MediaPlayerModule(ReactApplicationContext reactContext, MediaPlayerPackage mediaPlayerPackge) {
        super(reactContext);
        this.mediaPlayerPackage = mediaPlayerPackge;
    }
    
    @Override
    public String getName() {
        return "MediaPlayerAndroidModule";
    }
    
    private MediaPlayer getMediaPlayer()
    {
       List<ViewManager> viewManager = mediaPlayerPackage.getViewManager();
        if (viewManager != null && viewManager.size() > 0) {
            MediaPlayerManager mediaPlayerManager = (MediaPlayerManager)viewManager.get(0);
            return mediaPlayerManager.getMediaPlayer();
        }
        
        return null;
    }
    
    @ReactMethod
    public void setAutoPlay(boolean flagAutoPlay) {
        MediaPlayer.setAutoPlay(flagAutoPlay);
    }
    
    @ReactMethod
    public void setURL(String url, boolean flagAspectRatio) {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setURL(url, flagAspectRatio);   
        }
    }
    
    @ReactMethod
    public void play() {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.play();   
        }
    }
    
    @ReactMethod
    public void pause() {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    @ReactMethod
    public void stop() {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    @ReactMethod
    public void setVolume(int volume) {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
}
