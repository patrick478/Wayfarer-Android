package com.SteelAmbition.Wayfarer.data;

import java.util.*;

import com.SteelAmbition.Wayfarer.loader.*;


/**
 * The class that manages everything inside the state machine
 * 
 * @author Jeff
 *
 */
public class StateManager implements StateAccess{
	
	private boolean preventative = true;
	
	private List<PreventionNode> preventionNodes = new ArrayList<PreventionNode>();
	private int preventionNodeIndex = 0;
	private List<RecoveryNode> recoveryNodes = new ArrayList<RecoveryNode>();
	private int recoveryNodeIndex = 0;
	
	private List<Danger> dangers = new ArrayList<Danger>();
	private List<InformCard> informCards;
	
	private Survey survey;
	
	private String state = "Preventative";
	
	public StateManager(Loader l){
		dangers = l.loadDangers();
		Collections.sort(dangers);
		preventionNodes = l.loadPreventionNodes();
		recoveryNodes = l.loadRecoveryNodes();
		survey = l.loadSurvey();
		informCards = l.loadInformCards();
	}
	
	public void goPreventative(){
		preventative = true;
	}
	
	public void goRecovery(){
		preventative = false;
	}
	
	public List<InformCard> getMostRelevantInfo(int num){
		survey.sortInfo(informCards);
		return informCards;
	}
	
	public void printSelf(){
		StringBuilder b = new StringBuilder();
		//Dangers
		b.append("Dangers:\n");
		for(Danger d : dangers){
			b.append("   "+d.getName()+": ");
			for(int i=0;i<10;i++){
				if(i<d.getLevel()*10) b.append("*");
				else b.append("-");
			}
			b.append("\n");
		}
		//Nodes
		b.append("Complete:\n");
		for(int i=0;i<preventionNodeIndex;i++){
			b.append("   "+preventionNodes.get(i)+"\n");
		}
		b.append("Current: "+preventionNodes.get(preventionNodeIndex)+"\n");
		b.append("Next:\n");
		for(int i=preventionNodeIndex+1;i<preventionNodes.size();i++){
			b.append("   "+preventionNodes.get(i)+"\n");
		}
		b.append("Relevant Information:\n");
		for(int i=0;i<Math.min(informCards.size(),5);i++){
			b.append("   "+(i+1)+": "+informCards.get(i));
		}
		b.append("\nLong Term Goals:\n   {");
		for(int i=0;i<recoveryNodes.get(recoveryNodeIndex).getLongTermGoals().size();i++){
			b.append(recoveryNodes.get(recoveryNodeIndex).getLongTermGoals().get(i)+"}\n   {");
		}
		b.delete(b.length()-5, b.length());
		b.append("\nRegular Goals:\n   {");
		for(int i=0;i<recoveryNodes.get(recoveryNodeIndex).getRegularGoals().size();i++){
			b.append(recoveryNodes.get(recoveryNodeIndex).getRegularGoals().get(i)+"}\n   {");
		}
		b.delete(b.length()-5, b.length());
		System.out.println(b);
	}
	
	public String getState(){
		return state;
	}

	public List<Danger> getDangers() {
		return dangers;
	}

	public List<Goal> getPreventionGoals() {
		return preventionNodes.get(preventionNodeIndex).getGoals();
	}

	public List<Goal> getLongTermGoals() {
		return recoveryNodes.get(recoveryNodeIndex).getLongTermGoals();
	}

	public List<Goal> getRegularGoals() {
		return recoveryNodes.get(recoveryNodeIndex).getRegularGoals();
	}
}
