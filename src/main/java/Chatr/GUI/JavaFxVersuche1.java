package Chatr.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



public class JavaFxVersuche1 extends Application {

	@Override
	public void start(Stage stage) {
	VBox root = new VBox(40);
	ObservableList<Node> children = root.getChildren();
	final Rectangle rect = new Rectangle(80, 50);
	rect.setFill(Color.AQUAMARINE);
	children.add(rect);
	Button button = new Button("Rotate");
	button.setOnAction(new EventHandler<ActionEvent>() {
	

	public void handle(ActionEvent event) {
	rect.setRotate(rect.getRotate() + 10);
	}
	});
	children.add(button);
	Scene scene = new Scene(root, 120, 130);
	scene.setFill(Color.BEIGE);
	stage.setTitle("Rotate Test");
	stage.setScene(scene);
	stage.show();
	}

	public static void main(String[] args){
		launch(args);
	}
	
	
	
	
	
	
	
	// @Override
	// public void start(Stage stage) {
	// Label label = new Label("Chat 123");
	// StackPane root = new StackPane();
	// root.getChildren().add(label);
	// Scene scene = new Scene(root, 800, 600);
	// //scene.setFill(Color.CHOCOLATE);
	// scene.setStyle("-fx-background-color: #FF0000;");
	// stage.setTitle("Chat");
	// stage.setScene(scene);
	// stage.show();
	// }
	//
	//
	// public static void main(String[] args) {
	// launch(args);
	// }

//	@Override
//	public void start(Stage stage) {
//		GridPane grid = new GridPane();
//		grid.setHgap(15);
//		grid.setVgap(25);
//		ColumnConstraints constraint = new ColumnConstraints();
//		constraint.setPercentWidth(25);
//		grid.getColumnConstraints().addAll(constraint, constraint, constraint, constraint);
//		grid.setPadding(new Insets(10));
//		
//		// Überschrift
//		
//		grid.add(new Label("Label"), 0, 0);
//		
//		// Button 
//		
//		grid.add(new Button("Button"), 1, 0);
//		
//		// Chat-Übersicht evtl mit Liste
//		
//		ListView<String> listView = new ListView<String>();
//		listView.getItems().addAll("List Item 1", "List Item 2", "List Item 3");
//		grid.add(listView, 3, 1);
//		
//		//Textfeld -> neue Nachrricht
//		
//		grid.add(new TextField("TextField"), 1, 2);
//		
//		// Paaworteingabe -> Login
//		
//		PasswordField passwordField = new PasswordField();
//		passwordField.setText("Password");
//		grid.add(passwordField, 2, 2);
//		
//	
//		// Text-Area -> neue Nachricht 
//		
//		TextArea textArea = new TextArea("TextArea");
//		textArea.setMinHeight(60.00);
//		grid.add(textArea, 2, 3);
//		
//		
//		Scene scene = new Scene(grid, 600, 320);
//		scene.setFill(Color.BEIGE);
//		stage.setTitle("Chat");
//		stage.setScene(scene);
//		stage.show();
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
}
