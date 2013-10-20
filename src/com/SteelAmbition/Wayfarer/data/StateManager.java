package com.SteelAmbition.Wayfarer.data;

import com.SteelAmbition.Wayfarer.Network.*;
import com.SteelAmbition.Wayfarer.loader.DummyLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringReader;
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

	private String currentUser;

	public StateManager(Database db, Survey s,String currentUser){
		this.currentUser = currentUser;
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

	public StateManager(DummyLoader l) {
		preventionNodes = l.loadPreventionNodes();
		recoveryNodes = l.loadRecoveryNodes();
		survey = l.loadSurvey();
		dangers = l.loadDangers();
		informCards = l.loadInformCards();
	}

	public String getCurrentUser(){
		return currentUser;
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
		List<InformCard> ans = new ArrayList<InformCard>(getInformCards());
		while(num<ans.size()){
			ans.remove(num);
		}
		return ans;
	}

	public List<InformCard> getInformCards(){
		List<InformCard> ans = new ArrayList<InformCard>(informCards);
		List<InformCard> dismissals = new ArrayList<InformCard>();
		for(InformCard i:informCards){
			if(i.isDismissedFor(currentUser)) dismissals.add(i);
		}
		ans.removeAll(dismissals);
		return ans;
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
			qs.add(g.getRelatedQuestion());
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
				g.complete(true,currentUser);
			}
		}
		for(Goal g:getLongTermGoals()){
			if(goalName.equals(g.getName())){
				g.complete(true,currentUser);
			}
		}
		for(Goal g:getRegularGoals()){
			if(goalName.equals(g.getName())){
				g.complete(true,currentUser);
			}
		}
	}

	public List<Goal> getGoalsCompletedByUser(){
		List<Goal> goals = getCompletedGoals();
		List<Goal> toRemove = new ArrayList<Goal>();
		for(Goal g:goals){
			if(!g.getCompletedBy().equals(currentUser)){
				toRemove.add(g);
			}
		}
		for(Goal g:toRemove){
			goals.remove(g);
		}
		return goals;
	}

	public List<Goal> getCompletedGoals(){
		List<Goal> ans = new ArrayList<Goal>();
		for(PreventionNode n:preventionNodes){
			for(Goal g:n.getGoals()){
				if(g.isComplete()){
					ans.add(g);
				}
			}
		}
		for(RecoveryNode n:recoveryNodes){
			for(Goal g:n.getLongTermGoals()){
				if(g.isComplete()){
					ans.add(g);
				}
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

	public void dismissInformCard(String shortDesc){
		for(InformCard i:informCards){
			if(i.getShortDescription().equals(shortDesc)) i.dismissForUser(currentUser);
		}
	}

	public void setUser(String name){
		currentUser = name;
	}


	//--------------
	//STATIC METHODS

	public static Database newUserDatabase(String name){
		Database db = null;

		try {

            Subject s = new Subject(ServerAccess.getCurrentUser(), name);

            ServerAccess.getCurrentUser().setSubjectId(s.getId());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonReader jr = new JsonReader(new StringReader(s.getDatapool().toString()));
            jr.setLenient(true);
            db = gson.fromJson(jr,Database.class);


            db.setID(ServerAccess.getCurrentUser().getId());

//		} catch (MalformedURLException e) {
//			System.out.println("PostTask "+e.getMessage());
//		} catch (IOException e) {
//			System.out.println("PostTask "+e.getMessage());
		} catch (NetworkFailureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (AuthenticationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (AlreadyExistsException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return db;
	}

	public static void postState(StateManager state,String id){
		try { 
			// Set up & establish connection 
			/*URL url = new URL("http://wayfarer-server.herokuapp.com/subjects/"+id);
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
			*/
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Subject s =  ServerAccess.getCurrentUser().getSubject();
            s.setState(gson.toJson(state));
            s.update();


			// Get response 
		} catch (NetworkFailureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (AuthenticationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OldDataException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

	public static StateManager readState(String id){
		StateManager state = null;
		try { 
			// Set up & establish connection 
			/*URL url = new URL("http://wayfarer-server.herokuapp.com/subjects/"+id);
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
			state = gson.fromJson(jr,StateManager.class); */
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Subject subject = new Subject(ServerAccess.getCurrentUser());
            JSONObject jobject = subject.getState();
            JsonReader jr = new JsonReader(new StringReader(jobject.toString()));
            jr.setLenient(true);
            state = gson.fromJson(jr,StateManager.class);

        } catch (NetworkFailureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (AuthenticationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DoesNotExistException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return state;
	}

	public void setInformCards(List<InformCard> cards){
		informCards = cards;
	}

	public void setDangers(List<Danger> dangers){
		this.dangers = dangers;
	}

	private static String convertStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
