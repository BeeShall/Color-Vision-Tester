package model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * Class that handles decodes the rules from the serialized file
 */

public class syntaxReader {	
	private InputStream is;
	private Collection<Letter> rules;
	private Collection<String> strings;
	
	/**
	 * Description: Constructor for the syntaxReader class
	 * @param is InputStream to decode the rules from
	 */
	public syntaxReader(InputStream is){
		this.is = is;
		rules = new ArrayList<Letter>();
		strings = new ArrayList<String>();
		readRules();
	}
	
	/**
	 * Getter class for the rules decoded.
	 * @return Collection of rules
	 */
	public Collection<Letter> getRules(){
		return rules;
	}
	
	/**
	 * Getter Class to get the characters from the rules
	 * @return Collection of character for the rules
	 */
	public Collection<String> getStrings(){
		return strings;
	}
	
	/**
	 * Method to read the rules from the InputStream
	 */
	private void readRules(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;               
            while ((line = reader.readLine()) != null) {
            	createLetter(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }		
	}
	
	/**
	 * Method to split each rule between LHS and RHS
	 * @param data a single rule
	 */
	private void createLetter(String data){
			//Letter object to denote the rule
			Letter l = new Letter();
			//Split on -> because that determines the sides
	        String[] sides = data.split("->");
	        //RHS is the character for that rule
	        String temp = sides[1].trim();
	        l.setName(temp);
	        strings.add(temp);
	        //LHS contains the rules to get to the string which needs to parsed further
	        l = parseCoordinates(sides[0], l); 
	        rules.add(l);
	}
	
	/**
	 * Method to parse the rules in LHS into a Letter object
	 * @param text LHS part of the rule
	 * @param l Letter object to set the rules into
	 * @return Letter object to denote the rule
	 */
	private Letter parseCoordinates(String text, Letter l){		
		String[] coordinates= text.split(", ");
		for(String temp: coordinates){
			temp = temp.trim();
			//if the element in LHS doesn't begin with a parantheses, its a letter already in the rules
			if(temp.charAt(0) != '('){	
				Letter tempLetter = getExistingLetter(temp);
				//if it already exists, copy all the on pixels into the new rule.
				l.copyFrom(tempLetter);	
			}
			//if not parse it as a coordinate
			else{
				int x = Integer.parseInt(temp.substring(1, 2));
				int y = Integer.parseInt(temp.substring(3, 4));
				l.setPixelAt(x, y);
			}
		}
		return l;
	}
	
	/**
	 * Method to determine of a rule already exists.
	 * @param String name that needs to be checked
	 * @return Letter object that denotesname
	 */
	private Letter getExistingLetter(String letter){
		for(Letter temp: rules){
			//if any of the name of the letter matches with the name of this letter return 
			if(temp.getName().equals(letter)){
				return temp;
			}
		}
		return null;
	}
}
