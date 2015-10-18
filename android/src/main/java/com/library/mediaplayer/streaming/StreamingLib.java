/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package com.library.mediaplayer.streaming;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler;

public interface StreamingLib {
	
	/**
	 * Initialize the streaming library. Note that you must call this method before using any other method of the library
	 * @param context the application context
	 * @throws Exception if an error occurred during the library initialization
	 */
	public void initLib(Context context)  throws Exception;
	
	/**
	 * This factory method provides a new IStrean instance
	 * @param configParams All needed configuration string parameters. All the supported parameters are the following:
	 * 	<ul>
	 * 		<li>name: (mandatory) the name of the stream (it must be unique for stream)</li>
	 * 		<li>uri: (mandatory) the uri of the stream (it can be also changed later)</li>
	 * 		<li>latency: (optional) the preferred latency of the stream in ms (default value: 200 ms) </li>
	 * 	</ul>
	 * 
	 * @param notificationHandler the handler where to receive all notifications from the Library
	 *
	 * @throws Exception if an error occurred during the stream initialization
	 */
	public IStream createStream(HashMap<String,String> configParams, Handler notificationHandler) throws Exception;
	
}
