/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import account.LogIn;
import frame.Frame;
import important.Important;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 *
 * @author Marilonymous
 */
public class Gestion extends Application {

    Frame frame;
    LogIn logIn;

    @Override
    public void start(Stage stage) throws Exception {
        frame = new Frame(this);

        frame.draw("Frame", Color.WHITESMOKE);
        frame.setResizable(false);
        Important.print();
        
        logIn = new LogIn();
        logIn.draw();
        
        print();
    }

    public void print() {
        Important.getRoot().getChildren().addAll(logIn);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
