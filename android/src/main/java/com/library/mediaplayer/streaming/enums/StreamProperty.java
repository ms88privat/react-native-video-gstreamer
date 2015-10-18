/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014-2015, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package com.library.mediaplayer.streaming.enums;

public enum StreamProperty {
	/**
	 * the stream name property
	 */
	NAME,
	/**
	 * the stream uri property
	 */
    URI,  
    /**
     * the preferred stream latency property
     */
    LATENCY,
    
    /**
     * the video size
     */
    VIDEO_SIZE,
    
    /**
     * the current state (read only )
     */
    STATE
     
}
