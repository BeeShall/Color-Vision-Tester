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
	private Collection<Pixel> boardPixels;
	private Collection<Cluster> deducedClustersforColor;
	private Collection<Cluster> deducedClustersforDistance;


	// private kMeans clustering;

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
		boardPixels = new ArrayList<Pixel>(); 
		deducedClustersforColor = new ArrayList<Cluster>();
		deducedClustersforDistance = new ArrayList<Cluster>();
		getBoard();

		System.out.println("---------------------------");
		System.out.println("colorLimits");
		System.out.println("---------------------------");
		
		Collection<ClusterLimits> colorLimits = invokeClusteringOnColor(9);
		System.out.println("Number of clusters " + colorLimits.size());
		for (ClusterLimits c : colorLimits) {			
			System.out.println(c.getColorVlaue());
			System.out.println("Top Left: " + (int) c.getTopLeft().getRow() + " " + (int) c.getTopLeft().getCol());
			System.out.println(
					"Bottom Right: " + (int) c.getBottomRight().getRow() + " " + (int) c.getBottomRight().getCol());
		}
		

		System.out.println("---------------------------");
		System.out.println("distanceLimits");
		System.out.println("---------------------------");
		Collection<ClusterLimits> distanceLimits = invokeClusteringOnDistance(2);
		System.out.println("Number of clusters " + distanceLimits.size());
		for (ClusterLimits c : distanceLimits) {			
			System.out.println(c.getColorVlaue());
			System.out.println("Top Left: " + (int) c.getTopLeft().getRow() + " " + (int) c.getTopLeft().getCol());
			System.out.println(
					"Bottom Right: " + (int) c.getBottomRight().getRow() + " " + (int) c.getBottomRight().getCol());
		}

		
		System.out.println("---------------------------");
		System.out.println("Patterns");
		System.out.println("---------------------------");
		Collection<FullPattern> fullPatterns = getPatterns();
		for (FullPattern f : fullPatterns) {
			System.out.print(f.colorValue + " ");
			System.out.println(f.letter);
		}

	}

	public void addPixelToTable(int x, int y, float colorValue) {
		this.boardPixels.add(new Pixel(x, y, colorValue));
	}

	public Collection<ClusterLimits> invokeClusteringOnColor(int k) {		
		return performKMeansClustering(k, new Cluster(boardPixels), kMeansReading.COLOR);
	}
	public Collection<ClusterLimits> invokeClusteringOnDistance(int k){
		Collection<ClusterLimits> limits = new ArrayList<ClusterLimits>();
		for(Cluster c: deducedClustersforColor){
			limits.addAll(performKMeansClustering(k, c, kMeansReading.DISTANCE));
		}
		return limits;
	}

	private Collection<ClusterLimits> performKMeansClustering(int k, Cluster cluster, kMeansReading readingType) {
		Collection<ClusterLimits> colorLimits = new ArrayList<ClusterLimits>();
		Collection<ClusterLimits> distanceLimits = new ArrayList<ClusterLimits>();
		kMeans clustering = new kMeans(k, readingType);
		clustering.setPixels(Arrays.asList(cluster.getPixels()));
		clustering.init();
		Cluster[] deductions = clustering.getDeductions();
		for (Cluster c : deductions) {
			
			if (readingType == kMeansReading.COLOR) {
				if (c.getCentroid().getColorValue() >= 0) {
					ClusterLimits limit = new ClusterLimits(c.getTopLeftPixel(), c.getBottomRightPixel());
					limit.setColorVlaue(c.getCentroid().getColorValue());
					colorLimits.add(limit);
					deducedClustersforColor.add(c);
				}				
			} else {
				c.getCentroid().setColorValue(cluster.getCentroid().getColorValue());
				ClusterLimits limit =  new ClusterLimits(c.getTopLeftPixel(), c.getBottomRightPixel());
				limit.setColorVlaue(cluster.getCentroid().getColorValue());
				distanceLimits.add(limit);
				deducedClustersforDistance.add(c);
			}
		}
		if (readingType == kMeansReading.COLOR) {
		
			return colorLimits;
		}
		else{			
			return distanceLimits;
		}
	}

	public static void main(String[] args) {
		Dictionary dict = new Dictionary();
	}

	public Collection<FullPattern> getPatterns() {
		Collection<FullPattern> fullPatterns = new ArrayList<FullPattern>();
		for (Cluster cluster : deducedClustersforDistance) {
			if (cluster.getPixels().length > 0) {
				Letter deducedLetter = convertClusterToLetters(cluster);

				for (int i = 0; i <= deducedLetter.getDimRow(); i++) {
					for (int j = 0; j <= deducedLetter.getDimCol(); j++) {
						if (cluster.getCentroid().getColorValue() == 6.0) {
						}
						this.frontSearch(i, j, deducedLetter.isPixelSetAt(i, j));
					}
				}
				if (this.isFound()) {
					fullPatterns.add(new FullPattern(cluster.getCentroid().getColorValue(), this.getFullPattern()));
				}
				fSearcher.reset(rules);
			}
		}
		return fullPatterns;
	}

	private Letter convertClusterToLetters(Cluster deduction) {
		Letter letter = new Letter();
		trimPixels(deduction);
		for(Pixel p: deduction.getPixels()){
			letter.setPixelAt((int) p.getRow(), (int) p.getCol());
		}
		return letter;
	}


	private void trimPixels(Cluster cluster) {
		Pixel topLeft = cluster.getTopLeftPixel();
		for(Pixel pixel: cluster.getPixels()){
			pixel.adjustRow(-1 * topLeft.getRow());
			pixel.adjustCol(-1 * topLeft.getCol());
		}
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
							if (matcher.find()) {
								if (matcher.group().length() != 0) {
									float colorValue = (float) (Double.parseDouble(matcher.group().trim()));
									this.boardPixels.add(new Pixel(i, j, colorValue));
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
	
	public void reset(){
		resetForwardSearch();
		deducedClustersforColor.clear();
		deducedClustersforDistance.clear();
	}

}
