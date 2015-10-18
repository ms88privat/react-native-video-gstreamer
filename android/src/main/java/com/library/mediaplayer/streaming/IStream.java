/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package com.library.mediaplayer.streaming;



import android.view.SurfaceView;

import com.library.mediaplayer.streaming.enums.StreamProperty;
import com.library.mediaplayer.streaming.enums.StreamState;
import com.library.mediaplayer.streaming.utils.Size;

/**
 * An IStream object represents a single audio/video stream object. You can obtain a new IStream object by calling
 * the method {@link StreamingLib#createStream(java.util.HashMap, android.os.Handler)}.
 *
 */
public interface IStream {
	
	 
    /**
     * 
     * @return the name of this stream
     */
    public String getName();
    
    
    /**
     * 
     * @return the current state of this stream
     */
    public StreamState getState();
    
    /**
     * 
     * @return the current size of the video stream
     */
    public Size getVideoSize();
    
    
    /**
	 * Prepare the stream by providing a video surface
	 * @param surface the Surface where to render the stream
	 *
	 */
	public void prepare(SurfaceView surface);
	
    /**
     * Play the stream
     */
	public void play() ;
	
	/**
	 * pause the stream
	 */
	public void pause();
	
	public void setAspectRatio(boolean isAspectRatio);

	public void setVolume(int volume);
	/**
	 * Destroy this stream
	 */
	public void destroy();

    /**
     * Reads the current value of the specified stream property
     * @param property
     * @return the value of the property
     */
	public Object getProperty(StreamProperty property);
	
	/**
	 * Commit the stream properties values specified as argument
	 * @param properties the stream properties to update
	 * @return true if no error occurred during the update request; False otherwise
	 */
	public boolean commitProperties(StreamProperties properties);

	/**
	 * Load a still image from the remote camera, provided the uri
	 * @param uri the uri pointing to the image to load
	 * @return <code>true</code> if no error occurred during the operation, <code>false</code> otherwise
	 */
	public boolean loadStillImage(String uri);
	
	/**
	 * Get detailed informations about a stream error (return an empty stream if the stream is not in Stream.ERROR state)
	 * @return infomrations about the type of stream error
	 */
	public String getErrorMsg();
		
}

