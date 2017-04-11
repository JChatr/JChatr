package Chatr.GUI;
import java.util.HashSet;

import Chatr.GUI.Testkrams;


public class GUI {
	 static public HashSet<String> getListOfConversatins(){
		return Testkrams.getUserChats();
		
	}
}
