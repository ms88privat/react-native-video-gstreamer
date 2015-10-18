package com.nick.reactnativemediaplayer;

import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;

public class MediaPlayerManager extends SimpleViewManager<MediaPlayer> {
    public static final String REACT_CLASS = "MediaPlayerAndroidComponent";
    private MediaPlayer mediaPlayer = null;
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected MediaPlayer createViewInstance(ThemedReactContext context) {
        mediaPlayer = new MediaPlayer(context);
        return mediaPlayer;
    }

    @UIProp(UIProp.Type.NUMBER)
    public static final String PROP_SELECTED = "selected";

    @Override
    public void updateView(MediaPlayer view, CatalystStylesDiffMap props) {
        super.updateView(view, props);

    }
    
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }   
}