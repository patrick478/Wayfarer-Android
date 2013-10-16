package com.SteelAmbition.Wayfarer.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<String> dismissals = new HashSet<String>();
	
	public InformCard(String shortDesc,String longDesc){
		shortDescription = shortDesc;
		longDescription = longDesc;
	}
	
	public void dismissForUser(String name){
		dismissals.add(name);
	}
	
	public boolean isDismissedFor(String name){
		return dismissals.contains(name);
	}
	
	public Set<String> getDismissals(){
		return dismissals;
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
