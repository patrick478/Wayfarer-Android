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
	
	private String state = "Preventative";
	
	private List<PreventionNode> preventionNodes = new ArrayList<PreventionNode>();
	private int preventionNodeIndex = 0;
	private List<RecoveryNode> recoveryNodes = new ArrayList<RecoveryNode>();
	private int recoveryNodeIndex = 0;
	
	private List<Goal> goalPool = new ArrayList<Goal>();
	
	private List<Danger> dangers = new ArrayList<Danger>();
	private List<InformCard> informCards = new ArrayList<InformCard>();
	
	private Survey survey;
	private Date lastSurvey;
	
	public StateManager(Loader l){
		dangers = l.loadDangers();
		Collections.sort(dangers);
		preventionNodes = l.loadPreventionNodes();
		recoveryNodes = l.loadRecoveryNodes();
		survey = l.loadSurvey();
		informCards = l.loadInformCards();
		lastSurvey = l.getLastSurvey();
	}
	
	public StateManager(Database db, Survey s){
		for(Goal g:db.getPreventativeGoals()){
			preventionNodes.add(new PreventionNode(Arrays.asList(new Goal[]{g})));
		}
		for(Goal g:db.getRecoveryGoals()){
			recoveryNodes.add(new RecoveryNode(Arrays.asList(new Goal[]{g}),Arrays.asList(new Goal[0])));
		}
		for(Goal g:db.getRegularGoals()){
			goalPool.add(g);
		}
		for(Danger d:db.getDangers()){
			dangers.add(d);
		}
		for(InformCard i:db.getInformCards()){
			informCards.add(i);
		}
		s.apply(this);
	}
	
	public void goPreventative(){
		state = "Preventative";
		preventionNodeIndex = 0;
	}
	
	public void goRecovery(){
		state = "Recovery";
	}
	
	public List<InformCard> getMostRelevantInfo(int num){
		survey.sortInfo(informCards);
		return informCards;
	}
	
	public List<InformCard> getInformCards(){
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
	
	public void applySurvey(Survey s) {
		s.apply(this);
		updateState();
	}
	
	public void setPreventionNodes(List<PreventionNode> ns){
		preventionNodeIndex = 0;
		preventionNodes = ns;
	}
	
	private void updateState(){
		if(state.equals("Preventative") && preventionNodes.get(preventionNodeIndex).isComplete()){
			preventionNodeIndex++;
			if(preventionNodeIndex == preventionNodes.size()) state = "Recovery";
		}
	}
	
	/*public void fixGSON(){
		if(preventionNodes == null) preventionNodes = new ArrayList<PreventionNode>();
		if(recoveryNodes == null) recoveryNodes = new ArrayList<RecoveryNode>();
		if(goalPool == null) goalPool = new ArrayList<Goal>();
		if(informCards == null) informCards = new ArrayList<InformCard>();
		if(dangers == null) dangers = new ArrayList<Danger>();
	}*/
	
	public Survey getSurvey() {
		//if(new Date().getTime() - lastSurvey.getTime() < 1000*60*60*24)	return null;
		List<Question> qs = new ArrayList<Question>();
		for(Goal g:preventionNodes.get(preventionNodeIndex).getGoals()){
			qs.add(new Question(g.getRelatedQuestion(),Arrays.asList(new String[]{"Yes", "No"})));
		}
		Collections.sort(dangers);
		for(int i=0;i<3 && i<dangers.size();i++){
			qs.add(dangers.get(i).getRelevantQuestion());
		}
		survey = new Survey(qs);
		lastSurvey = new Date();
		return survey;
	}
}
