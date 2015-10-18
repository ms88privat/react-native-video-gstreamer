package com.library.mediaplayer.streaming;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.library.mediaplayer.streaming.enums.StreamState;
import com.library.mediaplayer.streaming.enums.StreamingEvent;

import java.util.HashMap;

/**
 * Created by baebae on 10/8/15.
 */
public class StreamPlayer {
    public interface PlayerCallback {
        public void message(int eventType, int state, String message);
    }
    private RelativeLayout playerLayout = null;
    private StreamingLib streamingLib = null;
    private IStream myStream = null;
    private PlayerCallback callback = null;
    private boolean flagAutoPlay = false;
    public StreamPlayer()
    {

        playerLayout = null;
    }

    private Handler messageHandler = new Handler() {

        public void handleMessage(Message streamingMessage) {
            // The bundle containing all available informations and resources about the incoming event
            StreamingEventBundle myEvent = (StreamingEventBundle) streamingMessage.obj;
            StreamState streamState =  ((IStream) myEvent.getData()).getState();
            // notify the user about the event and the new Stream state.
            if (streamState == StreamState.INITIALIZED && flagAutoPlay) {
                myStream.play();
            }
            callback.message(myEvent.getEvent().ordinal(), streamState.ordinal(), myEvent.getInfo());
//            if (streamState == StreamState.INITIALIZED) {
//                callback.initializeCompleted(true);
//            } else if (streamState == StreamState.DEINITIALIZED) {
//                callback.initializeCompleted(false);
//            }
        }
    };

    public void initStream(Context context, RelativeLayout layout, String strStreamingURI, boolean flagAspectRatio, boolean flagAutoPlay, PlayerCallback callback)
    {
        this.playerLayout = layout;
        this.callback = callback;
        this.flagAutoPlay = flagAutoPlay;
        try {
            HashMap<String, String> configParams = new HashMap<String, String>();
            configParams.put("name", "Stream 1");
            configParams.put("flagAspectRatio", Boolean.toString(flagAspectRatio));
            configParams.put("uri", strStreamingURI);

            streamingLib = new StreamingLibBackend();
            streamingLib.initLib(context);

            SurfaceView view = new SurfaceView(context);
            playerLayout.removeAllViews();
            playerLayout.addView(view);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp);

            this.myStream = streamingLib.createStream(configParams, messageHandler);
            // initialize the stream
            this.myStream.prepare(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (myStream != null) {
            myStream.play();
        }
    }

    public void pause() {
        if (myStream != null) {
            myStream.pause();
        }
    }

    public void stop() {
        if (myStream != null) {
            myStream.destroy();
        }
    }

    public void setAspectRatio(boolean flagAspectRatio) {
        if (myStream != null) {
            myStream.setAspectRatio(flagAspectRatio);
        }
    }

    public void setVolume(int volume) {
        if (myStream != null) {
            myStream.setVolume(volume);
        }
    }
}
