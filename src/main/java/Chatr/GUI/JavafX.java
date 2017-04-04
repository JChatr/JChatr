package Chatr.GUI;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JavafX extends Application {

//	public static void main(String[] args) {
//		launch(args);
//
//	}
//
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
	
	 
		public static void main(String[] args){
			launch(args);
		}
	 
		public void start(Stage stage) throws Exception {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("GUI.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}

}
