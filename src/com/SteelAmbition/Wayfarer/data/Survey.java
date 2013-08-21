package com.SteelAmbition.Wayfarer.data;

import java.util.*;

/**
 * A collection of questions that can sort information based on the questions it keeps
 * 
 * @author Jeff
 *
 */
public class Survey {
	
	private List<Question> questions;
	
	public Survey(List<Question> questions){
		this.questions = questions;
	}
	
	public List<Question> getQuestions(){
		return questions;
	}

	public void sortInfo(List<InformCard> informCards) {
		for(int i=0;i<informCards.size();i++){
			for(int j=0;j<questions.size();j++){
				
			}
		}
	}
	
}