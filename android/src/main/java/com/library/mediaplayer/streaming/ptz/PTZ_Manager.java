/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package com.library.mediaplayer.streaming.ptz;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.library.mediaplayer.streaming.enums.PTZ_Direction;
import com.library.mediaplayer.streaming.enums.PTZ_Zoom;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
public class PTZ_Manager {

	private String uri;
	private String username;
	private String password;
	private Context ctx;
	
	private static String TAG = "PTZ_Manager";
	
	/**
	 * Handles ptz commands of a remote Axis webcam. (tested on Axis 214 PTZ model)
	 * @param ctx The activity context
	 * @param uri The ptz uri of the webcam 
	 * @param username the username used for ptz authentication
	 * @param password  the username used for ptz authentication
	 */
	public PTZ_Manager(Context ctx, String uri, String username, String password)
	{
		this.ctx=ctx;
		this.uri = uri;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Zoom the webcam to the specified value (positive values are for zoom-in, negative values for zoom-out)
	 * @param value
	 */
	public void zoom(int value)
	{
		String cmd =  String.format("zoom=%d", value);
		this.sendPtzCmd(cmd);
	}
	
	/**
	 * Start zooming to the specified direction
	 * @param zoomDirection the zoom direction
	 */
	public void startZoom(PTZ_Zoom zoomDirection)
	{
		this.startZoom(zoomDirection, 30);
	}
	
    /**
     * Start zooming to the specified direction and speed
     * @param zoomDirection the zoom directiom
     * @param speed the zoom speed
     */
	public void startZoom(PTZ_Zoom zoomDirection, int speed)
	{
		String cmd =  String.format("continuouszoommove=%d",  zoomDirection.intValue()*speed);  
		this.sendPtzCmd(cmd);
	}
	
	/**
	 * Stop the zoom
	 */
	public void stopZoom()
	{
		this.startZoom(PTZ_Zoom.STOP, 0);
	}
	
	/**
	 * Start moving the webcam to the specified direction
	 *  
	 * @param direction the direction ({@link PTZ_Direction.STOP}} stops the webcam)
	 */
	public void startMove(PTZ_Direction direction)
	{
		this.startMove(direction,30);
	}
	
	/**
	 * Stop the pan and/or tilt movement of the webcam
	 */
	public void stopMove()
	{
		this.startMove(PTZ_Direction.STOP ,0);
	}
	
	/**
	 * Start moving the webcam to the specified direction and speed
	 * @param direction the moving direction
	 * @param speed the speed
	 */
	public void startMove(PTZ_Direction direction, int speed)
	{
		String cmd =  String.format("continuouspantiltmove=%s,%s",String.valueOf(direction.getX()*speed),String.valueOf(direction.getY()*speed));  
		this.sendPtzCmd(cmd);
	}
	
	/**
	 * Move the webcam to the position (and/or zoom value) specified by the preset passed as argument
	 * @param preset the preset name
	 */
	public void goTo(String preset)
	{
		String cmd =  String.format("gotoserverpresetname=%s", preset);
		this.sendPtzCmd(cmd);
	}
	
	 private void sendPtzCmd(String cmd)
     {
     	 String url =  String.format("%s?%s",this.uri, cmd); 
  
         Log.d(TAG, "sending command:"+ cmd);
         
         // Instantiate the RequestQueue.
         RequestQueue queue = Volley.newRequestQueue(this.ctx);

         // Request a string response from the provided URL.
         @SuppressWarnings({ "rawtypes", "unchecked" })
			StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                     new Response.Listener<String>() {
         	   
				
				@Override
				public void onResponse(String response) {
					Log.d(TAG,"Response is: "+  response);
				}
				
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
            	 Log.d(TAG,"Http Response Error:" + error.getMessage());
             }


         }) {
        	 	@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
				    HashMap<String, String> params = new HashMap<String, String>();
				    String creds = String.format("%s:%s",username,password);
				    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
				    params.put("Authorization", auth);
				    return params;
				}
         };
         
         
         // Add the request to the RequestQueue.
         queue.add(stringRequest);

     }
	 /**
	  * Get the uri connection string
	  * @return the uri used for connecting to the webcam
	  */
	public String getUri() {
		return uri;
	}

	/**
	 * Get the username used for the authentication
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
    
	/**
	 * Get the password used for the authentication
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
}
