package com.SteelAmbition.Wayfarer.data;

/**
 * Represents a danger to the suicidal person. Can be sorted by relevancy.
 * @author Jeff
 *
 */
public class Danger implements Comparable<Danger>{
	
	private String name;
	private double level;
	
	public Danger(String name, double level){
		this.name = name;
		this.level = level;
	}

	public String getName() {
		return name;
	}
	
	public double getLevel() {
		return level;
	}

	@Override
	public int compareTo(Danger o) {
		if(level == o.level) return 0;
		return level < o.level ? 1 : -1;
	}
}
