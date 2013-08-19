package com.SteelAmbition.Wayfarer.data;

import java.util.List;

/**
 * A node of the state machine in the recovery state
 * 
 * @author Jeff
 *
 */
public class RecoveryNode {

	private List<Goal> longTermGoals;
	private List<Goal> regularGoals;
	
	public RecoveryNode(List<Goal> longTermGoals, List<Goal> regularGoals){
		this.longTermGoals = longTermGoals;
		this.regularGoals = regularGoals;
	}
	
	public List<Goal> getLongTermGoals(){
		return longTermGoals;
	}

	public List<Goal> getRegularGoals(){
		return regularGoals;
	}
	
}
