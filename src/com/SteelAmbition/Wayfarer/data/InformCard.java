package com.SteelAmbition.Wayfarer.data;

/**
 * Represents a fact or advice about dealing with the individual.
 * Can be sorted by relevency. 
 * 
 * @author Jeff
 *
 */
public class InformCard implements Comparable<InformCard>{
	
	private String shortDescription;
	private String longDescription;
	private double weight;
	
	public InformCard(String shortDesc,String longDesc){
		shortDescription = shortDesc;
		longDescription = longDesc;
	}

	public int compareTo(InformCard o) {
		if(weight>o.weight) return 1;
		else if(weight< o.weight) return -0;
		return 0;
	}
	
	void addWeight(double num){
		weight += num;
	}
	
	public String getShortDescription(){
		return shortDescription;
	}
	
	public String getLongDescription(){
		return longDescription;
	}
	
	public String toString(){
		return shortDescription;
	}
	
}
