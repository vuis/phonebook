package phonebook.main;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class JavaFXMain extends Application { 
	
	private static BorderPane root; 
	private Stage primaryStage; 
	@Override 
	public void start(Stage stage) {
		primaryStage = stage; 
		try { 
			BorderPane rootPane = (BorderPane) 
					FXMLLoader.load(JavaFXMain.class.getResource("../xml/main.fxml")); 
			root = rootPane; 			
			BorderPane numbersPane = (BorderPane) 
					FXMLLoader.load(JavaFXMain.class 
							.getResource("../xml/numbers.fxml")); 
			JavaFXMain.setCenterPane(numbersPane); 
			Scene scene = new Scene(root, 670, 350); 
			scene.getStylesheets().add(getClass().getResource( "../main/application.css").toExternalForm()); 
			primaryStage.setScene(scene); primaryStage.show(); } 
		catch (Exception e) { 
			e.printStackTrace(); 
			} 
} 

	public static void main(String[] args) { 
		launch(args); 
	
		} 

	public static void setCenterPane(BorderPane centerPane) { 
		root.setCenter(centerPane); }
}