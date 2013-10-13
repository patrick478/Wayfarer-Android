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
	
	private Map<Integer,Double> weights; // Index -> Weight for informCard's affected
	
	private List<Map<String,Boolean>> outcomes; //Goals affected -> new goal state (completed or not)
	
	public Question(String question,List<String> answers){
		this.question = question;
		this.answers = answers;
		weights = new HashMap<Integer,Double>();
		outcomes = new ArrayList<Map<String,Boolean>>();
		for(int i=0;i<answers.size();i++){
			outcomes.add(new HashMap<String,Boolean>());
		}
	}
	
	public void answer(int ans){
		if(ans>=answers.size() || ans < 0) throw new IndexOutOfBoundsException();
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
	
	public void addOutcome(String ans, String node,boolean completion){
		for(int i=0;i<answers.size();i++){
			if(answers.get(i).equals(ans)) outcomes.get(i).put(node, completion);
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
		for(Goal g:s.getPreventionGoals()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(g.getName())){
					g.complete(outcomes.get(i).get(g.getName()));
				}
			}
		}
		for(Goal g:s.getLongTermGoals()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(g.getName())){
					g.complete(outcomes.get(i).get(g.getName()));
				}
			}
		}
		for(Goal g:s.getRegularGoals()){
			for(int i=0;i<outcomes.size();i++){
				if(i == selectedAnswer && outcomes.get(i).containsKey(g.getName())){
					g.complete(outcomes.get(i).get(g.getName()));
				}
			}
		}
		return true;
	}
}
