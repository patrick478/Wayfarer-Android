package com.SteelAmbition.Wayfarer.data;

import java.util.*;

/**
 * Represents and of the three types of goal used by the state manager.
 * Emergency goals, Long term goals, and regular goals
 * 
 * @author Jeff
 *
 */
public class Goal {

	private String name;
	private String description;
	private boolean complete;
	private Date completeTime;
	private String relatedQuestion = "";
	
public Goal(String name,String description,String relatedQuestion){
		this.name = name;
		this.description = description;
		this.relatedQuestion = relatedQuestion;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String toString(){
		return name+": "+description;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Goal)) return false;
		Goal g = (Goal) o;
		return name.equals(g.name) && description.equals(g.description);
	}
	
	public void complete(boolean completed){
		complete = completed;
		if(complete) completeTime = new Date();
		else completeTime = null;
	}
	
	public boolean isComplete(){
		return complete;
	}
	
	public String getRelatedQuestion(){
		return relatedQuestion;
	}
}
