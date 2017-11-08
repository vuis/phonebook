package phonebook.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import phonebook.connection.DatabaseConnection;
import phonebook.entities.Data;
import phonebook.main.JavaFXMain;

public class NumbersController {
	@FXML
	private TextField number;

	@FXML
	private TextField name;

	@FXML
	private TableView<Data> numbersTable;

	@FXML
	private TableColumn<Data, String> numberColumn;

	@FXML
	private TableColumn<Data, String> firstnameColumn;

	@FXML
	private TableColumn<Data, String> lastnameColumn;

	@FXML
	private TableColumn<Data, String> emailColumn;

	@FXML
	private TableColumn<Data, String> groupColumn;

	@FXML
	public void initialize() {
		numberColumn.setCellValueFactory(new Callback<CellDataFeatures<Data, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Data, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getNumber().getNumber());
			}
		});
		firstnameColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("firstname"));
		lastnameColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("lastname"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("email"));
		groupColumn.setCellValueFactory(new Callback<CellDataFeatures<Data, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Data, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getNumber().getGroup().getGroupName());
			}
		});
	}

	public void showAllNumbers() throws Exception {
		List<Data> numbers = DatabaseConnection.getData();
		List<Data> filteredNumbers = new ArrayList<Data>();

		if (number.getText().isEmpty() == false) {

			filteredNumbers = numbers.stream().filter(p -> p.getNumber().getNumber().contains(number.getText()))
					.collect(Collectors.toList());
		} else if (name.getText().isEmpty() == false) {

			filteredNumbers = numbers.stream()
					.filter(p -> p.getFirstname().contains(name.getText()) || p.getLastname().contains(name.getText()))
					.collect(Collectors.toList());
		} else {
			filteredNumbers = numbers;
		}
		ObservableList<Data> dataList = FXCollections.observableArrayList(filteredNumbers);
		numbersTable.setItems(dataList);
	}

	public void edit() {
		try {
			FXMLLoader l = new FXMLLoader(getClass().getResource("../xml/editNumber.fxml"));
			BorderPane root = (BorderPane) l.load();
			SaveNumberController cont = l.<SaveNumberController> getController();
			cont.edit(numbersTable.getItems(), numbersTable.getSelectionModel().getSelectedItem());
			JavaFXMain.setCenterPane(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delete() throws Exception {
		Data data = numbersTable.getSelectionModel().getSelectedItem();
		numbersTable.getItems().remove(data);
		DatabaseConnection.deleteData(data);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Informacija");
		alert.setHeaderText(null);
		alert.setContentText("Broj je uspješno obrisan");
		alert.showAndWait();
	}

	public void createPdf() throws SQLException, IOException {
		DatabaseConnection.createPdf();
	}
}
