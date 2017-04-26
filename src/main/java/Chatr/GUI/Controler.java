package Chatr.GUI;



import java.awt.Button;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Controler {
//	
//	@FXML
//	Button send;
	
	@FXML
	TextArea textArea;
	
	@FXML
	TextField userMessageInput;
	
	@FXML
	Label label;
	
	@FXML
	protected void buttonPressed(){
		String text = userMessageInput.getText();
		label.setText(text);
		userMessageInput.clear();
	}

}
