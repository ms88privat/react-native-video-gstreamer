/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package com.library.mediaplayer.streaming.utils;

/**
 * Image size (width and height dimensions).
 */
public class Size {
    /**
     * Sets the dimensions for pictures.
     *
     * @param w the photo width (pixels)
     * @param h the photo height (pixels)
     */
    public Size(int w, int h) {
        width = w;
        height = h;
    }
    
    @Override
    public String toString()
    {
    	//return "(width:" + this.width + " , height:" + this.height + ")";
    	return   "[ " + this.width + " x " + this.height + " ]";  // [ 320 x 392 ]
    }
    
    /**
     * Compares {@code obj} to this size.
     *
     * @param obj the object to compare this size with.
     * @return {@code true} if the width and height of {@code obj} is the
     *         same as those of this size. {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Size)) {
            return false;
        }
        Size s = (Size) obj;
        return width == s.width && height == s.height;
    }
    @Override
    public int hashCode() {
        return width * 32713 + height;
    }
    /** width of the picture */
    private int width;
    
    public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	/** height of the picture */
    private int height;
}