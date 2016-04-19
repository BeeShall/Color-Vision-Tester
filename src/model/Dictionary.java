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
	private Letter letter;
	private  Collection<Pixel> boardPixels;
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
		getBoard();
		
		performKMeansClustering(boardPixels, kMeansReading.COLOR);
		

	}
	
	private void performKMeansClustering(Collection<Pixel> pixels, kMeansReading readingType){	
		clustering = new kMeans(7,readingType);
		clustering.setPixels(pixels);
		clustering.init();
		Cluster[] deductions = clustering.getDeductions();
		for(Cluster cluster: deductions){
			//System.out.println(cluster.getCentroid().getColorValue());
			if(cluster.getCentroid().getColorValue() >0){
				Letter deducedLetter = convertClusterToLetter(cluster);
				for(int i=0; i<8;i++){
					for(int j=0;j<6;j++){
						this.frontSearch(i, j, deducedLetter.isPixelSetAt(i, j));
					}
				}
				if(this.isFound()){
					System.out.println(this.getFullPattern());
				}
				fSearcher.reset(rules);
			}
		}
	}
	
		
	public static void main(String[] args){
		Dictionary dict = new Dictionary();		
	}
	
	private Letter convertClusterToLetter(Cluster cluster){
		Letter letter = new Letter();
		Pixel topLeft = cluster.getTopLeftPixel();
		Pixel[] pixels = cluster.getPixels();
		for(Pixel pixel: pixels){
			//System.out.println("Converting letters");
			pixel.adjustRow(-1*topLeft.getRow());
			pixel.adjustCol(-1*topLeft.getCol());
			if(pixel.getRow()>=8 || pixel.getCol()>=6){
				System.out.println("Clustering on distance");
				//sending wrong pixels
				//just send the ones that are grouped
				this.performKMeansClustering(new ArrayList<Pixel>(Arrays.asList(pixels)),kMeansReading.DISTANCE);
			}
			//System.out.println("Row "+pixel.getRow()+" Col: "+pixel.getCol());
			letter.setPixelAt(pixel.getRow(), pixel.getCol());
		}		
		return letter;
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
									float colorValue = (float) (Double.parseDouble(matcher.group().trim())/100);
									if(colorValue >0.0){
										this.boardPixels.add(new Pixel(i,j, colorValue));
									}
								}
								//);
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
	 * Method to fetch the rule letter associated with the provided name
	 * 
	 * @param str:
	 *            name of the rule to fetch for
	 * @return: Rule Letter if found, null if not
	 */
	private Letter fetchLetter(String str) {
		for (Letter rule : rules) {
			if (rule.getName().equals(str)) {
				return rule;
			}
		}
		return null;
	}

	/**
	 * Method to reset the forward Search
	 */
	public void resetForwardSearch() {
		fSearcher.reset(rules);
	}

}
