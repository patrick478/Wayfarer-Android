package com.SteelAmbition.Wayfarer.loader;

import java.util.*;

import com.SteelAmbition.Wayfarer.data.*;


/**
 * TEMPORARY CODE
 * 
 * The content of these methods will be replaced when there is real data.
 * The method structure will likely stay the same or similar 
 * 
 * @author Jeff
 *
 */
public class Loader {
	
	private List<Danger> dangers = new ArrayList<Danger>();
	
	public Loader(){
		dangers.add(new Danger("Isolation",0.5));
		dangers.add(new Danger("Self esteem",0.3));
		dangers.add(new Danger("Godzilla",0.9));
	}
	
	public Loader(String s){
		
	}
	
	public List<Danger> loadDangers(){
		return dangers;
	}
	
	public Survey loadSurvey(){
		List<Question> q = new ArrayList<Question>();
		q.add(new Question("Question1",Arrays.asList(new String[]{"Ans1","Ans2","Ans3","Ans4"})));
		Survey s = new Survey(q);
		return s;
	}
	
	public List<InformCard> loadInformCards(){
		List<InformCard> in = new ArrayList<InformCard>();
		in.add(new InformCard("-Information Example 1-"));
		return in;
	}
	
	public List<PreventionNode> loadPreventionNodes(){
		List<PreventionNode> ans = new ArrayList<PreventionNode>();
		List<Goal> goals = new ArrayList<Goal>();
		goals.add(new Goal("Goal1","Description1"));
		goals.add(new Goal("Goal2","Description2"));
		ans.add(new PreventionNode(goals));
		goals = new ArrayList<Goal>();
		goals.add(new Goal("Goal3","Description3"));
		ans.add(new PreventionNode(goals));
		goals = new ArrayList<Goal>();
		goals.add(new Goal("Goal4","Description4"));
		goals.add(new Goal("Goal5","Description5"));
		goals.add(new Goal("Goal6","Description6"));
		ans.add(new PreventionNode(goals));
		return ans;
	}

	public List<RecoveryNode> loadRecoveryNodes() {
		List<RecoveryNode> ans = new ArrayList<RecoveryNode>();
		List<Goal> goals1 = new ArrayList<Goal>();
		goals1.add(new Goal("Long Term Goal 1","Description 1"));
		List<Goal> goals2 = new ArrayList<Goal>();
		goals2.add(new Goal("Regular Goal 1","Description 1"));
		ans.add(new RecoveryNode(goals1,goals2));
		return ans;
	}
}
