package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class kMeans {
	private int K;
	private List<Cluster> clusters;
	private Collection<Pixel> pixels;
	static private kMeansReading readingType;
	
	public kMeans(int k,kMeansReading readingType){
		this.K = k;
		this.clusters = new ArrayList<Cluster>();
		this.pixels = new ArrayList<Pixel>();
		this.readingType = readingType;
		
	}
	
	public void init(){		
		initClusters();
		assignPointToClusters();
		update();
		/*System.out.println("Final");
		for(Cluster cluster: clusters){
			System.out.println("Centyroid: "+cluster.getCentroid().getColorValue());
		System.out.println("No of pixels: "+ cluster.getPixels().length);

		}*/
		
		/*if(readingType == kMeansReading.DISTANCE){
		
			for(Cluster cluster: clusters){
				System.out.println("Centyroid: "+ cluster.getCentroid().getRow()+" "+cluster.getCentroid().getCol()+" "+ cluster.getCentroid().getColorValue());
				System.out.println("No of pixels: "+ cluster.getPixels().length);
	
			}
		}*/
	}
	
	public void setPixels(Collection<Pixel> pixels){
		this.pixels = pixels;
	}
	
	public void addPixel (Pixel pixel){
		this.pixels.add(pixel);
	}
	
	private void initClusters(){
		if(kMeans.readingType == kMeansReading.COLOR){
			float scale = (float)10/this.K;
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
		/*Random random = new Random();
		for(int i =0;i<this.K;i++){			
			float colorValue = (float) random.nextDouble();
			Cluster c = new Cluster(colorValue);
			clusters.add(c);
		}*/
/*	for(Cluster cluster: clusters){
			System.out.println(cluster.getCentroidColorValue());
			Pixel[] pixels = cluster.getPixels();
			for(Pixel pixel: pixels){				
				System.out.println(pixel.getRow()+" "+pixel.getCol());
			}
		}*/
	}	
	
	public static Pixel getRandomCentroid(Collection<Pixel> pixels) {
		int maxRow= -1; 
		int maxCol = -1;
		for(Pixel pixel: pixels){
			if(pixel.getRow()>maxRow)maxRow = pixel.getRow();
			if(pixel.getCol()>maxCol)maxCol = pixel.getCol();
		}
		Random r = new Random();
		int randomX = (int) (r.nextFloat() * maxRow);
		int randomY = (int) (r.nextFloat() * maxCol);
		
		return new Pixel(randomX, randomY);
	}
	
	private void assignPointToClusters(){
		for(Pixel pixel: pixels){
			int assignmentClusterIndex = -1;
			float meansDiff = -1;
			for(Cluster c: clusters){
				if(meansDiff == -1){
					if(this.readingType == kMeansReading.COLOR){
						meansDiff = Math.abs(pixel.getColorValue()-c.getCentroid().getColorValue());
					}
					else{
						meansDiff =(float) (Math.pow(pixel.getRow()-c.getCentroid().getRow(), 2)+ Math.pow(pixel.getCol()-c.getCentroid().getCol(), 2));
						//System.out.println("Means diff "+meansDiff);
					}
					assignmentClusterIndex = clusters.indexOf(c);
				}
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
	
	private void update(){
		boolean finished = true;
		do{
			final List<Pixel> lastCentroids = getCentroids();
			/*System.out.println("last Centroid");
			for(Pixel p: lastCentroids){
				System.out.println(p.getRow()+" "+p.getCol());
			}*/
			recalculateCentroids();
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
	
	private List<Pixel> getCentroids(){
		List<Pixel> centroids = new ArrayList<Pixel>();
		for(Cluster c: clusters){
			Pixel centroidPixel = c.getCentroid();
			centroids.add(new Pixel(centroidPixel.getRow(),centroidPixel.getCol(),centroidPixel.getColorValue()));
		}
		return centroids;
	}
	
	private void recalculateCentroids(){
		//System.out.println("Recalculating");
		for(Cluster c: clusters){
			float colorSum= 0;
			int rowSum = 0;
			int colSum = 0;
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
				c.setCentroid(new Pixel(rowSum/clusterPixels.length,colSum/clusterPixels.length,(float)colorSum/clusterPixels.length));
			}
			else{
				c.setCentroid(new Pixel(-1,-1,(float) -1));
			}
			
			
			/*	System.out.println("Centyroid: "+ c.getCentroid().getRow()+" "+c.getCentroid().getCol()+" "+ c.getCentroid().getColorValue());
				System.out.println("No of pixels: "+ c.getPixels().length);
				System.out.println();*/
			
		}

		
	}
	
	public Cluster[] getDeductions(){
		Cluster[] returnClusters = new Cluster[clusters.size()];
		clusters.toArray(returnClusters);
		return returnClusters;
	}

}
