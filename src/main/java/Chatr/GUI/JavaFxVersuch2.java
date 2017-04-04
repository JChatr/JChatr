//package Chatr.GUI;
//
//import java.awt.Label;
//
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//
//
//public class JavaFxVersuch2 extends Application{
//
//	
//	
//	public HBox addHBox() {
//	    HBox hbox = new HBox();
//	    hbox.setPadding(new Insets(15, 12, 15, 12));
//	    hbox.setSpacing(10);
//	    hbox.setStyle("-fx-background-color: #336699;");
//
//	    Button buttonCurrent = new Button("Current");
//	    buttonCurrent.setPrefSize(100, 20);
//
//	    Button buttonProjected = new Button("Projected");
//	    buttonProjected.setPrefSize(100, 20);
//	    hbox.getChildren().addAll(buttonCurrent, buttonProjected);
//
//	    return hbox;
//	}
//	
//	
//	 public static void main(String[] args) {
//	        launch(args);
//	    }
//	    
//
//	    @Override
//	    public void start(Stage primaryStage) {
//	        primaryStage.setTitle("Chatr");
//	       
//	        Button button1 = new Button("Button 1");
//	        Button button2 = new Button("Button 2");
//	        Button button3 = new Button("Button 3");
//	        Button button4 = new Button("Button 4");
//	        Button button5 = new Button("Button 5");
//	        Button button6 = new Button("Button 6");
//
//	        GridPane gridPane = new GridPane();
//
////	        gridPane.add(button1, 0, 0, 1, 1);
////	        gridPane.add(button2, 1, 0, 1, 1);
////	        gridPane.add(button3, 2, 0, 1, 1);
////	        gridPane.add(button4, 0, 1, 1, 1);
////	        gridPane.add(button5, 1, 1, 1, 1);
////	        gridPane.add(button6, 2, 1, 1, 1);
//	        
//	        HBox hbox = new HBox();
////	        HBox hbox = addHBox();
//	        
//	        
//	        HBox.setHgrow(button1, Priority.ALWAYS);
//	        HBox.setHgrow(button2, Priority.ALWAYS);
//	        button1.setMaxWidth(Double.MAX_VALUE);
//	        button2.setMaxWidth(Double.MAX_VALUE);
//	        hbox.getChildren().addAll(button1, button2);
//	    
//
//	        
//	       
//	        
//	        Scene scene = new Scene(gridPane, 800, 600);
//	        
//	        
//	        primaryStage.setScene(scene);
//	        primaryStage.show();
//	    }
//}
