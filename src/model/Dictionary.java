package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

/**
 * @author Bishal
 * Class that hold the dictionary for the Searches
 */

public class Dictionary {
	private ForwardSearcher fSearcher = null;
	public static Collection<Letter> rules;
	private static Collection<String> strings;
	private Letter letter;
	
	/**
	 * Constructor for the class
	 */
	public Dictionary(){
		//List of rules
		rules = new ArrayList<Letter>();
		//List of name of the rules
		strings = new ArrayList<String>();
		InputStream in;
		//fetching the serialized file with the rules
		try {
			in = this.getClass().getResource("rules.txt").openStream();
			importLetters(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	
	
	//After doing k-means clustering, make a function to trim the board for each cluster and then use forward search to find the letter
	
	
	
	
	
	/**
	 * Method to import the rules from the serialized file
	 * @param is Inputstream for the file
	 */
	private void importLetters(InputStream is){
		syntaxReader reader = new syntaxReader(is);
		rules = reader.getRules();		
		strings = reader.getStrings();
	}
	
	/**
	 * Method to get the name of the existing rules
	 * @return Collection of the name of the rules
	 */
	public Collection<String> getStringsinRules(){
		return strings;
	}
	
	/**
	 * Method to check if fullPattern is found or not from forward Search
	 * @return boolean value based on whether fullPattern is found or not
	 */
	public boolean isFound(){
		return fSearcher.isFound();
	}
	
	/**
	 * Method to get the full pattern from forward search
	 * @return The full pattern detected after the forward search
	 */
	public String getFullPattern(){
		return fSearcher.getFullPattern();
	}
		
	/**
	 * Method to get the names of the remaining candidates from forward search
	 * @return String array of the names of remaining candidates
	 */
	public String[] getRecognizedCandidatesFromForwardSearch(){
		return fSearcher.getRemainingCandidates();
	}
	
	/**
	 * Method to conduct the forward search based on whether the pixel is set on or off on a given pixel
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 * @param pixelSet: boolean value denoting whether the pixel is set or not
	 * @return String array of the candidates passing the current search
	 */
	public String[] frontSearch(int x, int y, boolean pixelSet){
		if(fSearcher == null){
			//if forward search is not initialized
			fSearcher = new ForwardSearcher(rules);
		}
		return fSearcher.forwardSearch(x, y, pixelSet);
	}
	
	/**
	 * Method to fetch the rule letter associated with the provided name
	 * @param str: name of the rule to fetch for
	 * @return: Rule Letter if found, null if not
	 */
	private Letter fetchLetter(String str){
		for(Letter rule: rules){
			if(rule.getName().equals(str)){
				return rule;
			}
		}
		return null;
	}
	
	/**
	 * Method to reset the forward Search
	 */
	public void resetForwardSearch(){
		fSearcher.reset(rules);
	}
	

}
