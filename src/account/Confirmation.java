/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

import important.Important;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
public class Confirmation extends Parent {

    Text codeText, hintText;

    Text title;

    Button confirm, back;

    Text info;

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789#@_-&";

    public Confirmation() {
        Important.getRoot().setCursor(Cursor.DEFAULT);

        codeText = new Text(randomAlphaNumeric(10));
        hintText = new Text("Ceci est votre identifiant de connexion\nVeuillez ne pas le perdre ou vous perdrez la connexion à votre compte");
        info = new Text("");

        codeText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        hintText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        info.setFont(Font.font(GetDimension.getDynamicWidth(20)));

        title = new Text("Gestion du Courier\n     (Confirmation)");
        title.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(50)));
        //title.setUnderline(true);
        title.setFill(Color.BLACK);

        confirm = new Button("Connexion");
        confirm.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));
        back = new Button("Retour");
        back.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));

        Listeiners();
        hintText.setTextAlignment(TextAlignment.CENTER);
        codeText.setFill(Color.GREEN);
        hintText.setFill(Color.RED);
    }

    public void draw() {
        PlaceTO(codeText, 850, 450);
        PlaceTO(hintText, 450, 550);

        PlaceTO(title, 700, 100);

        PlaceTO(confirm, 675, 700);
        PlaceTO(back, 1075, 700);

        PlaceTO(info, 900, 675);
    }

    public void PlaceTO(Node node, double x, double y) {
        this.getChildren().add(node);
        node.setTranslateX(GetDimension.getDynamicWidth(x));
        node.setTranslateY(GetDimension.getDynamicHeight(y));
    }

    public String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public void clear() {
        this.getChildren().clear();
    }

    public void Listeiners() {
        back.setOnMouseClicked(event -> {
            clear();
            SignIn signIn = new SignIn();
            signIn.draw();
            this.getChildren().add(signIn);
        });

        confirm.setOnMouseClicked(event -> {
            List<String> accountData = new ArrayList<>();
            accountData.add(codeText.getText());
            accountData.add(AccountData.getFullName());
            accountData.add(AccountData.getPassword());
            accountData.add(AccountData.getMail());
            accountData.add(AccountData.getNumber());
            AccountData.setId(codeText.getText());
            //System.out.println(AccountData.getFullName()+"\t"+AccountData.getPassword()+"\t"+AccountData.getMail()+"\t"+AccountData.getNumber());
            try {
                new DataOperations().Write(Utils.JarPath()+"/data/account.data", accountData);

                clear();
                LogIn logIn = new LogIn();
                logIn.draw();
                
                this.getChildren().add(logIn);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Confirmation.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
