package model;

import java.util.ArrayList;
import java.util.Collection;

public class Cluster {
	private Collection<Pixel> pixels;
	private Pixel centroid;	
	
	public Cluster(Pixel centroid) {
		super();
		this.pixels = new ArrayList<Pixel>();
		this.centroid = centroid;
	}
	public Pixel[] getPixels() {
		Pixel[] returnPixels = new Pixel[pixels.size()];
		pixels.toArray(returnPixels);
		return returnPixels;
	}
	public void addPixel(Pixel pixel) {
		this.pixels.add(pixel);
	}
	public Pixel getCentroid() {
		return centroid;
	}
	public void setCentroid(Pixel centroid) {
		this.centroid = centroid;
	}
	public void clearPixels(){
		this.pixels.clear();
	}
	
	public Pixel getTopLeftPixel(){
		int row = Integer.MAX_VALUE,col = Integer.MAX_VALUE;
		for(Pixel pixel: pixels){
			if(pixel.getRow()<row) row = pixel.getRow();
			if(pixel.getCol()<col) col = pixel.getCol();
		}
		return new Pixel(row,col,-1);
	}
	
	
	

}
