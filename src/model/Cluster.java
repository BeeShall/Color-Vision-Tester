package model;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 5 - Color Vision Tester                *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/27/2016                                          *
************************************************************/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Cluster {

	private Collection<Pixel> pixels;
	private Pixel centroid;	
	public Cluster(){
		
	}
	/**
	 * Description: constructor to initialize the class with a centroid as a Pixel object
	 * @param centroid: centroid for the cluster
	 */
	public Cluster(Pixel centroid){
		super();
		this.pixels = new ArrayList<Pixel>();
		this.centroid = centroid;
	}
	/**
	 * Description: constructor to initialize the class with a collection of pixels to be assigned to the cluster
	 * @param pixels: pixels to be assigned to the cluter
	 */
	public Cluster(Collection<Pixel> pixels) {
		super();
		this.pixels = new ArrayList<Pixel>();
		this.pixels.addAll(pixels);
		this.centroid = Cluster.getRandomCentroid(pixels);
	}
	/**
	 * Description: Method to get pixels in the cluster
	 * @return array of pixels in the cluster
	 */
	public Pixel[] getPixels() {
		Pixel[] returnPixels = new Pixel[pixels.size()];
		pixels.toArray(returnPixels);
		return returnPixels;
	}
	/**
	 * Method to add a pixel to the cluster
	 * @param pixel pixel to be added to the cluster
	 */
	public void addPixel(Pixel pixel) {
		this.pixels.add(pixel);
	}
	/**
	 * Method to get the centroid of the cluster
	 * @return centroid of the cluster as a pixel object
	 */
	public Pixel getCentroid() {
		return centroid;
	}
	/**
	 * Method to set the centroid of the cluster
	 * @param centroid Pixel object to set as centroid
	 */
	public void setCentroid(Pixel centroid) {
		this.centroid = centroid;
	}
	public void clearPixels(){
		this.pixels.clear();
	}
	
	/**
	 * Method to get the top left pixel of the bounding box for the cluster
	 * @return co-ordinates of top left pixel as pixel object
	 */
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
	
	/**
	 * Method to get the bottom right pixel of the bounding box for the cluster
	 * @return co-ordinates of bottom right pixel as pixel object
	 */
	public Pixel getBottomRightPixel(){
		float row = Integer.MIN_VALUE;
		float col = Integer.MIN_VALUE;
		for(Pixel pixel: pixels){
			if(pixel.getRow()>row) row = pixel.getRow();
			if(pixel.getCol()>col) col = pixel.getCol();
		}
		return new Pixel(row,col,this.centroid.getColorValue());
	}
	
	/**
	 * static Method to generate random centroid given a list of pixel
	 * @param pixels pixels to create a random centroid from
	 * @return random centroid as Pixel object
	 */
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
