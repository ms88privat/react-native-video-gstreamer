/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014-15, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package com.library.mediaplayer.streaming.enums;

/**
 * Contains all events triggered by the Streaming library
 * 
 */
public enum StreamingEvent {
   /** a stream internal state change occurred */
  STREAM_STATE_CHANGED,  
  /** the size of the video stream changed */ 
  VIDEO_SIZE_CHANGED,      
  /** a streaming error occurred */
  STREAM_ERROR,
  /** a streaming message */
  STREAM_MESSAGE
}
   
