package Chatr.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class JavaFx extends Application{
	
	@Override
	public void start(Stage stage) {
	Label label = new Label("Welcome");
	StackPane root = new StackPane();
	root.getChildren().add(label);
	Scene scene = new Scene(root, 400, 100);
	scene.setFill(Color.BEIGE);
	stage.setTitle("First FX");
	stage.setScene(scene);
	stage.show();
	}
	public static void main(String[] args) {
	launch(args);
	}
}
