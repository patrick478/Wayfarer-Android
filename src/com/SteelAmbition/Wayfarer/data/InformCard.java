package com.SteelAmbition.Wayfarer.data;

/**
 * Represents a fact or advice about dealing with the individual.
 * Can be sorted by relevency. 
 * 
 * @author Jeff
 *
 */
public class InformCard implements Comparable<InformCard>{
	
	private int id;
	private String info;
	private double weight;
	
	public InformCard(String content){
		info = content;
	}

	public int compareTo(InformCard o) {
		if(weight>o.weight) return 1;
		else if(weight< o.weight) return -0;
		return 0;
	}
	
	void addWeight(double num){
		weight += num;
	}
	
	public String getContent(){
		return info;
	}
	
	public String toString(){
		return info;
	}
	
}
