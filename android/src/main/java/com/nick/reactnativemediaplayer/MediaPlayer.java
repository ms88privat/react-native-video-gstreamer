package com.nick.reactnativemediaplayer;

import android.content.Context;
import android.widget.RelativeLayout;
import android.os.Handler;
import android.os.SystemClock;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;

import com.library.mediaplayer.streaming.StreamPlayer;
import java.util.ArrayList;

public class MediaPlayer extends RelativeLayout {

    private Context mContext;;
    private StreamPlayer player = null;
    private Handler handler = null;
    
    //--------------
    // static members;
    //--------------
    private static boolean flagAutoPlay = false;
    
    public static void setAutoPlay(boolean flagAutoPlay) {
        MediaPlayer.flagAutoPlay = flagAutoPlay;
    }
    
    //--------------
    // public members;
    //--------------
    public static boolean getAutoPlay() {
        return MediaPlayer.flagAutoPlay;   
    }
    
    public MediaPlayer(ThemedReactContext context) {
        super(context);
        mContext = context;
        this.setBackgroundColor(0xFFEE3333);
        handler = new Handler();
        setURL("udp://172.20.1.31:1234", false);
    }
    
    public void setURL(final String url, final boolean flagAspectRatio) {
        handler.post(new Runnable() {
           public void run() {
               player = new StreamPlayer();
               player.initStream(mContext, MediaPlayer.this, url, flagAspectRatio, getAutoPlay(), new StreamPlayer.PlayerCallback() {
                    @Override
                    public void message(int eventType, int state, String message) {
                        ReactContext reactContext = (ReactContext) mContext;
                        reactContext
                            .getNativeModule(UIManagerModule.class)
                            .getEventDispatcher().dispatchEvent( new MediaPlayerEvent(
                                getId(),
                                SystemClock.uptimeMillis(),
                                eventType,
                                state,
                                message
                            )
                        );
                    }
               });
           }
        });
        
    }
    
    public void play() {
        player.play();   
    }
    
    public void stop() {
        player.stop();   
    }
    
    public void pause() {
        player.pause();   
    }
    
    public void setVolume(int volume) {
        player.setVolume(volume);
    }
    public void setValues(ReadableArray values) {
        
    }

    public void setSelected(int selected) {
        
    }

    private final Runnable mLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(mLayoutRunnable);
    }
}
