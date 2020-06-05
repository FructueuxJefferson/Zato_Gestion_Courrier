/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

import important.Important;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import operations.GetDimension;

/**
 *
 * @author Perfection
 */
public class SignIn extends Parent {

    TextField id, password, mail, number;
    Text idText, passwordText, mailText, numberText;

    Text title;

    Text noAccount;

    Button inscription, back;

    Text info;

    public SignIn() {
        Important.getRoot().setCursor(Cursor.DEFAULT);

        idText = new Text("Nom complet");
        passwordText = new Text("Mot de passe");
        mailText = new Text("Mail");
        numberText = new Text("Numéro");
        info = new Text("");

        idText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        passwordText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        mailText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        numberText.setFont(Font.font(GetDimension.getDynamicWidth(30)));
        info.setFont(Font.font(GetDimension.getDynamicWidth(20)));

        id = new TextField();
        password = new TextField();
        id.setPrefSize(GetDimension.getDynamicWidth(300), GetDimension.getDynamicHeight(50));
        password.setPrefSize(GetDimension.getDynamicWidth(300), GetDimension.getDynamicHeight(50));
        mail = new TextField();
        number = new TextField() {
            @Override
            public void replaceText(int start, int end, String text) {
                if (text.matches("[+|0-9]*")) {
                    super.replaceText(start, end, text);
                }
            }

            @Override
            public void replaceSelection(String text) {
                if (text.matches("[0-9]*")) {
                    super.replaceSelection(text);
                }
            }
        };
        mail.setPrefSize(GetDimension.getDynamicWidth(300), GetDimension.getDynamicHeight(50));
        number.setPrefSize(GetDimension.getDynamicWidth(300), GetDimension.getDynamicHeight(50));

        id.setPromptText("Nom complet");
        password.setPromptText("Mot de passe");
        id.setFont(Font.font(GetDimension.getDynamicWidth(15)));
        password.setFont(Font.font(GetDimension.getDynamicWidth(15)));
        mail.setPromptText("Mail");
        number.setPromptText("Numéro");
        mail.setFont(Font.font(GetDimension.getDynamicWidth(15)));
        number.setFont(Font.font(GetDimension.getDynamicWidth(15)));

        title = new Text("Gestion du Courier\n      (Inscription)");
        title.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(50)));
        //title.setUnderline(true);
        title.setFill(Color.BLACK);

        noAccount = new Text("Pas de compte ?");
        noAccount.setUnderline(true);
        noAccount.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(15)));

        inscription = new Button("Inscription");
        inscription.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));
        back = new Button("Retour");
        back.setFont(Font.font("Arial", FontWeight.BLACK, GetDimension.getDynamicWidth(20)));

        Listeiners();
    }

    public void draw() {
        PlaceTO(idText, 700, 400);
        PlaceTO(passwordText, 700, 500);
        PlaceTO(mailText, 700, 600);
        PlaceTO(numberText, 700, 700);

        PlaceTO(id, 900, 370);
        PlaceTO(password, 900, 470);
        PlaceTO(mail, 900, 570);
        PlaceTO(number, 900, 670);

        PlaceTO(title, 700, 100);

        //PlaceTO(noAccount, 700, 750);
        PlaceTO(inscription, 950, 750);
        PlaceTO(back, 1110, 750);

        PlaceTO(info, 900, 825);
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
        back.setOnMouseClicked(event -> {
            clear();
            LogIn logIn = null;
            try {
                logIn = new LogIn();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SignIn.class.getName()).log(Level.SEVERE, null, ex);
            }
            logIn.draw();
            this.getChildren().add(logIn);
        });

        inscription.setOnMouseClicked(event -> {
            if (!id.getText().isEmpty()) {
                if (!password.getText().isEmpty()) {
                    if (isValidEmailAddress(mail.getText())) {
                        if (isValidPhoneNumber(number.getText())) {
                            clear();
                            AccountData.setFullName(id.getText());
                            AccountData.setPassword(password.getText());
                            AccountData.setMail(mail.getText());
                            AccountData.setNumber(number.getText());
                            Confirmation confirmation = new Confirmation();
                            confirmation.draw();
                            this.getChildren().add(confirmation);
                        } else {
                            setError("Numéro de téléphone invalide");
                        }
                    } else {
                        setError("Adresse mail non valide");
                    }
                } else {
                    setError("Les mots de passe nuls ne sont pas valides");
                }
            } else {
                setError("Les noms nuls ne sont pas valides");
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public void setError(String txt) {
        info.setText(txt);
        info.setFill(Color.RED);
    }
}
