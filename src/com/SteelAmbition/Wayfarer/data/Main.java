package com.SteelAmbition.Wayfarer.data;

import com.SteelAmbition.Wayfarer.loader.*;

public class Main {
	
	public static void main(String[] args){
		StateManager s = new StateManager(new Loader());
		s.printSelf();
	}
}
