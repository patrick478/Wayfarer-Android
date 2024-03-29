package com.SteelAmbition.Wayfarer.data;

import java.util.*;

/**
 * A node of the state machine in the preventative state
 * 
 * @author Jeff
 *
 */
public class PreventionNode {

	private List<Goal> goals;
	
	public PreventionNode(List<Goal> goals){
		this.goals = new ArrayList<Goal>(goals);
	}
	
	public List<Goal> getGoals(){
		return goals;
	}
	
	public List<String> getInformation(){
		return null;
	}
	
	public boolean isComplete(){
		for(Goal g:goals){
			if(!g.isComplete()){
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("{");
		for(Goal g : goals) b.append(g+",");
		b.delete(b.length()-1,b.length());
		b.append("}");
		return b.toString();
	}
}
