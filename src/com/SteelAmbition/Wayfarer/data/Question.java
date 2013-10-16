package com.SteelAmbition.Wayfarer.data;

import java.util.*;

/**
 * A question that can be asked to help sort informations and dangers.
 * Stores the answer on the question.
 * 
 * @author Jeff
 *
 */
public class Question {
	
	private String question;
	private List<String> answers;
	private int selectedAnswer = -1;

	private List<Map<String,Double>> weights; // Index -> Weight for informCard's affected
	private List<Map<String,Double>> dangerWeights; // Index -> Weight for informCard's affected

	private List<Map<String,Boolean>> outcomes; //Goals affected -> new goal state (completed or not)
	private List<Map<String,Boolean>> removals; //Goals removed -> new goal state (remove or not)
	
	public Question(String question,List<String> answers){
		this.question = question;
		this.answers = answers;
		weights = new ArrayList<Map<String,Double>>();
		dangerWeights = new ArrayList<Map<String,Double>>();
		outcomes = new ArrayList<Map<String,Boolean>>();
		removals = new ArrayList<Map<String,Boolean>>();
		for(int i=0;i<answers.size();i++){
			outcomes.add(new HashMap<String,Boolean>());
			removals.add(new HashMap<String,Boolean>());
			weights.add(new HashMap<String,Double>());
			dangerWeights.add(new HashMap<String,Double>());
		}
	}
	
	public void answer(int ans){
		if(ans>=answers.size()) throw new IndexOutOfBoundsException();
		selectedAnswer = ans;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public List<String> getAnswers(){
		return answers;
	}
	
	public int getAnswer(){
		return selectedAnswer;
	}
	
	public void addDangerWeight(String ans, String danger,double weight){
		for(int i=0;i<answers.size();i++){
			if(answers.get(i).equals(ans)) dangerWeights.get(i).put(danger, weight);
		}
	}
	
	public void addCardWeight(String ans, String card,double weight){
		for(int i=0;i<answers.size();i++){
			if(answers.get(i).equals(ans)) weights.get(i).put(card, weight);
		}
	}
	
	public void addOutcome(String ans, String node,boolean completion){
		for(int i=0;i<answers.size();i++){
			if(answers.get(i).equals(ans)) outcomes.get(i).put(node, completion);
		}
	}
	
	public void addRemoval(String ans, String node,boolean removed){
		for(int i=0;i<answers.size();i++){
			if(answers.get(i).equals(ans)) removals.get(i).put(node, removed);
		}
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("Q) "+question+"\n\n");
		for(int i=0;i<answers.size();i++){
			b.append(i+": "+answers.get(i)+"\n");
		}
		return b.toString();
	}
	
	public boolean apply(StateManager s){
		List<Goal> toRemove = new ArrayList<Goal>();
		for(Danger d:s.getDangers()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(d.getName())){
					d.setLevel(dangerWeights.get(i).get(d.getName()));
				}
			}
		}
		for(InformCard c:s.getInformCards()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(c.getShortDescription())){
					c.addWeight(dangerWeights.get(i).get(c.getShortDescription()));
				}
			}
		}
		for(Goal g:s.getPreventionGoals()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(g.getName())){
					g.complete(outcomes.get(i).get(g.getName()),s.getCurrentUser());
				}
				if(i == selectedAnswer && removals.get(i).containsKey(g.getName()) && removals.get(i).get(g.getName())){
					toRemove.add(g);
				}
			}
		}
		for(Goal g:s.getLongTermGoals()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(g.getName())){
					g.complete(outcomes.get(i).get(g.getName()),s.getCurrentUser());
				}
				if(i == selectedAnswer && removals.get(i).containsKey(g.getName()) && removals.get(i).get(g.getName())){
					toRemove.add(g);
				}
			}
		}
		for(Goal g:s.getRegularGoals()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(g.getName())){
					g.complete(outcomes.get(i).get(g.getName()),s.getCurrentUser());
				}
				if(i == selectedAnswer && removals.get(i).containsKey(g.getName()) && removals.get(i).get(g.getName())){
					toRemove.add(g);
				}
			}
		}
		s.getPreventionGoals().removeAll(toRemove);
		s.getLongTermGoals().removeAll(toRemove);
		s.getRegularGoals().removeAll(toRemove);
		return true;
	}
}
