/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import important.Important;
import java.util.Scanner;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Gestion;
import operations.GetDimension;

/**
 *
 * @author Perfection
 */
public class Frame extends Parent {

    Scene scene;
    Stage stage = new Stage();
    Group root = new Group();
    GetDimension dim = new GetDimension();

    String title;
    double width, height;
    int fps;
    Color color;

    Scanner numeric = new Scanner(System.in);

    Timeline timeline;

    Gestion engine;

    public Frame(Gestion engine) {
        this.engine = engine;
    }

    public void draw(String title, Color color) {
        width = GetDimension.getWindow().width;
        height = GetDimension.getWindow().height;

        scene = new Scene(root, width, height);

        scene.setFill(color);

        scene.setCamera(new PerspectiveCamera());
        scene.setRoot(root);

        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

        assignImportant();
    }

    public void setResizable(boolean isResizable) {
        stage.setResizable(isResizable);
    }

    public void assignImportant() {
        Important.assign(stage, scene, root, width, height);
    }

    public void setFullScreen(boolean bool) {
        stage.setFullScreen(bool);
    }

}
