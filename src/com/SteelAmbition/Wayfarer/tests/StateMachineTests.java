package com.SteelAmbition.Wayfarer.tests;

import com.SteelAmbition.Wayfarer.data.*;
import com.SteelAmbition.Wayfarer.loader.DummyLoader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the state machine and its components
 * 
 * @author Jeff
 *
 */
public class StateMachineTests {
	
	@Test
	public void testDanger(){
		Danger d = new Danger("Isolation","",0,null);
		assertTrue(d.getName().equals("Isolation"));
		assertTrue(d.getLevel() == 0);
	}
	
	@Test
	public void testGoal(){
		Goal g = new Goal("Goal 1","Description 1",null);
		assertTrue(g.getName().equals("Goal 1"));
		assertTrue(g.getDescription().equals("Description 1"));
	}
	
	@Test
	public void testInformCard(){
		InformCard in = new InformCard("Content","Long Content");
		assertTrue(in.getShortDescription().equals("Content"));
		assertTrue(in.getLongDescription().equals("Long Content"));
	}
	
	@Test
	public void testPreventionNode(){
		List<Goal> g = new ArrayList<Goal>();
		g.add(new Goal("Goal 1", "Description 1",null));
		g.add(new Goal("Goal 2", "Description 2",null));
		PreventionNode p = new PreventionNode(g);
		assertTrue(p.getGoals().get(0).equals(new Goal("Goal 1", "Description 1",null)));
		assertTrue(p.getGoals().get(1).getName().equals("Goal 2"));
		assertTrue(p.getGoals().get(1).getDescription().equals("Description 2"));
	}
	
	@Test
	public void testQuestion(){
		Question q = new Question("What colour?",Arrays.asList(new String[]{"red","blue","green"}));
		assertTrue(q.getQuestion().equals("What colour?"));
		assertTrue(q.getAnswers().get(1).equals("blue"));
		q.answer(2);
		assertTrue(q.getAnswer() == 2);
		try{
			q.answer(5);
			fail();
		}
		catch(IndexOutOfBoundsException e){};
	}
	
	@Test
	public void testRecoveryNode(){
		List<Goal> g1 = new ArrayList<Goal>();
		g1.add(new Goal("t1","d1",null));
		List<Goal> g2 = new ArrayList<Goal>();
		g2.add(new Goal("t2","d2",null));
		RecoveryNode r = new RecoveryNode(g1,g2);
		assertTrue(r.getLongTermGoals().size() == 1);
		assertTrue(r.getLongTermGoals().get(0).equals(new Goal("t1","d1",null)));
		assertTrue(r.getRegularGoals().get(0).getDescription().equals("d2"));
	}
	
	@Test
	public void testStateManager(){
		//Ignore if this test breaks. It relies on other test code that may need to be changed
		StateManager s = new StateManager(new DummyLoader());
		assertTrue(s.getDangers() != null);
		assertTrue(s.getLongTermGoals() != null);
		assertTrue(s.getMostRelevantInfo(1) != null);
		assertTrue(s.getPreventionGoals() != null);
		assertTrue(s.getRegularGoals() != null);
		assertTrue(s.getState().equals("Preventative"));
	}
	
	@Test
	public void testSurvey(){
		List<Question> q = new ArrayList<Question>();
		q.add(new Question("Why does it?",Arrays.asList(new String[]{"yes","no","maybe"})));
		Survey s = new Survey(q);
		assertTrue(s.getQuestions().get(0).getQuestion().equals("Why does it?"));
	}
	
	@Test
	public void testPreventionNodeCompletion(){
		StateManager sm = new StateManager(new DummyLoader());
		List<PreventionNode> ns = new ArrayList<PreventionNode>();
		ns.add(new PreventionNode(Arrays.asList(new Goal[]{new Goal("G1","test goal",null)})));
		sm.setPreventionNodes(ns);
		List<String> ans = new ArrayList<String>();
		ans.add("yes");
		ans.add("no");
		List<Question> qs = new ArrayList<Question>();
		qs.add(new Question("It it complete?",ans));
		qs.get(0).addOutcome("yes","G1", true);
		qs.get(0).answer(0);
		Survey s = new Survey(qs);
		sm.applySurvey(s);
		assertTrue(sm.getState().equals("Recovery"));
	}
	
