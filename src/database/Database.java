/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import courriel.CourrierModel;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import notification.NotificationModel;
import utils.Utils;

public class Database {

	Connection connection;
	Statement statement;
	DatabaseMetaData metadata;

	public boolean connect(String typeConnection) throws ClassNotFoundException, SQLException {
		switch (typeConnection) {
		case "remote":
			return remote();
		case "embadded":
			return embadded();
		}
		return false;

	}

	public void initialize() throws SQLException {
		createCourrierTable();
		createNotificationsTable();
	}

	public boolean remote() throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/easylife", "fructorich",
				"arfqfrarolflor");
		if (connection != null) {
			System.out.println("Connected to database #1");
			statement = connection.createStatement();
			metadata = connection.getMetaData();

			return true;
		}
		return false;
	}

	public boolean embadded() throws SQLException {
		String dbURL1 = "jdbc:derby:Database/gestion;create=true;user=zato;password=zato";
		connection = DriverManager.getConnection(dbURL1);
		if (connection != null) {
			System.out.println("Connected to database #1");
			statement = connection.createStatement();
			metadata = connection.getMetaData();
			return true;
		}
		return false;

	}

	public String getTextConnection() {
		if (connection == null) {
			return "Connexion impossible à la base de donnée";
		}

		return "Connexion à la base de donnée établie";
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public void createCourrierTable() throws SQLException {
		ResultSet rs = metadata.getTables(null, null, "COURRIER", null);

		if (!rs.next()) {
			String sql = "CREATE TABLE Courrier " + "(Name VARCHAR(255), " + " SendDate VARCHAR(255), "
					+ " ReceivedNumberDB VARCHAR(255), " + " ReceivedDateDB VARCHAR(255), "
					+ " SenderName VARCHAR(255), " + " ReceivedDateSFE VARCHAR(255), " + " Priority VARCHAR(255), "
					+ " Delay VARCHAR(255), " + " State VARCHAR(255), " + " Position VARCHAR(255), "
					+ " PRIMARY KEY ( Name ))";

			statement.executeUpdate(sql);
			System.out.println("Table Courrier Created now");
			Utils.showAlert(AlertType.INFORMATION, "Initialisation", "Installation des utilitaires", "Succès");
		} else {
			System.out.println("Table Courrier already Created");
		}

	}

	public void createNotificationsTable() throws SQLException {
		ResultSet rs = metadata.getTables(null, null, "NOTIFICATION", null);

		if (!rs.next()) {
			String sql = "CREATE TABLE NOTIFICATION " + " (PostDate VARCHAR(255), " + " Name VARCHAR(255), "
					+ " Description VARCHAR(255))";

			statement.executeUpdate(sql);
			System.out.println("Table NOTIFICATION Created now");
			Utils.showAlert(AlertType.INFORMATION, "Initialisation", "Mise en place des outils", "Succès");
		} else {
			System.out.println("Table NOTIFICATION already Created");
		}

	}

	public boolean addNewCourrier(String name, String SendDate, String ReceivedNumberDB, String ReceivedDateDB,
			String SenderName, String ReceivedDateSFE, String Priority, String Delay, String State, String Position)
			throws SQLException {
		String inset = "INSERT INTO Courrier"
				+ "(Name, SendDate, ReceivedNumberDB, ReceivedDateDB, SenderName, ReceivedDateSFE, Priority, Delay, State, Position) VALUES"
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statementement = connection.prepareStatement(inset);
		statementement.setString(1, name);
		statementement.setString(2, SendDate);
		statementement.setString(3, ReceivedNumberDB);
		statementement.setString(4, ReceivedDateDB);
		statementement.setString(5, SenderName);
		statementement.setString(6, ReceivedDateSFE);
		statementement.setString(7, Priority);
		statementement.setString(8, Delay);
		statementement.setString(9, State);
		statementement.setString(10, Position);
		int rowsInserted = statementement.executeUpdate();

		if (rowsInserted > 0) {
			System.out.println("A new courriel was inserted successfully!");
			return true;
		} else {
			return false;
		}
	}

	public boolean addNewNotification(String date, String name, String description) throws SQLException {
		String inset = "INSERT INTO NOTIFICATION" + "(PostDate, Name, Description) VALUES" + "(?, ?, ?)";
		PreparedStatement statementement = connection.prepareStatement(inset);
		statementement.setString(1, date);
		statementement.setString(2, name);
		statementement.setString(3, description);
		int rowsInserted = statementement.executeUpdate();

		if (rowsInserted > 0) {
			System.out.println("A new notification was inserted successfully!");
			return true;
		} else {
			return false;
		}
	}

	public boolean addNewCourrier(CourrierModel courrierModel) throws SQLException {
		courrierModel.printInfos();
		return addNewCourrier(courrierModel.getName(), courrierModel.getSendDate(), courrierModel.getReceivedNumberDB(),
				courrierModel.getReceivedDateDB(), courrierModel.getSenderName(), courrierModel.getReceivedDateSFE(),
				courrierModel.getPriority(), courrierModel.getDelay(), courrierModel.getState(),
				courrierModel.getPosition());
	}

	public boolean addNewNotification(NotificationModel not) throws SQLException {
		not.printInfos();
		return addNewNotification(not.getDate(), not.getName(), not.getDescription());
	}

	public ArrayList<CourrierModel> getAllCourrierArrayList() throws SQLException {
		ArrayList<CourrierModel> courriers = new ArrayList<>();
		String sql = "SELECT *FROM Courrier";
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {
			CourrierModel courrierModel = new CourrierModel(rs.getString(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
					rs.getString(9), rs.getString(10));
			courriers.add(courrierModel);
		}
		return courriers;
	}

	public ArrayList<NotificationModel> getAllNotificationsArrayList() throws SQLException {
		ArrayList<NotificationModel> notifs = new ArrayList<>();
		String sql = "SELECT *FROM NOTIFICATION";
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {
			NotificationModel not = new NotificationModel(rs.getString(1), rs.getString(2), rs.getString(3));
			System.out.println(not.printInfos());
			notifs.add(not);
		}
		return notifs;
	}

	public ArrayList<String> getCourrierNames() throws SQLException {
		ArrayList<String> ids = new ArrayList<>();
		String sql = "SELECT *FROM Courrier";
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {
			String id = rs.getString("Name");
			ids.add(id);
		}
		return ids;
	}

	public boolean deleteCourrier(String name) throws SQLException {
		String sql1 = "DELETE FROM Courrier WHERE Name=?";

		PreparedStatement statementement1 = connection.prepareStatement(sql1);
		statementement1.setString(1, name);

		int rowsDeleted = statementement1.executeUpdate();
		if (rowsDeleted > 0) {
			return true;
		}

		return false;
	}

	public boolean updateCourrier(String name, String SendDate, String ReceivedNumberDB, String ReceivedDateDB,
			String SenderName, String ReceivedDateSFE, String Priority, String Delay, String State, String Position,
			String keyName) throws SQLException {

		String sql = "UPDATE Courrier SET Name=?, SendDate=?,"
				+ "ReceivedNumberDB=?, ReceivedDateDB=?, SenderName=?, ReceivedDateSFE=?,"
				+ " Priority=?, Delay=?, State=?, Position=? WHERE Name=?";
		System.out.println(connection.prepareStatement(sql).toString());
		PreparedStatement statementement = connection.prepareStatement(sql);

		statementement.setString(1, name);
		statementement.setString(2, SendDate);
		statementement.setString(3, ReceivedNumberDB);
		statementement.setString(4, ReceivedDateDB);
		statementement.setString(5, SenderName);
		statementement.setString(6, ReceivedDateSFE);
		statementement.setString(7, Priority);
		statementement.setString(8, Delay);
		statementement.setString(9, State);
		statementement.setString(10, Position);
		statementement.setString(11, keyName);
		int rowsUpdated = statementement.executeUpdate();

		if (rowsUpdated > 0) {
			return true;
		}

		return false;
	}

	public boolean updateCourrier(CourrierModel courrier, String keyName) throws SQLException {

		return updateCourrier(courrier.getName(), courrier.getSendDate(), courrier.getReceivedNumberDB(),
				courrier.getReceivedDateDB(), courrier.getSenderName(), courrier.getReceivedDateSFE(),
				courrier.getPriority(), courrier.getDelay(), courrier.getState(), courrier.getPosition(), keyName);
	}

	public void deleteAllNotifications() throws SQLException {
		Statement statementement = connection.createStatement();
		statementement.executeUpdate("DELETE FROM NOTIFICATION");
		System.out.println("All notids was deleted");
	}
	
	public void deleteAllCourrier() throws SQLException {
		Statement statementement = connection.createStatement();
		statementement.executeUpdate("DELETE FROM Courrier");
		System.out.println("All notids was deleted");
	}

	public void deleteNotificationsTable() throws SQLException {
		Statement statementement = connection.createStatement();
		statementement.executeUpdate("DROP TABLE NOTIFICATION");
		System.out.println("table notids was deleted");
	}
	
	public void deleteCourriersTable() throws SQLException {
		Statement statementement = connection.createStatement();
		statementement.executeUpdate("DROP TABLE COURRIER");
		System.out.println("table notids was deleted");
	}
	
	public void RESET() throws SQLException{
		deleteNotificationsTable();
		deleteCourriersTable();
	}
}
