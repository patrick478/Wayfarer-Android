package com.SteelAmbition.Wayfarer.data;

import java.util.*;

public class Database {
	
	private String id;
	
	private List<Danger> dangers;
	private List<InformCard> informCards;
	private List<Goal> preventativeGoals;
	private List<Goal> recoveryGoals;
	private List<Goal> regularGoals;
	
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
		preventativeGoals = preventativeGoals;
	}

	public List<Goal> getRecoveryGoals() {
		return recoveryGoals;
	}

	public void setRecoveryGoals(List<Goal> recoveryGoals) {
		recoveryGoals = recoveryGoals;
	}

	public List<Goal> getRegularGoals() {
		return regularGoals;
	}

	public void setRegularGoals(List<Goal> regularGoals) {
		regularGoals = regularGoals;
	}
	
}
