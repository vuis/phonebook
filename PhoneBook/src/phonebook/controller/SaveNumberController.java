package phonebook.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import phonebook.connection.DatabaseConnection;
import phonebook.entities.Data;
import phonebook.entities.Number;
import phonebook.validator.Validator;
import phonebook.entities.Group;

public class SaveNumberController extends Validator {

	@FXML
	private TextField number;
	@FXML
	private TextField firstname;
	@FXML
	private TextField lastname;
	@FXML
	private TextField email;
	@FXML
	private ComboBox<String> groupBox;

	private boolean isEdit;
	private Data toShow;
	@SuppressWarnings("unused")
	private List<Data> dataList;

	public SaveNumberController() {
	}

	@FXML
	public void initialize() throws SQLException, IOException {
		List<Group> groupList = DatabaseConnection.getGroups();
		for (Group gr : groupList) {
			groupBox.getItems().add(gr.getGroupName());
		}
	}

	public void edit(List<Data> data, Data toShow) {
		this.toShow = toShow;
		this.dataList = data;
		isEdit = true;
		number.setText(toShow.getNumber().getNumber());
		firstname.setText(toShow.getFirstname());
		lastname.setText(toShow.getLastname());
		email.setText(toShow.getEmail());
		groupBox.setValue(toShow.getNumber().getGroup().getGroupName());
	}

	@FXML
	private void saveNumber() throws Exception {
		if (!(validateValue(firstname) & validateValue(lastname) & validateNumber(number) & validateValue(groupBox))) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Pogreška");
			alert.setHeaderText(null);
			alert.setContentText("Podaci nisu u ispravnom formatu!");
			alert.showAndWait();
			return;
		}
		Group group = null;
		for (Group gr : DatabaseConnection.getGroups()) {
			if (gr.getGroupName().equals(groupBox.getValue())) {
				group = gr;
			}
		}
		Number nr = new Number(number.getText(), group);
		Data data = new Data(firstname.getText(), lastname.getText(), email.getText(), nr);
		if (isEdit) {
			nr.setId(toShow.getNumber().getId());
			data.setId(toShow.getId());
			DatabaseConnection.updateData(data);
		} else {
			DatabaseConnection.saveData(data);
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Informacija");
		alert.setHeaderText(null);
		alert.setContentText("Broj je uspješno unesen");
		alert.showAndWait();

	}

}
