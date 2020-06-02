/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

import board.Board;
import database.Database;
import important.Important;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import operations.DataOperations;
import operations.GetDimension;
import utils.Utils;

/**
 *
 * @author Perfection
 */
public class LogIn extends Parent {

    TextField id, password;
    Text idText, passwordText;

    Text title;

    Text noAccount;

    Button connection, quit;

    Text info;

    List<String> accountData;

    Database database = new Database();

    public LogIn() throws IOException, FileNotFoundException, ClassNotFoundException {
        Important.getRoot().setCursor(Cursor.DEFAULT);

        idText = new Text("Identifiant");
        passwordText = new Text("Mot de passe");
        info = new Text("");

        idText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        passwordText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        info.setFont(Font.font(GetDimension.getDynamicWidth(20)));

        id = new TextField();
        password = new TextField();
        id.setPrefSize(GetDimension.getDynamicWidth(300), GetDimension.getDynamicHeight(50));
        password.setPrefSize(GetDimension.getDynamicWidth(300), GetDimension.getDynamicHeight(50));

        id.setPromptText("Identifiant");
        password.setPromptText("Mot de passe");
        id.setFont(Font.font(GetDimension.getDynamicWidth(15)));
        password.setFont(Font.font(GetDimension.getDynamicWidth(15)));

        title = new Text("Gestion du Courier\n      (Connexion)");
        title.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(50)));
        //title.setUnderline(true);
        title.setFill(Color.BLACK);

        noAccount = new Text("Pas de compte ?");
        noAccount.setUnderline(true);
        noAccount.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(15)));

        connection = new Button("Connexion");
        connection.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));
        quit = new Button("Quitter");
        quit.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));

        getAccountData();

        Listeiners();

    }

    public void draw() {
        PlaceTO(idText, 700, 450);
        PlaceTO(passwordText, 700, 550);

        PlaceTO(id, 900, 415);
        PlaceTO(password, 900, 515);

        PlaceTO(title, 700, 100);

        PlaceTO(noAccount, 700, 600);

        PlaceTO(connection, 950, 600);
        PlaceTO(quit, 1107, 600);

        PlaceTO(info, 900, 675);
    }

    public void PlaceTO(Node node, double x, double y) {
        this.getChildren().add(node);
        node.setTranslateX(GetDimension.getDynamicWidth(x));
        node.setTranslateY(GetDimension.getDynamicHeight(y));
    }

    public void clear() {
        this.getChildren().clear();
    }

    public void Listeiners() {
        noAccount.setOnMouseEntered(event -> {
            Important.getRoot().setCursor(Cursor.HAND);
        });
        noAccount.setOnMouseExited(event -> {
            Important.getRoot().setCursor(Cursor.DEFAULT);
        });
        noAccount.setOnMouseClicked(event -> {
            clear();
            SignIn signIn = new SignIn();
            signIn.draw();
            this.getChildren().add(signIn);
        });

        connection.setOnMouseClicked(event -> {
            if (new File(Utils.JarPath()+"/data/account.data").exists()) {
                if (id.getText().equals(AccountData.getId())) {
                    if (password.getText().equals(AccountData.getPassword())) {
                        info.setFill(Color.GREEN);
                        info.setText("Connection to the account done\n Connection to the database");

                        try {
                            boolean connectedToDatabase = database.connect("embadded");
                            if (connectedToDatabase) {
                                info.setText("Connection to the database done");
                                database.initialize();
                                clear();
                                Board board = new Board(database);
                                board.draw();

                                this.getChildren().add(board);
                            } else {
                                info.setText("Connection to the database failed");
                            }
                        } catch (IOException | ClassNotFoundException | SQLException ex) {
                            Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        info.setFill(Color.RED);
                        info.setText("Connection to the account failed\nPassword invalid");
                    }
                } else {
                    info.setFill(Color.RED);
                    info.setText("Connection to the account failed\nIdentifiant invalid");
                }
            } else {
                info.setFill(Color.RED);
                info.setText("Create your account first");
            }
        });

        quit.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }

    public void getAccountData() throws IOException, FileNotFoundException, ClassNotFoundException {
        if (new File("data/account.data").exists()) {
            accountData = new DataOperations().Read("data/account.data");

            AccountData.setId(accountData.get(0));
            AccountData.setFullName(accountData.get(1));
            AccountData.setPassword(accountData.get(2));
            AccountData.setMail(accountData.get(3));
            AccountData.setNumber(accountData.get(4));

            info.setFill(Color.GREEN);
            info.setText("Account found");
        } else {
            info.setFill(Color.RED);
            info.setText("Account not found");
        }
    }
}
