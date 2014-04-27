package com.sutd.PongHelpers;

/**
 * Our self-defined class that defines dimensions.
 * 
 * This class was actually created in response to the fact that Android phones
 * do not support the Dimension class in Java.awt package
 * **/

public class Dimension {
	public int height;
	public int width;

	public Dimension(int width, int height) {
		this.height = height;
		this.width = width;
	}
}
