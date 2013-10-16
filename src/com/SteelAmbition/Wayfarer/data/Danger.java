package com.SteelAmbition.Wayfarer.data;

/**
 * Represents a danger to the suicidal person. Can be sorted by relevancy.
 * @author Jeff
 *
 */
public class Danger implements Comparable<Danger>{
	
	private String name;
	private String description;
	private double level;
	private Question relevantQuestion;
	
	public Danger(String name, String desc, double level, Question q){
		this.name = name;
		this.level = level;
		relevantQuestion = q;
		description = desc;
	}

	public String getName() {
		return name;
	}
	
	public double getLevel() {
		return level;
	}
	
	public void setLevel(double d){
		level = d;
	}
	
	public Question getRelevantQuestion(){
		return relevantQuestion;
	}

	@Override
	public int compareTo(Danger o) {
		if(level == o.level) return 0;
		return level < o.level ? 1 : -1;
	}
}
