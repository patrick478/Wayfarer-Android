package com.SteelAmbition.Wayfarer.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.SteelAmbition.Wayfarer.loader.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class Main {

	public static void main(String[] args){
		/*try {
			Reader reader = new FileReader(new File("TestPerson.json"));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			StateManager s = gson.fromJson(reader, StateManager.class);
			s.printSelf();
		} catch (FileNotFoundException e) {}*/
		/*StateManager s = new StateManager(new Database(),new Survey(Arrays.asList(new Question[0])));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String out = gson.toJson(s);
		try {
			FileOutputStream f = new FileOutputStream(new File("TestPerson.json"));
			f.write(out.getBytes());
		} catch (IOException e) {}*/
		/*Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String out = gson.toJson(new Database());
		try {
			FileOutputStream f = new FileOutputStream(new File("TestDB.json"));
			f.write(out.getBytes());
		} catch (IOException e) {}*/

		//Posting tests
		/*String name = "Jesus";
		Database db = newUserDatabase(name);
		StateManager state = new StateManager(db,new Survey(Arrays.asList(new Question[]{})));
		postState(state,db.getId());
		StateManager s = readState(db.getId());
		System.out.println("-----------");
		s.printSelf();*/
		
		
		//
		//
		
		//TEMP JUNK
		Database db = makeDB();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String out = gson.toJson(db);
		try {
			FileOutputStream f = new FileOutputStream(new File("TestDB.json"));
			f.write(out.getBytes());
		} catch (IOException e) {}
	}
	
	static String convertStreamToString(InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	private static Database makeDB(){
		Database db = new Database();
		
		List<Danger> dangers = new ArrayList<Danger>();
		try {
			Scanner sc = new Scanner(new File("DangerData.txt"));
			while(sc.hasNext()){
				String name = sc.nextLine();
				String desc = sc.nextLine();
				String q = sc.nextLine();
				List<String> ans = new ArrayList<String>();
				String a;
				while(sc.hasNextLine() && !(a = sc.nextLine()).equals("")){
					ans.add(a);
				}
				Danger d = new Danger(name,desc,0,new Question(q,ans));
				dangers.add(d);
				if(sc.hasNextLine()) sc.nextLine();
			}
		} catch (FileNotFoundException e) {}
		db.setDangers(dangers);
		
		List<InformCard> informCards = new ArrayList<InformCard>();
		try {
			Scanner sc = new Scanner(new File("InformCard.txt"));
			while(sc.hasNext()){
				String shortDesc = sc.nextLine();
				String longDesc = sc.nextLine();
				InformCard i = new InformCard(shortDesc,longDesc);
				informCards.add(i);
				if(sc.hasNextLine()) sc.nextLine();
			}
		} catch (FileNotFoundException e) {}
		db.setInformCards(informCards);
		
		return db;
	}
}
