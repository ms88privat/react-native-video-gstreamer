package com.nick.reactnativemediaplayer;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class MediaPlayerEvent extends Event<MediaPlayerEvent> {
    public static final String EVENT_NAME = "topChange";
    
    private final int eventType;
    private final int streamState;
    private final String message;

    public MediaPlayerEvent(int viewId, long timestampMs, int eventType, int streamState, String message) {
        super(viewId, timestampMs);
        this.eventType = eventType;
        this.streamState = streamState;
        this.message = message;
    }

    public int getEventType() {
        return eventType;
    }

    public int getStreamState() {
        return streamState;
    }
    
    public String getMessage() {
        return message;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public short getCoalescingKey() {
        return 0;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap eventData = Arguments.createMap();
        eventData.putInt("eventType", getEventType());
        eventData.putInt("streamState", getStreamState());
        eventData.putString("message", getMessage());
        return eventData;
    }
}
