/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import account.AccountData;
import account.LogIn;
import courriel.CourrierModel;
import database.Database;
import important.Important;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.DocumentException;

import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import operations.DataOperations;
import operations.GetDimension;
import utils.Utils;
import notification.NotificationModel;

/**
 *
 * @author Perfection
 */
public class Board extends Parent {

	Text title;

	List<String> accountData;

	Button deconnection;

	TabPane tabPane;
	ScrollPane acceuiScrollPane, addCourrierPane, gestionnairePane, notificationPane, modificationPane;

	Database database;

	CourrierModel courrierModelToSave = new CourrierModel("", "", "", "", "", "", "", "", "", "");;
	TableView<CourrierModel> courriels;
	TableView<NotificationModel> notifications;
	ListView<String> options;
	ObservableList<String> optionsList;

	ArrayList<CourrierModel> courriers;
	ArrayList<NotificationModel> notifs;

	Tab logOutTab;
	Tab notificationTab;

	public Board(Database database) throws IOException, FileNotFoundException, ClassNotFoundException, SQLException {
		this.database = database;
		courriers = database.getAllCourrierArrayList();
		notifs = database.getAllNotificationsArrayList();

		optionsList = FXCollections.observableArrayList();
		optionsList.addAll("Détails", "Modifier", "Supprimer");

		deconnection = new Button("Déconnexion");

		deconnection.setOnMouseClicked(event -> {
			clear();
			LogIn logIn;
			try {
				logIn = new LogIn();

				logIn.draw();

				this.getChildren().add(logIn);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		GetAccountData();

		title = new Text("Bienvenu " + accountData.get(1));
		title.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(50)));
		// title.setUnderline(true);
		title.setFill(Color.BLACK);

		tabPane = new TabPane();
		tabPane.setPrefSize(GetDimension.window.width - 100, GetDimension.window.height - 200);
		tabPane.setTabMinWidth((GetDimension.window.width - 200) / 4);
		tabPane.setTabMaxWidth((GetDimension.window.width - 200) / 4);

		acceuiScrollPane = BoardUtils.GetBoardScrollPane();
		addCourrierPane = BoardUtils.GetBoardScrollPane();
		gestionnairePane = BoardUtils.GetBoardScrollPane();
		notificationPane = BoardUtils.GetBoardScrollPane();
		modificationPane = BoardUtils.GetBoardScrollPane();

		Tab acceuilTab = new Tab("Catalogue");
		acceuilTab.setClosable(false);
		Tab addCourrielTab = new Tab("Ajouter Courrier");
		addCourrielTab.setClosable(false);
		notificationTab = new Tab("Notification");
		notificationTab.setClosable(false);
		logOutTab = new Tab("Modification");
		logOutTab.setClosable(false);

		tabPane.getTabs().addAll(acceuilTab, addCourrielTab, notificationTab, logOutTab);

		acceuilTab.setContent(acceuiScrollPane);
		acceuiScrollPane.setContent(Acceuil());
		addCourrielTab.setContent(addCourrierPane);
		addCourrierPane.setContent(AddCourrier());
		notificationPane.setContent(Notifications());
		notificationTab.setContent(notificationPane);

		logOutTab.setContent(modificationPane);

		logOutTab.setDisable(true);

		// AddNotifications();

		tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			int selectedIndex = newValue.intValue();

			if (selectedIndex != tabPane.getTabs().indexOf(logOutTab)) {
				logOutTab.setDisable(true);
			}
			if (selectedIndex == tabPane.getTabs().indexOf(acceuilTab)) {
				try {
					RefreshAcceuilPane();
				} catch (SQLException ex) {
					Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});

		TriggerNotifications();

		DEVMODE();

		// database.deleteNotificationsTable();
	}

	public void draw() {
		PlaceTO(title, 700, 100);
		PlaceTO(deconnection, 1760, 100);

		PlaceTO(tabPane, 50, 150);
	}

	public void PlaceTO(Node node, double x, double y) {
		this.getChildren().add(node);
		node.setTranslateX(GetDimension.getDynamicWidth(x));
		node.setTranslateY(GetDimension.getDynamicHeight(y));
	}

	public void clear() {
		this.getChildren().clear();
	}

	public void Listeiner() {

	}

	public void GetAccountData() throws IOException, FileNotFoundException, ClassNotFoundException {
		if (new File("data/account.data").exists()) {
			accountData = new DataOperations().Read("data/account.data");

			AccountData.setId(accountData.get(0));
			AccountData.setFullName(accountData.get(1));
			AccountData.setPassword(accountData.get(2));
			AccountData.setMail(accountData.get(3));
			AccountData.setNumber(accountData.get(4));

			/*
			 * info.setFill(Color.GREEN); info.setText("Account found");
			 */
		} else {
			/*
			 * info.setFill(Color.RED); info.setText("Account not found");
			 */
		}
	}

	public VBox Acceuil() throws SQLException {
		VBox vb = new VBox(GetDimension.getDynamicHeight(5));
		courriels = new TableView<CourrierModel>();

		courriels.setOnMouseClicked((event) -> {
			if (options != null) {
				this.getChildren().remove(options);
			}
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				int index = courriels.getSelectionModel().getSelectedIndex();
				CourrierModel courrier = courriels.getItems().get(index);

				options = new ListView<>(optionsList);

				ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), options);
				scaleTransition.setFromX(1);
				scaleTransition.setFromY(0);
				scaleTransition.setToX(1);
				scaleTransition.setToY(1);
				scaleTransition.play();
				scaleTransition.setOnFinished((javafx.event.ActionEvent actionEvent) -> {
					scaleTransition.stop();
				});
				options.setEffect(new DropShadow());

				options.setPrefSize(125, 74);
				options.setItems(optionsList);
				options.setOnMouseEntered(event1 -> {
					options.setStyle("-fx-background-color: grey");
				});
				options.setOnMouseExited(event1 -> {
					options.setStyle("-fx-background-color: white");
				});
				options.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if (newValue.equals("Détails")) {
							Remove(options);
							Utils.showAlert(AlertType.INFORMATION, "Détails", "Courrier", courrier.printInfos());
						} else if (newValue.equals("Modifier")) {
							Remove(options);
							try {
								modificationPane.setContent(UpdateCourrier(courrier));
								modificationPane.setFitToWidth(true);
								modificationPane.setFitToHeight(true);
								logOutTab.setDisable(false);
								tabPane.getSelectionModel().select(3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (newValue.equals("Supprimer")) {
							Remove(options);
							DeleteCourrierDialog(courrier.getName());
						}
					}
				});
				options.setTranslateX(event.getSceneX());
				options.setTranslateY(event.getSceneY());
				this.getChildren().add(options);
			}
		});

		TableColumn<CourrierModel, String> column1 = new TableColumn<>("Nom du courrier");
		column1.setCellValueFactory(new PropertyValueFactory<>("name"));
		// column1.setMinWidth(GetDimension.window.width / 10 - 10);
		column1.setPrefWidth(GetDimension.window.width / 10 - 10);
		column1.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column2 = new TableColumn<>("Date d'envoi");
		column2.setCellValueFactory(new PropertyValueFactory<>("sendDate"));
		// column2.setMinWidth(GetDimension.window.width / 10 - 10);
		column2.setPrefWidth(GetDimension.window.width / 10 - 10);
		column2.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column3 = new TableColumn<>("Num de rec DB");
		column3.setCellValueFactory(new PropertyValueFactory<>("receivedNumberDB"));
		// column3.setMinWidth(GetDimension.window.width / 10 - 10);
		column3.setPrefWidth(GetDimension.window.width / 10 - 10);
		column3.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column4 = new TableColumn<>("Date de rec");
		column4.setCellValueFactory(new PropertyValueFactory<>("receivedDateDB"));
		// column4.setMinWidth(GetDimension.window.width / 10 - 10);
		column4.setPrefWidth(GetDimension.window.width / 10 - 10);
		column4.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column5 = new TableColumn<>("Destinateur");
		column5.setCellValueFactory(new PropertyValueFactory<>("senderName"));
		// column5.setMinWidth(GetDimension.window.width / 10 - 10);
		column5.setPrefWidth(GetDimension.window.width / 10 - 10);
		column5.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column6 = new TableColumn<>("Date de rec SFE");
		column6.setCellValueFactory(new PropertyValueFactory<>("receivedDateSFE"));
		// column6.setMinWidth(GetDimension.window.width / 10 - 10);
		column6.setPrefWidth(GetDimension.window.width / 10 - 10);
		column6.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column7 = new TableColumn<>("Priorité");
		column7.setCellValueFactory(new PropertyValueFactory<>("priority"));
		column7.setPrefWidth(GetDimension.window.width / 10 - 10);
		// column7.setMinWidth(GetDimension.window.width / 10 - 10);
		column7.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column8 = new TableColumn<>("Délai");
		column8.setCellValueFactory(new PropertyValueFactory<>("delay"));
		// column8.setMinWidth(GetDimension.window.width / 10 - 10);
		column8.setPrefWidth(GetDimension.window.width / 10 - 10);
		column8.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column9 = new TableColumn<>("Etat");
		column9.setCellValueFactory(new PropertyValueFactory<>("state"));
		// column9.setMinWidth(GetDimension.window.width / 10 - 10);
		column9.setPrefWidth(GetDimension.window.width / 10 - 10);
		column9.setMaxWidth(GetDimension.window.width / 10 - 10);

		TableColumn<CourrierModel, String> column10 = new TableColumn<>("Position");
		column10.setCellValueFactory(new PropertyValueFactory<>("position"));
		// column10.setMinWidth(GetDimension.window.width / 10 - 10);
		column10.setPrefWidth(GetDimension.window.width / 10 - 10);
		column10.setMaxWidth(GetDimension.window.width / 10 - 10);

		courriels.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8, column9,
				column10);

		// courriels.getItems().add(new CourrierModel("Courrier du ministère",
		// "24/10/78", "14689", "24/10/79", "FRGT", "44/78/98", "10j", "A traiter",
		// "rgrg", "10"));
		courriels.getItems().addAll(courriers);
		System.out.println(courriers.size());

		courriels.setMinWidth(GetDimension.window.width - 100);
		courriels.setMaxWidth(GetDimension.window.width - 100);
		courriels.setMinHeight(GetDimension.window.height - 200);
		courriels.setMaxHeight(GetDimension.window.height - 200);

		courriels.setPadding(new Insets(GetDimension.getDynamicHeight(50), GetDimension.getDynamicHeight(10),
				GetDimension.getDynamicHeight(10), GetDimension.getDynamicHeight(10)));

		ComboBox<CourrierModel.CourrierPriorité> priorities = new ComboBox<>();
		priorities.getItems().add(null);
		priorities.getItems().addAll(CourrierModel.CourrierPriorité.values());
		priorities.setValue(null);
		ComboBox<CourrierModel.CourrierState> states = new ComboBox<>();
		states.getItems().add(null);
		states.getItems().addAll(CourrierModel.CourrierState.values());
		states.setValue(null);
		ComboBox<CourrierModel.CourrierPosition> positions = new ComboBox<>();
		positions.getItems().add(null);
		positions.getItems().addAll(CourrierModel.CourrierPosition.values());
		positions.setValue(null);

		ObjectProperty<Predicate<CourrierModel>> priorityFilter = new SimpleObjectProperty<>();
		priorityFilter.bind(Bindings.createObjectBinding(
				() -> courrier -> priorities.getValue() == null
						|| priorities.getValue().toString().equals(courrier.getPriority()),
				priorities.valueProperty()));

		ObjectProperty<Predicate<CourrierModel>> stateFilter = new SimpleObjectProperty<>();
		stateFilter.bind(Bindings.createObjectBinding(
				() -> courrier -> states.getValue() == null || states.getValue().toString().equals(courrier.getState()),
				states.valueProperty()));

		ObjectProperty<Predicate<CourrierModel>> positionFilter = new SimpleObjectProperty<>();
		positionFilter
				.bind(Bindings.createObjectBinding(
						() -> courrier -> positions.getValue() == null
								|| positions.getValue().toString().equals(courrier.getPosition()),
						positions.valueProperty()));

		FilteredList<CourrierModel> filteredItems = new FilteredList<>(FXCollections.observableList(courriers));
		courriels.setItems(filteredItems);

		filteredItems.predicateProperty()
				.bind(Bindings.createObjectBinding(
						() -> priorityFilter.get().and(stateFilter.get()).and(positionFilter.get()), priorityFilter,
						stateFilter, positionFilter));

		Button clear = new Button("Clear Filters");
		clear.setOnAction(e -> {
			priorities.setValue(null);
			states.setValue(null);
			positions.setValue(null);
		});

		Button save = new Button("Sauvegarder");
		save.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog("Courriers.pdf");
			dialog.setTitle("Opération");
			dialog.setHeaderText("Sauvegarde");
			dialog.setContentText("Entrez le nom de votre ficher");

			Optional<String> result = dialog.showAndWait();
			
			if (result != null) {
				String file = result.get();
				if(!file.endsWith(".pdf")) {
					file.concat(".pdf");
				}
				if (new File(Utils.JarPath()+"/courriers/" + file).exists()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Confirmation");
					alert.setHeaderText("Fichier existant !!!");
					alert.setContentText("Continuer ?");

					ButtonType yes = new ButtonType("Oui", ButtonData.APPLY);
					ButtonType no = new ButtonType("Non", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(yes, no);

					Optional<ButtonType> resultBtn = alert.showAndWait();
					if (resultBtn.get() == yes) {
						alert.close();

						String saveMessage = "Liste des Courriers\n\n\n\n";
						int num = 1;
						for (CourrierModel cou : courriels.getItems()) {
							String msg = cou.printInfos();
							String nums = "Courrier N°" + num;
							saveMessage += nums + "\n\n" + msg + "\n\n";
							num++;
						}

						try {
							boolean saved = Utils.SavePDF(saveMessage, file);
							if (saved) {
								Utils.showAlert(AlertType.INFORMATION, "Opération", "Sauvegarde", "Succès");
							} else {
								Utils.showAlert(AlertType.ERROR, "Opération", "Sauvegarde", "Echec");
							}
						} catch (IOException e1) {

							e1.printStackTrace();
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else if (resultBtn.get() == no) {
						alert.close();
					}
				} else {
					String saveMessage = "Liste des Courriers\n\n\n\n";
					int num = 1;
					for (CourrierModel cou : courriels.getItems()) {
						String msg = cou.printInfos();
						String nums = "Courrier N°" + num;
						saveMessage += nums + "\n\n" + msg + "\n\n";
						num++;
					}

					try {
						boolean saved = Utils.SavePDF(saveMessage, file);
						if (saved) {
							Utils.showAlert(AlertType.INFORMATION, "Opération", "Sauvegarde", "Succès");
						} else {
							Utils.showAlert(AlertType.ERROR, "Opération", "Sauvegarde", "Echec");
						}
					} catch (IOException e1) {

						e1.printStackTrace();
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}

		});

		HBox filters = new HBox(5, Utils.setLabelDescription("Prioroté", priorities),
				Utils.setLabelDescription("Etat", states), Utils.setLabelDescription("Position", positions), clear,
				save);
		filters.setPadding(new Insets(5));
		vb.getChildren().addAll(filters, courriels);

		return vb;
	}

	Node AddCourrier() throws SQLException {
		courrierModelToSave = new CourrierModel("", "", "", "", "", "", "", "", "", "");
		VBox finalNode = new VBox(GetDimension.getDynamicHeight(5));
		HBox actions = new HBox(GetDimension.getDynamicWidth(25));
		Button add, clear;
		add = new Button("Ajouter");
		add.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));
		clear = new Button("Clear");
		clear.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));
		actions.getChildren().addAll(add, clear);
		actions.setPadding(new Insets(GetDimension.getDynamicWidth(50), 0, GetDimension.getDynamicWidth(100), 0));

		finalNode.getChildren().addAll(
				BoardUtils.AddCourrielLineWithTextField("Nom", "Nouveau courrier", 30, 500, 50, 25,
						courrierModelToSave.getNameSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Send Date", "", 30, 500, 50, 25,
						courrierModelToSave.getSendDateSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithTextField("Num de réception DB", "Num de réception", 30, 500, 50, 25,
						courrierModelToSave.getReceivedNumberDBSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Rec Date DB", "", 30, 500, 50, 25,
						courrierModelToSave.getReceivedDateDBSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithTextField("Sender name", "Sender num", 30, 500, 50, 25,
						courrierModelToSave.getSenderNumSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Rec Date SFE", "", 30, 500, 50, 25,
						courrierModelToSave.getReceivedDateSFESimpleStringProperty()),
				BoardUtils.AddCourrielLineWithComboBox(new String[] { "Pas_Urgent", "Urgent" }, "", "Priorité",
						"Priorité", 30, 500, 50, 25, courrierModelToSave.getPrioriSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Délai de traitement", LocalDate.now().plusWeeks(1).toString(), 30,
						500, 50, 25, courrierModelToSave.getDelaySimpleStringProperty()),
				BoardUtils.AddCourrielLineWithComboBox(new String[] { "Non_Traité", "Traité" }, "", "State", "State",
						30, 500, 50, 25, courrierModelToSave.getStateSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithComboBox(
						new String[] { "Directeur", "Ministère", "SFE", "Secrétaire", "Ailleurs" }, "", "Position",
						"State", 30, 500, 50, 25, courrierModelToSave.getPositionSimpleStringProperty()),
				actions);

		add.setOnMouseClicked(event -> {
			try {
				if (!Utils.doesCourrierExits(courrierModelToSave.getName(), courriers)) {
					boolean addCourrier = database.addNewCourrier(courrierModelToSave);
					if (addCourrier) {
						Utils.showAlert(AlertType.INFORMATION, "Opétation",
								"Ajout du courrier <<" + courrierModelToSave.getName() + ">>", "Succès");
						RefreshAddCourrierPane();
						RefreshNotificationPane();
					} else {
						Utils.showAlert(AlertType.ERROR, "Opétation",
								"Ajout du courrier <<" + courrierModelToSave.getName() + ">>", "Echec");
					}
				} else {
					Utils.showAlert(AlertType.ERROR, "Opétation",
							"Ajout du courrier <<" + courrierModelToSave.getName() + ">>",
							"Echec!\n Ce nom de courrier existe déjà");
				}
			} catch (SQLException ex) {
				Utils.showAlert(AlertType.ERROR, "Opétation",
						"Ajout du courrier <<" + courrierModelToSave.getName() + ">>",
						"Exception !\n Ce nom de courrier existe déjà");
			}
		});
		clear.setOnMouseClicked(event -> {
			try {
				RefreshAddCourrierPane();
			} catch (SQLException ex) {
				Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
			}
		});

		finalNode.setPadding(new Insets(GetDimension.getDynamicWidth(15)));
		finalNode.setAlignment(Pos.CENTER_LEFT);
		return finalNode;
	}

	Node UpdateCourrier(CourrierModel courrier) throws SQLException {
		String keyName = courrier.getName();
		VBox finalNode = new VBox(GetDimension.getDynamicHeight(5));
		HBox actions = new HBox(GetDimension.getDynamicWidth(25));
		Button modify, clear;
		modify = new Button("Modifier");
		modify.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));
		clear = new Button("Default");
		clear.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));
		actions.getChildren().addAll(modify, clear);
		actions.setPadding(new Insets(GetDimension.getDynamicWidth(50), 0, GetDimension.getDynamicWidth(100), 0));

		finalNode.getChildren().addAll(
				BoardUtils.AddCourrielLineWithTextField("Nom", courrier.getName(), 30, 500, 50, 25,
						courrier.getNameSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Send Date", courrier.getSendDate(), 30, 500, 50, 25,
						courrier.getSendDateSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithTextField("Num de réception DB", courrier.getReceivedNumberDB(), 30, 500,
						50, 25, courrier.getReceivedNumberDBSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Rec Date DB", courrier.getReceivedDateDB(), 30, 500, 50, 25,
						courrier.getReceivedDateDBSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithTextField("Sender name", courrier.getSenderName(), 30, 500, 50, 25,
						courrier.getSenderNumSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Rec Date SFE", courrier.getReceivedDateSFE(), 30, 500, 50, 25,
						courrier.getReceivedDateSFESimpleStringProperty()),
				BoardUtils.AddCourrielLineWithComboBox(new String[] { "Pas_Urgent", "Urgent" }, courrier.getPriority(),
						"Priorité", "Priorité", 30, 500, 50, 25, courrier.getPrioriSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithDate("Délai de traitement", courrier.getDelay(), 30, 500, 50, 25,
						courrier.getDelaySimpleStringProperty()),
				BoardUtils.AddCourrielLineWithComboBox(new String[] { "Non_Traité", "Traité" }, courrier.getState(),
						"State", "State", 30, 500, 50, 25, courrier.getStateSimpleStringProperty()),
				BoardUtils.AddCourrielLineWithComboBox(
						new String[] { "Directeur", "Ministère", "SFE", "Secrétaire", "Ailleurs" },
						courrier.getPosition(), "Position", "State", 30, 500, 50, 25,
						courrier.getPositionSimpleStringProperty()),
				actions);

		modify.setOnMouseClicked(event -> {
			try {
				if ((!courrier.getName().equals(courrier.getName())
						&& !Utils.doesCourrierExits(courrier.getName(), courriers)) == false) {
					boolean updateCourrier = database.updateCourrier(courrier, keyName);
					System.out.println(!courrier.getName().equals(courrier.getName())
							&& !Utils.doesCourrierExits(courrier.getName(), courriers));

					if (updateCourrier) {

						Utils.showAlert(AlertType.INFORMATION, "Opétation",
								"Mise à jour d'un courrier <<" + courrier.getName() + ">>", "Succès");
						RefreshAddCourrierPane();
						RefreshNotificationPane();
						logOutTab.setDisable(true);
						tabPane.getSelectionModel().select(0);
					} else {
						Utils.showAlert(AlertType.ERROR, "Opétation",
								"Mise à jour d'un courrier <<" + courrier.getName() + ">>", "Echec");
					}
				} else {

				}
			} catch (SQLException ex) {
				Utils.showAlert(AlertType.ERROR, "Opétation",
						"Mise à jour d'un courrier <<" + courrier.getName() + ">>",
						"Exception !\n Ce courrier existe déjà");
			}
		});
		clear.setOnMouseClicked(event -> {
			try {
				RefreshUpdatePane(courrier);
			} catch (SQLException ex) {
				Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
			}
		});

		finalNode.setPadding(new Insets(GetDimension.getDynamicWidth(15)));
		finalNode.setAlignment(Pos.CENTER_LEFT);

		System.out.println(finalNode.toString());
		return finalNode;
	}

	void DeleteCourrierDialog(String keyName) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Supprimer ce courrier ?");
		alert.setContentText("Cette action est irréversible !!!");

		ButtonType yes = new ButtonType("Oui", ButtonData.APPLY);
		ButtonType no = new ButtonType("Non", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(yes, no);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yes) {
			alert.close();
			try {
				boolean delete = database.deleteCourrier(keyName);
				if (delete) {
					Utils.showAlert(AlertType.INFORMATION, "Opération", "Suppression de courrier <<" + keyName + ">>",
							"Succès");
					RefreshAcceuilPane();
					RefreshNotificationPane();
				}
			} catch (SQLException e) {
				Utils.showAlert(AlertType.ERROR, "Opération", "Suppression de courrier <<" + keyName + ">>", "Echec");
				e.printStackTrace();
			}
		} else if (result.get() == no) {
			alert.close();
		}
	}

	VBox Notifications() {
		VBox vb = new VBox(GetDimension.getDynamicHeight(5));
		notifications = new TableView<NotificationModel>();

		notifications.setOnMouseClicked((event) -> {
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				int index = notifications.getSelectionModel().getSelectedIndex();
				NotificationModel notif = notifications.getItems().get(index);

				Utils.showAlert(AlertType.INFORMATION, "Détails", "Notification", notif.printInfos());
			}
		});

		TableColumn<NotificationModel, String> column1 = new TableColumn<>("Date");
		column1.setCellValueFactory(new PropertyValueFactory<>("date"));
		// column1.setMinWidth(GetDimension.window.width / 10 - 10);
		column1.setPrefWidth(GetDimension.window.width / 3 - 10);
		column1.setMaxWidth(GetDimension.window.width / 3 - 10);

		TableColumn<NotificationModel, String> column2 = new TableColumn<>("Nom");
		column2.setCellValueFactory(new PropertyValueFactory<>("name"));
		// column2.setMinWidth(GetDimension.window.width / 10 - 10);
		column2.setPrefWidth(GetDimension.window.width / 3 - 10);
		column2.setMaxWidth(GetDimension.window.width / 3 - 10);

		TableColumn<NotificationModel, String> column3 = new TableColumn<>("Description");
		column3.setCellValueFactory(new PropertyValueFactory<>("description"));
		// column3.setMinWidth(GetDimension.window.width / 10 - 10);
		column3.setPrefWidth(GetDimension.window.width / 3 - 10);
		column3.setMaxWidth(GetDimension.window.width / 3 - 10);

		notifications.getColumns().addAll(column1, column2, column3);

		notifications.getItems().addAll(notifs);
		System.out.println(notifs.size());

		notifications.setMinWidth(GetDimension.window.width - 100);
		notifications.setMaxWidth(GetDimension.window.width - 100);
		notifications.setMinHeight(GetDimension.window.height - 200);
		notifications.setMaxHeight(GetDimension.window.height - 200);

		notifications.setPadding(new Insets(GetDimension.getDynamicHeight(50), GetDimension.getDynamicHeight(10),
				GetDimension.getDynamicHeight(10), GetDimension.getDynamicHeight(10)));

		vb.getChildren().addAll(notifications);

		return vb;
	}

	void TriggerNotifications() throws SQLException {
		int number = 0;
		for (CourrierModel courM : courriers) {
			if (Utils.validateJavaDate(courM.getDelay())) {
				if (Date.valueOf(LocalDate.now()).after(Date.valueOf(courM.getDelay()))) {
					if (!Utils.NotifWasShownToday(notifs, courM.getName(), LocalDate.now().toString())) {
						database.addNewNotification(LocalDate.now().toString(), courM.getName(),
								"Le délai de traitement est dépassé pour ce courrier");
						number++;
					} else {
						number++;
					}
				} else if (Date.valueOf(LocalDate.now()).compareTo(Date.valueOf(courM.getDelay())) == 0) {
					if (!Utils.NotifWasShownToday(notifs, courM.getName(), LocalDate.now().toString())) {
						database.addNewNotification(LocalDate.now().toString(), courM.getName(),
								"Le délai de traitement est atteint pour ce courrier");
						number++;
					} else {
						number++;
					}
				}
			}
		}

		notifs = database.getAllNotificationsArrayList();
		notificationTab.setText("Notification(" + number + ")");
	}

	void RefreshAcceuilPane() throws SQLException {
		if (courriels != null) {
			this.getChildren().remove(courriels);
			courriers = database.getAllCourrierArrayList();
			acceuiScrollPane.setContent(null);
			acceuiScrollPane.setContent(Acceuil());
			acceuiScrollPane.setFitToHeight(true);
			acceuiScrollPane.setFitToWidth(true);

			TriggerNotifications();
		}
	}

	void RefreshNotificationPane() throws SQLException {
		if (notifications != null) {
			this.getChildren().remove(notifications);
			notifs = database.getAllNotificationsArrayList();
			notificationPane.setContent(null);
			notificationPane.setContent(Notifications());
			notificationPane.setFitToHeight(true);
			notificationPane.setFitToWidth(true);

			TriggerNotifications();
		}
	}

	void RefreshAddCourrierPane() throws SQLException {
		courriers = database.getAllCourrierArrayList();
		addCourrierPane.setContent(null);
		addCourrierPane.setContent(AddCourrier());
		addCourrierPane.setFitToWidth(true);
		addCourrierPane.setFitToHeight(true);

		TriggerNotifications();
	}

	void RefreshUpdatePane(CourrierModel courrier) throws SQLException {
		modificationPane.setContent(null);
		modificationPane.setContent(UpdateCourrier(courrier));
		modificationPane.setFitToWidth(true);
		modificationPane.setFitToHeight(true);

		TriggerNotifications();
	}

	public void Remove(Node node) {
		this.getChildren().remove(node);
	}

	public void DEVMODE() {
		Important.getScene().setOnKeyPressed(event -> {
			if (event.isAltDown() && event.isShiftDown() && event.getCode() == KeyCode.R) {
				try {
					ShOWDEVDIALOG();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void ShOWDEVDIALOG() throws SQLException {
		List<String> choices = new ArrayList<>();
		choices.add("Courrier");
		choices.add("Notification");
		choices.add("Account");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Nothing", choices);
		dialog.setTitle("Dev Mode");
		dialog.setHeaderText("This is a dev panel to delete records");
		dialog.setContentText("Choose table");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			System.out.println("Your choice: " + result.get());
			if (result.get().equals("Courrier")) {
				database.deleteAllCourrier();
				GENERALREFRESH();
			} else if (result.get().equals("Notification")) {
				database.deleteAllNotifications();
				GENERALREFRESH();
			}
		}
	}

	public void GENERALREFRESH() throws SQLException {
		RefreshAcceuilPane();
		RefreshNotificationPane();
	}

}
