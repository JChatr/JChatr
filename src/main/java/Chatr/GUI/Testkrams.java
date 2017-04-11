package Chatr.GUI;

import java.util.HashSet;


public class Testkrams {
	
	private static HashSet<String> userChats = new HashSet<String>();	
	
	public static void main(String[] args) {
		
		
		getUserChats().add("Chat1");
		getUserChats().add("Chat2");
		getUserChats().add("Chat3");
		getUserChats().add("Chat4");
		getUserChats().add("Chat5");
		
		
		
	}

	public static HashSet<String> getUserChats() {
		return userChats;
	}

	public void setUserChats(HashSet<String> userChats) {
		Testkrams.userChats = userChats;
	}
	
}	
