package com.SteelAmbition.Wayfarer.data;

import java.util.*;

public interface StateAccess {
	
	public List<Danger> getDangers();
	public List<Goal> getPreventionGoals();
	public List<InformCard> getInformCards();
	public List<InformCard> getMostRelevantInfo(int num);
	
	public List<Goal> getLongTermGoals();
	public List<Goal> getRegularGoals();
	
	public Survey getSurvey();
	public void applySurvey(Survey s);
	
	public void goPreventative();
	public void goRecovery();
	
	public void dismissInformCard(String shortDesc);
	public List<Goal> getGoalsCompletedByUser();
	public List<Goal> getCompletedGoals();
}
