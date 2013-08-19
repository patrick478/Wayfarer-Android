package com.SteelAmbition.Wayfarer.data;

import java.util.*;

/**
 * A question that can be asked to help sort information and dangers.
 * Stores the answer on the question.
 * 
 * @author Jeff
 *
 */
public class Question {
	
	private String question;
	private List<String> answers;
	private int selectedAnswer;
	
	private Map<Integer,Double> weights; // Index -> Weight for informCard's affected
	
	public Question(String question,List<String> answers){
		this.question = question;
		this.answers = answers;
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
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("Q) "+question+"\n\n");
		for(int i=0;i<answers.size();i++){
			b.append(i+": "+answers.get(i)+"\n");
		}
		return b.toString();
	}
}
