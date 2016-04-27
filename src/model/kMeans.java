package model;
/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 5 - Color Vision Tester                *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/27/2016                                          *
************************************************************/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Bishal
 *Class that hold the k-means algorith
 */
public class kMeans {
	//k-means value
	private int K;
	//list of clusters
	private List<Cluster> clusters;
	//list of pixels to cluster on
	private Collection<Pixel> pixels;
	//type of clustering : color or distance
	private static kMeansReading readingType;
	//no of iterations for clustering
	private int noOfIterations; 
	
	public int getNoOfIterations() {
		return noOfIterations;
	}

	public kMeans(int k,kMeansReading readingType){
		this.K = k;
		this.clusters = new ArrayList<Cluster>();
		this.pixels = new ArrayList<Pixel>();
		this.readingType = readingType;
		this.noOfIterations = 0;
		
	}
	
	/**
	 * Method to initialize clustering
	 * Initializes each cluster with random values and assign the pixels to these centroids and keeps updating
	 */
	public void init(){		
		initClusters();
		assignPointToClusters();
		update();
	}
	
	public void setPixels(Collection<Pixel> pixels){
		this.pixels = pixels;
	}
	
	/**
	 * Method to add pixels
	 * @param pixel pixel to add
	 */
	public void addPixel (Pixel pixel){
		this.pixels.add(pixel);
	}
	
	/**
	 * Metthod to initialize all the clusters with a random value
	 */
	private void initClusters(){
		if(kMeans.readingType == kMeansReading.COLOR){
			float scale = (float)1/this.K;
			float limit = (float )0-scale;
			for(int i=0; i<this.K; i++){
				float randomCentroidValue = (limit+limit+scale)/2;
				limit+=scale;
				//System.out.println("RandomCentroid Value: "+randomCentroidValue );
				clusters.add(new Cluster(new Pixel(-1,-1,randomCentroidValue)));
			}
		}
		else{			
			clusters.add(new Cluster(pixels));
			//System.out.println("1. row"+ (maxRow-(maxRow/2) +" col"+ (maxCol-(maxCol/2)) ));
			clusters.add(new Cluster(pixels));
			//System.out.println("2. row"+ (maxRow+(maxRow/2) +" col"+ (maxCol+(maxCol/2)) ));
		}
	}	
	
	/**
	 * Method to assign the pixels to the clusters based on their closeness to the centroid
	 */
	private void assignPointToClusters(){
		for(Pixel pixel: pixels){
			int assignmentClusterIndex = -1;
			float meansDiff = -1;
			for(Cluster c: clusters){
				//if its the first time set the values for this pixels as default
				if(meansDiff == -1){
					if(this.readingType == kMeansReading.COLOR){
						meansDiff = Math.abs(pixel.getColorValue()-c.getCentroid().getColorValue());
					}
					else{
						//if distance then calculate the distance between two pixels
						meansDiff =(float) (Math.pow(pixel.getRow()-c.getCentroid().getRow(), 2)+ Math.pow(pixel.getCol()-c.getCentroid().getCol(), 2));
						//System.out.println("Means diff "+meansDiff);
					}
					assignmentClusterIndex = clusters.indexOf(c);
				}
				//if not compare with the previosu ones
				else{
					float newMeansDiff;
					if(this.readingType == kMeansReading.COLOR){
						newMeansDiff = Math.abs(pixel.getColorValue()-c.getCentroid().getColorValue());
					}
					else{
						newMeansDiff = (float) (Math.pow(pixel.getRow()-c.getCentroid().getRow(), 2)+ Math.pow(pixel.getCol()-c.getCentroid().getCol(), 2));
					}
					//System.out.println(newColorDiff);
					if(newMeansDiff<meansDiff){
						assignmentClusterIndex = clusters.indexOf(c);
						meansDiff = newMeansDiff;
					}
				}
			}
			clusters.get(assignmentClusterIndex).addPixel(pixel);
		}

    
	}
	
	/**
	 * Method to iterate and update the clusters
	 */
	private void update(){
		boolean finished = true;
		do{
			//save the last centroids
			final List<Pixel> lastCentroids = getCentroids();
			/*System.out.println("last Centroid");
			for(Pixel p: lastCentroids){
				System.out.println(p.getRow()+" "+p.getCol());
			}*/
			recalculateCentroids();
			//reclaculate new centroids
			noOfIterations++;
			/*System.out.println("new Centroid");
			for(Cluster c: clusters){
				System.out.println("Centyroid: "+ c.getCentroid().getRow()+" "+c.getCentroid().getCol()+" "+ c.getCentroid().getColorValue());
			}*/
				
			finished = true;
			for(int i=0; i<clusters.size();i++){
				//checking if the last centroid value for all the clusters is the same.
				//if yes we are finished
				boolean condition;
				if(this.readingType == kMeansReading.COLOR){
					condition = clusters.get(i).getCentroid().getColorValue() != lastCentroids.get(i).getColorValue();					
				}
				else{
					condition = (clusters.get(i).getCentroid().getRow() != lastCentroids.get(i).getRow()) || (clusters.get(i).getCentroid().getCol() != lastCentroids.get(i).getCol());
				}
				if(condition) finished = false;
			}

			if(!finished){
				for(Cluster c: clusters){
					c.clearPixels();
				}
				assignPointToClusters();
			}
			
		}while(!finished);			
	}
	
	/**
	 * Mehod to get all the centroids
	 * @return List of centroids from all clusters
	 */
	private List<Pixel> getCentroids(){
		List<Pixel> centroids = new ArrayList<Pixel>();
		for(Cluster c: clusters){
			Pixel centroidPixel = c.getCentroid();
			centroids.add(new Pixel(centroidPixel.getRow(),centroidPixel.getCol(),centroidPixel.getColorValue()));
		}
		return centroids;
	}
	
	/**
	 * Method to recalculate the centroid for each cluster by taking mean of the values from each pixel in the cluster
	 */
	private void recalculateCentroids(){
		//System.out.println("Recalculating");
		for(Cluster c: clusters){
			float colorSum= 0;
			float rowSum = 0;
			float colSum = 0;
			Pixel[] clusterPixels = c.getPixels();
			if(clusterPixels.length != 0){
				for(Pixel pixel: clusterPixels ){
					if(this.readingType == kMeansReading.COLOR){
						colorSum += pixel.getColorValue();
					} 
					else{
						rowSum += pixel.getRow();
						colSum += pixel.getCol();
					}
				}
				c.setCentroid(new Pixel((float)rowSum/clusterPixels.length,(float)colSum/clusterPixels.length,(float)colorSum/clusterPixels.length));
			}
			else{
				c.setCentroid(new Pixel((float)-1,(float)-1,(float) -1));
			}
			
			
			/*	System.out.println("Centyroid: "+ c.getCentroid().getRow()+" "+c.getCentroid().getCol()+" "+ c.getCentroid().getColorValue());
				System.out.println("No of pixels: "+ c.getPixels().length);
				System.out.println();*/
			
		}

		
	}
	
	/**
	 * Method to get the deductions from clustering
	 * @return Array of deduced clusters
	 */
	public Cluster[] getDeductions(){
		Cluster[] returnClusters = new Cluster[clusters.size()];
		clusters.toArray(returnClusters);
		return returnClusters;
	}

}
