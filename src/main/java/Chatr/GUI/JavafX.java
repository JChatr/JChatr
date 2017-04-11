package Chatr.GUI;

import java.util.HashSet;

import javax.swing.text.Element;
import javax.swing.text.html.ListView;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JavafX extends Application {


	public static final HashSet<String> data = GUI.getListOfConversatins();
			
	public static void main(String[] args) {
		launch(args);

	}

//	@Override
//	public void start(Stage stage) throws Exception {
//		Scene scene = new Scene();
//		stage.setScene(scene);
//		stage.show();
//	}
	
		public void start(Stage stage) throws Exception {
			stage.setTitle("Chat"); 
			
			
			// ListView listView = new ListView((Element) data);
			
			
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("GUI.fxml"));
			Scene scene = new Scene(root);
			
			
			
			stage.setScene(scene);
			stage.show();
	        
		}

}
