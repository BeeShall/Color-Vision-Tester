package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

/**
 * @author Bishal
 * Class that handles the Forward Search
 */

public class ForwardSearcher {
	private Collection<Letter> passed;
	private Collection<Letter> failed;
	private boolean found = true;
	private Letter fullPattern = null;
	
	/**
	 * Constructor for the ForwardSearcher Class
	 * @param rules: collection of rules to conduct forward search on
	 */
	public ForwardSearcher(Collection<Letter> rules){
		//list of rules that passed after each search
		passed = new ArrayList<Letter>();
		//list of rules that failed after each search
		failed = new ArrayList<Letter>();
		//full pattern detected after complete search
		fullPattern = new Letter();
		//all rules pass initially
		passed.addAll(rules);
	}
	
	/**
	 * Method to check if fullPattern is found or not
	 * @return boolean value based on whether fullPattern is found or not
	 */
	public boolean isFound(){
		return found;
	}
	
	/**
	 * Method to get the full pattern
	 * @return The full pattern detected after the forward search
	 */
	public String getFullPattern(){
		for(Letter l: passed){
			if(fullPattern.compareLetter(l)) return l.getName();
		}
		return null;
	}
	
	/**
	 * Method to get the names of the remaining candidates
	 * @return String array of the names of remaining candidates
	 */
	public String[] getRemainingCandidates(){
		String[] candidates = new String[passed.size()];
		int i=0;
		for(Letter letter: passed){
			candidates[i++]=letter.getName();
		}
		return candidates;
	}
	
	/**
	 * Method to conduct the forward search based on whether the pixel is set on or off on a given pixel
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 * @param pixelSet: boolean value denoting whether the pixel is set or not
	 * @return String array of the candidates passing the current search
	 */
	public String[] forwardSearch(int x, int y, boolean pixelSet){
			divideTree(x,y,pixelSet);
			return getRemainingCandidates();
	}
	
	/**
	 * Method to divide the decision based on whether the pixel is set on or off on a given pixel
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 * @param pixelSet: boolean value denoting whether the pixel is set or not
	 */
	public void divideTree(int x, int y, boolean pixelSet){
		if(pixelSet){
			//if the pixel is on make it the full pattern
			fullPattern.setPixelAt(x, y);
		}
		Collection<Letter> tempPassedList = passed;
		//for every single passed rules, if the current pixel is off, remove from passed and add to failed
		for (Iterator<Letter> iterator = passed.iterator(); iterator.hasNext(); ) {
			Letter letter = iterator.next();
			if(letter.isPixelSetAt(x, y)==true && pixelSet == false){
				failed.add(letter);
				iterator.remove();
			}
		}
		//if none of the passed rules pass this pixel, revert back one step and ignore this pixel
		if(passed.isEmpty()){ 
			passed = tempPassedList;
			found = false;
		}
	}
	
	/**
	 * Method to reset the forwardSearch
	 * @param rules Collection of rules to reset with
	 */
	public void reset(Collection<Letter> rules){
		passed.clear();
		failed.clear();
		passed.addAll(rules);
		found = true;
		fullPattern = null;
	}

}
