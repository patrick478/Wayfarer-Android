package com.SteelAmbition.Wayfarer.tests;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

import com.SteelAmbition.Wayfarer.data.*;
import com.SteelAmbition.Wayfarer.loader.Loader;


/**
 * Tests for the state machine and its components
 * 
 * @author Jeff
 *
 */
public class StateMachineTests {
	
	@Test
	public void testDanger(){
		Danger d = new Danger("Isolation",0);
		assertTrue(d.getName().equals("Isolation"));
		assertTrue(d.getLevel() == 0);
	}
	
	@Test
	public void testGoal(){
		Goal g = new Goal("Goal 1","Description 1");
		assertTrue(g.getName().equals("Goal 1"));
		assertTrue(g.getDescription().equals("Description 1"));
	}
	
	@Test
	public void testInformCard(){
		InformCard in = new InformCard("Content");
		assertTrue(in.getContent().equals("Content"));
	}
	
	@Test
	public void testPreventionNode(){
		List<Goal> g = new ArrayList<Goal>();
		g.add(new Goal("Goal 1", "Description 1"));
		g.add(new Goal("Goal 2", "Description 2"));
		PreventionNode p = new PreventionNode(g);
		assertTrue(p.getGoals().get(0).equals(new Goal("Goal 1", "Description 1")));
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
		g1.add(new Goal("t1","d1"));
		List<Goal> g2 = new ArrayList<Goal>();
		g2.add(new Goal("t2","d2"));
		RecoveryNode r = new RecoveryNode(g1,g2);
		assertTrue(r.getLongTermGoals().size() == 1);
		assertTrue(r.getLongTermGoals().get(0).equals(new Goal("t1","d1")));
		assertTrue(r.getRegularGoals().get(0).getDescription().equals("d2"));
	}
	
	@Test
	public void testStateManager(){
		//Ignore if this test breaks. It relies on other test code that may need to be changed
		StateManager s = new StateManager(new Loader());
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
}
