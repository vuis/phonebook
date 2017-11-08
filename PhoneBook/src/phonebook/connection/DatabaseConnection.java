package phonebook.connection;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mysql.jdbc.Statement;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import phonebook.entities.Data;
import phonebook.entities.Group;
import phonebook.entities.Number;

public class DatabaseConnection {

	private static final String DATABASE = "properties.txt";

	private static Connection databaseConnection() throws SQLException, IOException {
		Properties properties = new Properties();
		properties.load(new FileReader(DATABASE));
		String databaseURL = properties.getProperty("databaseURL");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		Connection databaseConnection = DriverManager.getConnection(databaseURL, username, password);
		return databaseConnection;
	}

	private static void closeConnection(Connection databaseConnection) throws SQLException, IOException {
		databaseConnection.close();
	}

	public static List<Group> getGroups() throws SQLException, IOException {
		Connection connection = databaseConnection();
		List<Group> groupList = new ArrayList<>();		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PHONEBOOK.GROUP");
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String groupName = resultSet.getString("groupName");
			Group group = new Group(id, groupName);
			groupList.add(group);
		}

		closeConnection(connection);
		return groupList;
	}

	public static List<Number> getNumbers() throws SQLException, IOException {
		Connection connection = databaseConnection();
		List<Group> groupList = getGroups();
		List<Number> numberList = new ArrayList<>();		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PHONEBOOK.NUMBERS");
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String number = resultSet.getString("number");
			int groupId = resultSet.getInt("groupId");
			Group group = null;
			for (Group gr : groupList) {
				if (gr.getId() == groupId) {
					group = gr;
				}
			}
			Number nr = new Number(id, number, group);
			numberList.add(nr);
		}
		closeConnection(connection);
		return numberList;
	}

	public static List<Data> getData() throws SQLException, IOException {
		Connection connection = databaseConnection();
		List<Number> numberList = getNumbers();
		List<Data> dataList = new ArrayList<>();		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PHONEBOOK.DATA");
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String firstname = resultSet.getString("firstname");
			String lastname = resultSet.getString("lastname");
			String email = resultSet.getString("email");
			int numberId = resultSet.getInt("numberId");
			Number dataNumber = null;
			for (Number nr : numberList) {
				if (nr.getId() == numberId) {
					dataNumber = nr;
				}
			}
			Data data = new Data(id, firstname, lastname, email, dataNumber);
			dataList.add(data);
		}
		closeConnection(connection);
		return dataList;
	}

	public static void saveData(Data data) throws Exception {
		Connection connection = databaseConnection();
		connection.setAutoCommit(false);
		PreparedStatement insertdata = connection.prepareStatement(
				"INSERT INTO PHONEBOOK.NUMBERS (number, groupId) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
		insertdata.setString(1, data.getNumber().getNumber());
		insertdata.setInt(2, data.getNumber().getGroup().getId());
		insertdata.executeUpdate();
		ResultSet generatedKeys = insertdata.getGeneratedKeys();
		if (generatedKeys.next()) {
			data.getNumber().setId(generatedKeys.getInt(1));
		}
		insertdata = connection
				.prepareStatement("INSERT INTO PHONEBOOK.DATA (firstname,lastname,email,numberId) VALUES (?,?,?,?)");
		insertdata.setString(1, data.getFirstname());
		insertdata.setString(2, data.getLastname());
		insertdata.setString(3, data.getEmail());
		insertdata.setInt(4, data.getNumber().getId());
		insertdata.executeUpdate();
		connection.commit();
		connection.setAutoCommit(true);
		closeConnection(connection);
	}

	public static void updateData(Data data) throws Exception {
		Connection connection = databaseConnection();
		connection.setAutoCommit(false);
		PreparedStatement insertdata = connection
				.prepareStatement("UPDATE PHONEBOOK.NUMBERS SET NUMBER=?, GROUPID=? WHERE ID=?");
		insertdata.setString(1, data.getNumber().getNumber());
		insertdata.setInt(2, data.getNumber().getGroup().getId());
		insertdata.setInt(3, data.getNumber().getId());
		insertdata.executeUpdate();
		insertdata = connection
				.prepareStatement("UPDATE PHONEBOOK.DATA SET FIRSTNAME=?, LASTNAME=?, EMAIL=?, NUMBERID=? WHERE ID=?");
		insertdata.setString(1, data.getFirstname());
		insertdata.setString(2, data.getLastname());
		insertdata.setString(3, data.getEmail());
		insertdata.setInt(4, data.getNumber().getId());
		insertdata.setInt(5, data.getId());
		insertdata.executeUpdate();
		connection.commit();
		connection.setAutoCommit(true);
		closeConnection(connection);
	}

	public static void deleteData(Data data) throws Exception {
		Connection connection = databaseConnection();
		connection.setAutoCommit(false);
		PreparedStatement deletedata = connection.prepareStatement("DELETE FROM PHONEBOOK.NUMBERS WHERE ID=?");
		deletedata.setInt(1, data.getNumber().getId());
		deletedata.executeUpdate();
		deletedata = connection.prepareStatement("DELETE FROM PHONEBOOK.DATA WHERE ID=?");
		deletedata.setInt(1, data.getId());
		deletedata.executeUpdate();
		connection.commit();
		connection.setAutoCommit(true);
		closeConnection(connection);
	}

	public static void createPdf() throws SQLException, IOException {
		Connection conn = databaseConnection();
		InputStream in = DatabaseConnection.class.getResourceAsStream("../jasper/PhoneBook.jrxml");
		JasperReport report = null;
		JasperPrint jasperPrint = null;
		File folder = new File("C:/PhoneBook");
		if (!folder.exists()) {
			folder.mkdir();
		}
		try {
			report = JasperCompileManager.compileReport(in);
			jasperPrint = JasperFillManager.fillReport(report, null, conn);
			JasperExportManager.exportReportToPdfFile(jasperPrint, folder + "/PhoneBook.pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		conn.close();
		if (Desktop.isDesktopSupported()) {
			try {
				File myFile = new File("C:/PhoneBook/Phonebook.pdf");
				Desktop.getDesktop().open(myFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
