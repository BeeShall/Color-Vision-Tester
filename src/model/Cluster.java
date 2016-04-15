package model;

import java.util.ArrayList;
import java.util.Collection;

public class Cluster {
	private Collection<Pixel> pixels;
	private float centroidColorValue;	
	
	public Cluster(float centroidColorValue) {
		super();
		this.pixels = new ArrayList<Pixel>();
		this.centroidColorValue = centroidColorValue;
	}
	public Pixel[] getPixels() {
		return (Pixel[]) pixels.toArray();
	}
	public void addPixel(Pixel pixel) {
		this.pixels.add(pixel);
	}
	public float getCentroidColorValue() {
		return centroidColorValue;
	}
	public void setCentroidColorValue(float centroidColorValue) {
		this.centroidColorValue = centroidColorValue;
	}
	public void clearPixels(){
		this.pixels.clear();
	}
	
	
	

}