	@Test
	public void testPreventionNodeNonCompletion(){
		StateManager sm = new StateManager(new DummyLoader());
		List<PreventionNode> ns = new ArrayList<PreventionNode>();
		ns.add(new PreventionNode(Arrays.asList(new Goal[]{new Goal("G1","test goal",null), new Goal("G2","test goal2",null)})));
		sm.setPreventionNodes(ns);
		List<String> ans = new ArrayList<String>();
		ans.add("yes");
		ans.add("no");
		List<Question> qs = new ArrayList<Question>();
		qs.add(new Question("It it complete?",ans));
		qs.get(0).addOutcome("yes","G1", true);
		qs.get(0).answer(0);
		qs.add(new Question("It it still complete?",ans));
		qs.get(1).addOutcome("yes","G2", true);
		qs.get(1).answer(1);
		Survey s = new Survey(qs);
		sm.applySurvey(s);
		assertTrue(sm.getState().equals("Preventative"));
	}
	
	@Test
	public void testSurveyGeneration(){
		StateManager sm = new StateManager(new DummyLoader());
		List<PreventionNode> ns = new ArrayList<PreventionNode>();
		ns.add(new PreventionNode(Arrays.asList(new Goal[]{new Goal("G1","test goal1","Is goal1 Complete?"), 
				new Goal("G2","test goal2","Is goal2 Complete?")})));
		sm.setPreventionNodes(ns);
		sm.getDangers().add(new Danger("Danger1","",5,new Question("DangerQuestion1",Arrays.asList(new String[]{"a","b","c","d"}))));
		sm.getDangers().add(new Danger("Danger2","",4,new Question("DangerQuestion2",Arrays.asList(new String[]{"a","b","c","d"}))));
		sm.getDangers().add(new Danger("Danger3","",3,new Question("DangerQuestion3",Arrays.asList(new String[]{"a","b","c","d"}))));
		sm.getDangers().add(new Danger("Danger4","",2,new Question("DangerQuestion4",Arrays.asList(new String[]{"a","b","c","d"}))));
		sm.getDangers().add(new Danger("Danger5","",1,new Question("DangerQuestion5",Arrays.asList(new String[]{"a","b","c","d"}))));
		Survey s = sm.getSurvey();
		assertTrue(s.getQuestions().size() == 5);
		assertTrue(s.getQuestions().get(0).getQuestion().equals("Is goal1 Complete?"));
		assertTrue(s.getQuestions().get(1).getQuestion().equals("Is goal2 Complete?"));
		assertTrue(s.getQuestions().get(2).getQuestion().equals("DangerQuestion1"));
		assertTrue(s.getQuestions().get(3).getQuestion().equals("DangerQuestion2"));
		assertTrue(s.getQuestions().get(4).getQuestion().equals("DangerQuestion3"));
	}
	
	@Test
	public void testCompletedList(){
		StateManager sm = new StateManager(new DummyLoader());
		List<PreventionNode> ns = new ArrayList<PreventionNode>();
		ns.add(new PreventionNode(Arrays.asList(new Goal[]{new Goal("G1","test goal1","Is goal1 Complete?"), 
				new Goal("G2","test goal2","Is goal2 Complete?")})));
		sm.setPreventionNodes(ns);
		sm.completeGoal("G1");
		assertTrue(sm.getCompletedGoals().size() == 1);
		assertTrue(sm.getCompletedGoals().get(0).getName().equals("G1"));
	}
	
	@Test
	public void testInformCardWeights(){
		StateManager sm = new StateManager(new DummyLoader());
		List<PreventionNode> ns = new ArrayList<PreventionNode>();
		ns.add(new PreventionNode(Arrays.asList(new Goal[]{new Goal("G1","test goal",null)})));
		sm.setPreventionNodes(ns);
		List<InformCard> cards = new ArrayList<InformCard>();
		cards.add(new InformCard("test_card","longdesc"));
		sm.setInformCards(cards);
		List<String> ans = new ArrayList<String>();
		ans.add("yes");
		ans.add("no");
		List<Question> qs = new ArrayList<Question>();
		qs.add(new Question("It it complete?",ans));
		qs.get(0).addCardWeight("yes", "test_card", 1.0);
		qs.get(0).answer(0);
		Survey s = new Survey(qs);
		sm.applySurvey(s);
		assertTrue(1.0 == sm.getInformCards().get(0).getWeight());
	}
	
