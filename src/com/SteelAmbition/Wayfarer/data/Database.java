package com.SteelAmbition.Wayfarer.data;

import java.util.*;

public class Database {
	
	private String id;
	
	private List<Danger> dangers;
	private List<InformCard> informCards;
	private List<Goal> preventativeGoals;
	private List<Goal> recoveryGoals;
	private List<Goal> regularGoals;
	
	private Survey initialSurvey;
	
	public Database(){
		dangers = new ArrayList<Danger>();
		dangers.add(new Danger("example_name","",0,new Question("example_question",Arrays.asList(new String[]{"answer_a","answer_b"}))));
		informCards = new ArrayList<InformCard>();
		informCards.add(new InformCard("example_short_description","example_long_description"));
		preventativeGoals = new ArrayList<Goal>();
		preventativeGoals.add(new Goal("preventative_goal_name","preventative_goal_description","preventative_goal_completion_question"));
		recoveryGoals = new ArrayList<Goal>();
		recoveryGoals.add(new Goal("recovery_goal_name","recovery_goal_description","recovery_goal_completion_question"));
		regularGoals = new ArrayList<Goal>();
		regularGoals.add(new Goal("regular_goal_name","regular_goal_description","regular_goal_completion_question"));
		//getInitialSurvey();
	}
	
	public Survey getInitialSurvey(){
		List<Question> questions = new ArrayList<Question>();
		questions.add(new Question("Are they on any medication?",
				Arrays.asList(new String[]{"None", "Antidepressant", "antipsychotic"})));
		questions.add(new Question("Do they have any prior troubles with suicide?",
				Arrays.asList(new String[]{"Yes (more than one)", "Yes (only one)", "No"})));
		questions.add(new Question("Is there a history of mental illness in their family?",
				Arrays.asList(new String[]{"Yes", "No", "Not sure"})));
		questions.get(2).addCardWeight("Yes", "Consider genetics", 0.8);
		questions.get(2).addCardWeight("No", "Consider genetics", -1);
		questions.add(new Question("How close are you to this person?",
				Arrays.asList(new String[]{"Not close", "Friend", "Close Friend", "Family"})));
		questions.add(new Question("Have you already talked to them about this?",
				Arrays.asList(new String[]{"Yes, they are stable for now", "Yes, but I'm not sure they are stable", "No"})));
		initialSurvey = new Survey(questions);
		return initialSurvey;
	}
	
	public String getId(){
		return id;
	}

    public void setID(String id){
        this.id = id;
    }
	
	public List<Danger> getDangers() {
		return dangers;
	}

	public void setDangers(List<Danger> dangers) {
		this.dangers = dangers;
	}

	public List<InformCard> getInformCards() {
		return informCards;
	}

	public void setInformCards(List<InformCard> informCards) {
		this.informCards = informCards;
	}

	public List<Goal> getPreventativeGoals() {
		return preventativeGoals;
	}

	public void setPreventativeGoals(List<Goal> preventativeGoals) {
		this.preventativeGoals = preventativeGoals;
	}

	public List<Goal> getRecoveryGoals() {
		return recoveryGoals;
	}

	public void setRecoveryGoals(List<Goal> recoveryGoals) {
		this.recoveryGoals = recoveryGoals;
	}

	public List<Goal> getRegularGoals() {
		return regularGoals;
	}

	public void setRegularGoals(List<Goal> regularGoals) {
		this.regularGoals = regularGoals;
	}
	
}
