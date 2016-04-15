package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class kMeans {
	private int K;
	private int NUM_PIXELS;
	private Collection<Cluster> clusters;
	private Collection<Pixel> pixels;
	
	public kMeans(int k, int maxPixels){
		this.K = k;
		this.NUM_PIXELS = maxPixels;
		this.clusters = new ArrayList<Cluster>();
		initClusters(24,24);
		assignPointToClusters();
		update();
	}
	
	public void setPixels(Collection<Pixel> pixels){
		this.pixels = pixels;
	}
	
	private void initClusters(int maxRow, int maxCol){
		Random random = new Random();
		for(int i =0;i<this.K;i++){			
			float colorValue = (float) random.nextDouble();
			Cluster c = new Cluster(colorValue);
			clusters.add(c);
		}
	}	
	
	private void assignPointToClusters(){
		for(Pixel pixel: pixels){
			Cluster assignmentCluster = null;
			float colorDiff = -1;
			for(Cluster c: clusters){
				if(assignmentCluster== null){
					assignmentCluster = c;
				}
				else{
					float newColorDiff = Math.abs(pixel.getColorValue()-c.getCentroidColorValue());
					if(newColorDiff<colorDiff){
						assignmentCluster = c;
						colorDiff = newColorDiff;
						c.addPixel(pixel);
					}
				}
			}
		}
	}
	
	private void update(){
		boolean finished = true;
		do{
			Cluster[] lastClusters = (Cluster[]) clusters.toArray();
			recalculateCentroids();
			int index =0;
			for(Cluster c: clusters){
				//checking if the last centroid value for all the clusters is the same.
				//if yes we are finished
				if(c.getCentroidColorValue() != lastClusters[index].getCentroidColorValue()){
					finished = false;
					c.clearPixels();
				}
			}
			if(!finished) assignPointToClusters();
		}while(!finished);			
	}
	
	private void recalculateCentroids(){
		for(Cluster c: clusters){
			float colorSum= 0;
			Pixel[] clusterPixels = c.getPixels();
			for(Pixel pixel: clusterPixels ){
				colorSum += pixel.getColorValue();
			}
			c.setCentroidColorValue((float)colorSum/clusterPixels.length);
		}
		
	}
	
	public Cluster[] getDeductions(){
		return (Cluster[]) clusters.toArray();
	}

}
