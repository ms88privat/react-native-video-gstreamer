/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014-2015, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */
 
package com.library.mediaplayer.streaming.enums;

public enum PTZ_Zoom {
	
	IN(1),
	STOP(0),
	OUT(-1);
   
	private int val = 0;
	
	
	private PTZ_Zoom(int val)
		{
		this.val = val;
		
		}
	
	public int intValue() {return this.val;};
}