	@Test
	public void testDangerWeights(){
		StateManager sm = new StateManager(new DummyLoader());
		List<PreventionNode> ns = new ArrayList<PreventionNode>();
		ns.add(new PreventionNode(Arrays.asList(new Goal[]{new Goal("G1","test goal",null)})));
		sm.setPreventionNodes(ns);
		List<Danger> dangers = new ArrayList<Danger>();
		dangers.add(new Danger("test_danger","desc",0.1,new Question("Q1", Arrays.asList(new String[]{"Yes","No"}))));
		sm.setDangers(dangers);
		List<String> ans = new ArrayList<String>();
		ans.add("yes");
		ans.add("no");
		List<Question> qs = new ArrayList<Question>();
		qs.add(new Question("It it complete?",ans));
		qs.get(0).addDangerWeight("yes", "test_danger", 0.5);
		qs.get(0).answer(0);
		Survey s = new Survey(qs);
		sm.applySurvey(s);
		assertTrue(0.5 == sm.getDangers().get(0).getLevel());
	}
	
	@Test
	public void testDismissInformCards(){
		Database db = new Database();
		List<InformCard> cards = new ArrayList<InformCard>();
		cards.add(new InformCard("test_card","longdesc"));
		db.setInformCards(cards);
		StateManager sm = new StateManager(db,new Survey(Arrays.asList(new Question[]{})),"test_user");
		sm.dismissInformCard("test_card");
		assertTrue(0 == sm.getInformCards().size());
		sm.setUser("frank");
		assertTrue(1 == sm.getInformCards().size());
		sm.setUser("test_user");
		assertTrue(0 == sm.getInformCards().size());
	}
	
	@Test
	public void testGoalRemoval(){
		Database db = new Database();
		List<Goal> goals = new ArrayList<Goal>();
		goals.add(new Goal("G1","desc","Q1"));
		goals.add(new Goal("G2","desc","Q2"));
		db.setPreventativeGoals(goals);
		StateManager sm = new StateManager(db,new Survey(Arrays.asList(new Question[]{})),"test_user");
		List<String> ans = new ArrayList<String>();
		ans.add("yes");
		ans.add("no");
		List<Question> qs = new ArrayList<Question>();
		qs.add(new Question("It it complete?",ans));
		qs.get(0).addRemoval("yes", "G1", true);
		assertTrue(sm.getPreventionGoals().get(0).getName().equals("G1"));
		assertTrue(sm.getPreventionGoals().get(0).getCompletedBy() == null);
		qs.get(0).answer(0);
		Survey s = new Survey(qs);
		sm.applySurvey(s);
		assertTrue(sm.getPreventionGoals().get(0).getName().equals("G2"));
	}
	
	@Test
	public void testCompletedByUser(){
		Database db = new Database();
		List<Goal> goals = new ArrayList<Goal>();
		goals.add(new Goal("G1","desc","Q1"));
		goals.add(new Goal("G2","desc","Q2"));
		db.setPreventativeGoals(goals);
		StateManager sm = new StateManager(db,new Survey(Arrays.asList(new Question[]{})),"test_user");
		List<String> ans = new ArrayList<String>();
		ans.add("yes");
		ans.add("no");
		List<Question> qs = new ArrayList<Question>();
		qs.add(new Question("It it complete?",ans));
		qs.get(0).addOutcome("yes", "G1", true);
		qs.get(0).answer(0);
		Survey s = new Survey(qs);
		sm.applySurvey(s);
		assertTrue(sm.getCompletedGoals().get(0).getName().equals("G1"));
		assertTrue(sm.getCompletedGoals().get(0).getCompletedBy().equals("test_user"));
	}
}
