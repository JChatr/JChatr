package Chatr.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Controler {
	
	@FXML
	TextArea textArea;
	
	@FXML
	TextField textField;
	
	@FXML
	Label label;
	
	@FXML
	protected void buttonPressed(){
		String text = textField.getText();
		label.setText(text);
		textField.clear();
	}

}
