package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Cluster {


	private Collection<Pixel> pixels;
	private Pixel centroid;	
	public Cluster(){
		
	}
	public Cluster(Pixel centroid){
		super();
		this.pixels = new ArrayList<Pixel>();
		this.centroid = centroid;
	}
	public Cluster(Collection<Pixel> pixels) {
		super();
		this.pixels = new ArrayList<Pixel>();
		this.pixels.addAll(pixels);
		this.centroid = Cluster.getRandomCentroid(pixels);
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
		
		float row = Integer.MAX_VALUE;
		float col = Integer.MAX_VALUE;
		for(Pixel pixel: pixels){
				//System.out.println(pixel.getRow()+" "+pixel.getCol());
			if(pixel.getRow()<row) row = pixel.getRow();
			if(pixel.getCol()<col) col = pixel.getCol();
			//System.out.println("TopLeft "+row+" "+col);
		}
		return new Pixel(row,col,-1);
	}
	
	public Pixel getBottomRightPixel(){
		float row = Integer.MIN_VALUE;
		float col = Integer.MIN_VALUE;
		for(Pixel pixel: pixels){
			if(pixel.getRow()>row) row = pixel.getRow();
			if(pixel.getCol()>col) col = pixel.getCol();
		}
		return new Pixel(row,col,this.centroid.getColorValue());
	}
	
	private static Pixel getRandomCentroid(Collection<Pixel> pixels) {
		float maxRow= -1; 
		float maxCol = -1;
		for(Pixel pixel: pixels){
			if(pixel.getRow()>maxRow)maxRow = pixel.getRow();
			if(pixel.getCol()>maxCol)maxCol = pixel.getCol();
		}
		Random r = new Random();
		float randomX = (r.nextFloat() * maxRow);
		float randomY = (int) (r.nextFloat() * maxCol);
		
		//System.out.println("Random centroid: "+randomX+" "+randomY);
		
		return new Pixel(randomX, randomY);
	}
	
	
	

}
