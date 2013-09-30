package com.SteelAmbition.Wayfarer.data;

import com.SteelAmbition.Wayfarer.loader.Loader;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


/**
 * The class that manages everything inside the state machine
 * 
 * @author Jeff
 *
 */
public class StateManager implements StateAccess{
	
	private String state = "Preventative";
	
	private List<PreventionNode> preventionNodes = new ArrayList<PreventionNode>();
	private int preventionNodeIndex = 0;
	private List<RecoveryNode> recoveryNodes = new ArrayList<RecoveryNode>();
	private int recoveryNodeIndex = 0;
	
	private List<Goal> goalPool = new ArrayList<Goal>();
	
	private List<Danger> dangers = new ArrayList<Danger>();
	private List<InformCard> informCards = new ArrayList<InformCard>();
	
	private Survey survey;
	private Date lastSurvey;
	
	public StateManager(Loader l){
		dangers = l.loadDangers();
		Collections.sort(dangers);
		preventionNodes = l.loadPreventionNodes();
		recoveryNodes = l.loadRecoveryNodes();
		survey = l.loadSurvey();
		informCards = l.loadInformCards();
		lastSurvey = l.getLastSurvey();
	}
	
	public StateManager(Database db, Survey s){
		for(Goal g:db.getPreventativeGoals()){
			preventionNodes.add(new PreventionNode(Arrays.asList(new Goal[]{g})));
		}
		for(Goal g:db.getRecoveryGoals()){
			recoveryNodes.add(new RecoveryNode(Arrays.asList(new Goal[]{g}),Arrays.asList(new Goal[0])));
		}
		for(Goal g:db.getRegularGoals()){
			goalPool.add(g);
		}
		for(Danger d:db.getDangers()){
			dangers.add(d);
		}
		for(InformCard i:db.getInformCards()){
			informCards.add(i);
		}
		s.apply(this);
	}
	
	public void goPreventative(){
		state = "Preventative";
		preventionNodeIndex = 0;
	}
	
	public void goRecovery(){
		state = "Recovery";
	}
	
	public List<InformCard> getMostRelevantInfo(int num){
		survey.sortInfo(informCards);
		return informCards;
	}
	
	public List<InformCard> getInformCards(){
		return informCards;
	}
	
	public void printSelf(){
		StringBuilder b = new StringBuilder();
		//Dangers
		b.append("Dangers:\n");
		for(Danger d : dangers){
			b.append("   "+d.getName()+": ");
			for(int i=0;i<10;i++){
				if(i<d.getLevel()*10) b.append("*");
				else b.append("-");
			}
			b.append("\n");
		}
		//Nodes
		b.append("Complete:\n");
		for(int i=0;i<preventionNodeIndex;i++){
			b.append("   "+preventionNodes.get(i)+"\n");
		}
		b.append("Current: "+preventionNodes.get(preventionNodeIndex)+"\n");
		b.append("Next:\n");
		for(int i=preventionNodeIndex+1;i<preventionNodes.size();i++){
			b.append("   "+preventionNodes.get(i)+"\n");
		}
		b.append("Relevant Information:\n");
		for(int i=0;i<Math.min(informCards.size(),5);i++){
			b.append("   "+(i+1)+": "+informCards.get(i));
		}
		b.append("\nLong Term Goals:\n   {");
		for(int i=0;i<recoveryNodes.get(recoveryNodeIndex).getLongTermGoals().size();i++){
			b.append(recoveryNodes.get(recoveryNodeIndex).getLongTermGoals().get(i)+"}\n   {");
		}
		b.delete(b.length()-5, b.length());
		b.append("\nRegular Goals:\n   {");
		for(int i=0;i<recoveryNodes.get(recoveryNodeIndex).getRegularGoals().size();i++){
			b.append(recoveryNodes.get(recoveryNodeIndex).getRegularGoals().get(i)+"}\n   {");
		}
		b.delete(b.length()-5, b.length());
		System.out.println(b);
	}
	
	public String getState(){
		return state;
	}

	public List<Danger> getDangers() {
		return dangers;
	}

	public List<Goal> getPreventionGoals() {
		return preventionNodes.get(preventionNodeIndex).getGoals();
	}

	public List<Goal> getLongTermGoals() {
		return recoveryNodes.get(recoveryNodeIndex).getLongTermGoals();
	}

	public List<Goal> getRegularGoals() {
		return recoveryNodes.get(recoveryNodeIndex).getRegularGoals();
	}
	
	public void applySurvey(Survey s) {
		s.apply(this);
		updateState();
	}
	
	public void setPreventionNodes(List<PreventionNode> ns){
		preventionNodeIndex = 0;
		preventionNodes = ns;
	}
	
	private void updateState(){
		if(state.equals("Preventative") && preventionNodes.get(preventionNodeIndex).isComplete()){
			preventionNodeIndex++;
			if(preventionNodeIndex == preventionNodes.size()) state = "Recovery";
		}
	}
	
	/*public void fixGSON(){
		if(preventionNodes == null) preventionNodes = new ArrayList<PreventionNode>();
		if(recoveryNodes == null) recoveryNodes = new ArrayList<RecoveryNode>();
		if(goalPool == null) goalPool = new ArrayList<Goal>();
		if(informCards == null) informCards = new ArrayList<InformCard>();
		if(dangers == null) dangers = new ArrayList<Danger>();
	}*/
	
