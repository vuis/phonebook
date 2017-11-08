package phonebook.controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import phonebook.main.JavaFXMain;

public class MainController {
	public void showNumbers() { 
		try { 
			BorderPane numbersPane = (BorderPane) 
					FXMLLoader.load(JavaFXMain.class 
							.getResource("../xml/numbers.fxml")); 
			JavaFXMain.setCenterPane(numbersPane); 
			} 
		catch (Exception e) { 
			e.printStackTrace(); 
			} 
		}
	
	public void newNumber() { 
		try { 
			BorderPane noviClanPane = (BorderPane) 
					FXMLLoader.load(JavaFXMain.class 
							.getResource("../xml/editNumber.fxml")); 
			JavaFXMain.setCenterPane(noviClanPane); 
			} 
		catch (Exception e) { 
			e.printStackTrace(); 
			} 
		}
	
}
