package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

/**
 * @author Bishal Class that hold the dictionary for the Searches
 */

public class Dictionary {
	private ForwardSearcher fSearcher = null;
	public static Collection<Letter> rules;
	private static Collection<String> strings;
	private  Collection<Pixel> boardPixels;
	private Collection<ClusterLimits> colorLimits;
	private Collection<ClusterLimits> distanceLimits;
	private Collection<FullPattern> fullPatterns;
	
	private kMeans clustering;

	/**
	 * Constructor for the class
	 */
	public Dictionary() {
		// List of rules
		rules = new ArrayList<Letter>();
		// List of name of the rules
		strings = new ArrayList<String>();
		InputStream in;
		// fetching the serialized file with the rules
		try {
			in = this.getClass().getResource("rules.txt").openStream();
			importLetters(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(Letter l: rules){
			if(l.getName().equals("BREAK")){
				for(String p: l.pixels){
					System.out.println(p);
				}
			}
		}*/
		boardPixels = new ArrayList<Pixel>();
		colorLimits = new ArrayList<ClusterLimits>();
		distanceLimits = new ArrayList<ClusterLimits>();
		fullPatterns = new ArrayList<FullPattern>();
		getBoard();
		

		performKMeansClustering(9,boardPixels, kMeansReading.COLOR);
		System.out.println("colorLimits");
		
		for(ClusterLimits c: colorLimits){
			System.out.println(c.getColorVlaue());
			System.out.println("Top Left: "+c.getTopLeft().getRow()+" "+c.getTopLeft().getCol());
			System.out.println("Bottom Right: "+c.getBottomRight().getRow()+" "+c.getBottomRight().getCol());
		}
		
	System.out.println("distanceLimits");
		
		for(ClusterLimits c: distanceLimits){
			System.out.println(c.getColorVlaue());
			System.out.println("Top Left: "+c.getTopLeft().getRow()+" "+c.getTopLeft().getCol());
			System.out.println("Bottom Right: "+c.getBottomRight().getRow()+" "+c.getBottomRight().getCol());
		}
	System.out.println("Patterns");
		
		for(FullPattern f: fullPatterns){
			System.out.println(f.colorValue);
			System.out.println(f.letter);
		}
		

	}
	
	public void addPixelToTable(int x, int y, float colorValue){
		this.boardPixels.add(new Pixel(x,y, colorValue));
	}
	
	public void invokeClustering(int k){
		performKMeansClustering(k,boardPixels, kMeansReading.COLOR);
	}
	
	
	public void performKMeansClustering(int k, Collection<Pixel> pixels, kMeansReading readingType){	
		clustering = new kMeans(k,readingType);
		clustering.setPixels(pixels);
		clustering.init();
		Cluster[] deductions = clustering.getDeductions();
		for(Cluster cluster: deductions){
			ClusterLimits limit = new ClusterLimits(cluster.getTopLeftPixel(),cluster.getBottomRightPixel());
			limit.setColorVlaue(cluster.getCentroid().getColorValue());
			colorLimits.add(limit);
			/*System.out.println(cluster.getCentroid().getColorValue());
			System.out.println("Top Left: "+cluster.getTopLeftPixel().getRow()+" "+cluster.getTopLeftPixel().getCol());
			System.out.println("Bottom Right: "+cluster.getBottomRightPixel().getRow()+" "+cluster.getBottomRightPixel().getCol());*/
		}
		//System.out.println();
		for(Cluster cluster: deductions){
			//System.out.println(cluster.getCentroid().getColorValue());
			//System.out.println("Deduction");
			if(cluster.getPixels().length>0){
				Letter[] deducedLetters = convertClusterToLetter(cluster);
				for(int l =0;l<2;l++){
					//System.out.println("Letter");
					for(int i=0; i<=deducedLetters[l].getDimRow();i++){
						for(int j=0;j<=deducedLetters[l].getDimCol();j++){
							if(cluster.getCentroid().getColorValue()==6.0){
							//System.out.println("row "+i+" col "+j+" "+deducedLetters[l].isPixelSetAt(i, j));
							}

							this.frontSearch(i, j, deducedLetters[l].isPixelSetAt(i, j));
						}
					}
					if(this.isFound()){
						fullPatterns.add(new FullPattern(cluster.getCentroid().getColorValue(),this.getFullPattern()));
						//System.out.println("Full pattern "+this.getFullPattern());
					}					
					fSearcher.reset(rules);
				}	
			}
		}
	}
	
		
	public static void main(String[] args){
		Dictionary dict = new Dictionary();		
	}
	
	private Letter[] convertClusterToLetter(Cluster cluster){
		//System.out.println("Clustering for color Value: "+cluster.getCentroid().getColorValue());
		int clusterTopLeftRow = cluster.getTopLeftPixel().getRow();
		int clusterTopLeftCol = cluster.getTopLeftPixel().getCol();
		Pixel[] pixels = trimPixels(cluster);
		kMeans tempClustering = new kMeans(2,kMeansReading.DISTANCE);
		tempClustering.setPixels(new ArrayList<Pixel>(Arrays.asList(pixels)));
		tempClustering.init();
		Cluster[] deductions = tempClustering.getDeductions();
		//System.out.println(clusterTopLeftRow+" "+clusterTopLeftCol);
		for(Cluster c: deductions){		
			ClusterLimits limit = new ClusterLimits(new Pixel(c.getTopLeftPixel().getRow()+clusterTopLeftRow,c.getTopLeftPixel().getCol()+clusterTopLeftCol)
					,new Pixel(c.getBottomRightPixel().getRow()+clusterTopLeftRow,c.getBottomRightPixel().getCol()+clusterTopLeftCol ));
			limit.setColorVlaue(cluster.getCentroid().getColorValue());
			distanceLimits.add(limit);
	/*		System.out.println("Top Left: "+(c.getTopLeftPixel().getRow()+clusterTopLeftRow)+" "+(c.getTopLeftPixel().getCol()+clusterTopLeftCol));
			System.out.println("Bottom Right: "+(c.getBottomRightPixel().getRow()+clusterTopLeftRow)+" "+(c.getBottomRightPixel().getCol()+clusterTopLeftCol));*/
		}
		//check the deductions
		Letter[] letters = new Letter[2];	
		for(int i=0;i<2;i++){
			letters[i] = new Letter();
			//System.out.println("Cluster");
			Pixel[] deductionPixels = trimPixels(deductions[i]);
			//System.out.println("Converting letters");
			for(Pixel pixel: deductionPixels){
				if(cluster.getCentroid().getColorValue()==7.0){
					//System.out.println("Row "+pixel.getRow()+" Col: "+pixel.getCol());
				}
				letters[i].setPixelAt(pixel.getRow(), pixel.getCol());
			}	
		}
		return letters;
	}
	
	private Pixel[] trimPixels(Cluster cluster){
		Pixel[] pixels = cluster.getPixels();
		Pixel topLeft = cluster.getTopLeftPixel();
		for(Pixel pixel: pixels){
			//System.out.println("Row "+pixel.getRow()+" Col: "+pixel.getCol());
			pixel.adjustRow(-1*topLeft.getRow());
			pixel.adjustCol(-1*topLeft.getCol());			
		}		
		return pixels;
	}

	private void getBoard() {
		boardPixels = new ArrayList<Pixel>();
		try {
			FileReader fr = new FileReader("test");
			BufferedReader reader = new BufferedReader(fr);
			String line;
			try {
				for (int i = 0; i < 24; i++) {
					if ((line = reader.readLine()) != null) {
						   Pattern pattern = Pattern.compile("\\d+");
					       Matcher matcher = pattern.matcher(line);						
						for (int j = 0; j < 24; j++) {
							if(matcher.find()){
								if(matcher.group().length() != 0){
									float colorValue = (float) (Double.parseDouble(matcher.group().trim()));
										this.boardPixels.add(new Pixel(i,j, colorValue));
								}							
							}
						}
					}
				}				
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// After doing k-means clustering, make a function to trim the board for
	// each cluster and then use forward search to find the letter

	/**
	 * Method to import the rules from the serialized file
	 * 
	 * @param is
	 *            Inputstream for the file
	 */
	private void importLetters(InputStream is) {
		syntaxReader reader = new syntaxReader(is);
		rules = reader.getRules();
		strings = reader.getStrings();
	}

	/**
	 * Method to get the name of the existing rules
	 * 
	 * @return Collection of the name of the rules
	 */
	public Collection<String> getStringsinRules() {
		return strings;
	}

	/**
	 * Method to check if fullPattern is found or not from forward Search
	 * 
	 * @return boolean value based on whether fullPattern is found or not
	 */
	public boolean isFound() {
		return fSearcher.isFound();
	}

	/**
	 * Method to get the full pattern from forward search
	 * 
	 * @return The full pattern detected after the forward search
	 */
	public String getFullPattern() {
		return fSearcher.getFullPattern();
	}

	/**
	 * Method to get the names of the remaining candidates from forward search
	 * 
	 * @return String array of the names of remaining candidates
	 */
	public String[] getRecognizedCandidatesFromForwardSearch() {
		return fSearcher.getRemainingCandidates();
	}

	/**
	 * Method to conduct the forward search based on whether the pixel is set on
	 * or off on a given pixel
	 * 
	 * @param x:
	 *            x coordinate of pixel
	 * @param y:
	 *            y coordinate of pixel
	 * @param pixelSet:
	 *            boolean value denoting whether the pixel is set or not
	 * @return String array of the candidates passing the current search
	 */
	public String[] frontSearch(int x, int y, boolean pixelSet) {
		if (fSearcher == null) {
			// if forward search is not initialized
			fSearcher = new ForwardSearcher(rules);
		}
		return fSearcher.forwardSearch(x, y, pixelSet);
	}

	

	/**
	 * Method to reset the forward Search
	 */
	public void resetForwardSearch() {
		fSearcher.reset(rules);
	}

}