	public Survey getSurvey() {
		//if(new Date().getTime() - lastSurvey.getTime() < 1000*60*60*24)	return null;
		List<Question> qs = new ArrayList<Question>();
		for(Goal g:preventionNodes.get(preventionNodeIndex).getGoals()){
			qs.add(new Question(g.getRelatedQuestion(),Arrays.asList(new String[]{"Yes", "No"})));
		}
		Collections.sort(dangers);
		for(int i=0;i<3 && i<dangers.size();i++){
			qs.add(dangers.get(i).getRelevantQuestion());
		}
		survey = new Survey(qs);
		lastSurvey = new Date();
		return survey;
	}
	
	public void completeGoal(String goalName){
		for(Goal g:getPreventionGoals()){
			if(goalName.equals(g.getName())){
				g.complete(true);
			}
		}
		for(Goal g:getLongTermGoals()){
			if(goalName.equals(g.getName())){
				g.complete(true);
			}
		}
		for(Goal g:getRegularGoals()){
			if(goalName.equals(g.getName())){
				g.complete(true);
			}
		}
	}
	
	public List<Goal> getCompletedGoals(){
		List<Goal> ans = new ArrayList<Goal>();
		for(Goal g:getPreventionGoals()){
			if(g.isComplete()){
				ans.add(g);
			}
		}
		for(Goal g:getLongTermGoals()){
			if(g.isComplete()){
				ans.add(g);
			}
		}
		for(Goal g:getRegularGoals()){
			if(g.isComplete()){
				ans.add(g);
			}
		}
		Collections.sort(ans);
		return ans;
	}
	
	//--------------
	//STATIC METHODS
	
	public static Database newUserDatabase(String name){
		Database db = null;
		try { 
			// Set up & establish connection 
			URL url = new URL("http://wayfarer-server.herokuapp.com/subjects");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
			conn.setReadTimeout(100000); 
			conn.setConnectTimeout(150000); 
			conn.setRequestMethod("PUT"); 
			conn.addRequestProperty("Content-Type", "application/json");
			conn.setDoInput(true); 
			conn.setDoOutput(true); 

			// Send data 
			OutputStream os = conn.getOutputStream(); 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, 
					"UTF-8")); 
			//System.out.println(a.getHtml());
			bw.write("{\"name\": \""+name+"\"}");
			bw.close(); 
			os.close(); 

			// Get response 
			String reply = convertStreamToString(conn.getInputStream());
			System.out.println(conn.getResponseCode()+": "+conn.getResponseMessage()+"\n"+reply); 
			JsonReader jr = new JsonReader(new StringReader(reply));
			jr.setLenient(true);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			db = gson.fromJson(jr,Database.class);
		} catch (MalformedURLException e) { 
			System.out.println("PostTask "+e.getMessage()); 
		} catch (IOException e) { 
			System.out.println("PostTask "+e.getMessage()); 
		} 
		return db;
	}
	
	public static void postState(StateManager state,String id){
		try { 
			// Set up & establish connection 
			URL url = new URL("http://wayfarer-server.herokuapp.com/subjects/"+id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
			conn.setReadTimeout(100000); 
			conn.setConnectTimeout(150000); 
			conn.setRequestMethod("POST"); 
			conn.addRequestProperty("Content-Type", "application/json");
			conn.setDoInput(true); 
			conn.setDoOutput(true); 

			// Send data 
			OutputStream os = conn.getOutputStream(); 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, 
					"UTF-8")); 
			//System.out.println(a.getHtml());
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String out = gson.toJson(state);
			bw.write("{\"state\": "+out+"}");
			bw.close(); 
			os.close(); 
			System.out.println(conn.getResponseCode()+": "+conn.getResponseMessage()); 
			System.out.println("{\"state\": "+out+"}");

			// Get response 
		} catch (MalformedURLException e) { 
			System.out.println("PostTask "+e.getMessage()); 
		} catch (IOException e) { 
			System.out.println("PostTask "+e.getMessage()); 
		} 
	}
	
	public static StateManager readState(String id){
		StateManager state = null;
		try { 
			// Set up & establish connection 
			URL url = new URL("http://wayfarer-server.herokuapp.com/subjects/"+id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
			conn.setReadTimeout(100000); 
			conn.setConnectTimeout(150000); 
			conn.setRequestMethod("GET"); 
			conn.addRequestProperty("Content-Type", "application/json");
			conn.setDoInput(true); 
			conn.setDoOutput(false);

			// Get response 
			if(conn.getResponseCode() != 200) return null;
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String reply = convertStreamToString(conn.getInputStream());
			JsonElement jelement = new JsonParser().parse(reply);
			JsonObject  jobject = jelement.getAsJsonObject();
			reply = jobject.get("state").toString();
			System.out.println(conn.getResponseCode()+": "+conn.getResponseMessage()+"\n"+reply); 
			JsonReader jr = new JsonReader(new StringReader(reply));
			jr.setLenient(true);
			state = gson.fromJson(jr,StateManager.class);
		} catch (MalformedURLException e) { 
			System.out.println("PostTask "+e.getMessage()); 
		} catch (IOException e) { 
			System.out.println("PostTask "+e.getMessage()); 
		} 
		return state;
	}
	
	private static String convertStreamToString(InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
