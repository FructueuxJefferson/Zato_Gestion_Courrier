/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package important;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Marilonymous
 */
public class Important {

    static Scene scene;
    static Stage stage;
    static Group root;
    static double width, height;

    static ArrayList<Object> list = new ArrayList<>();

    public static void assign(Stage stage, Scene scene, Group root, double width, double height) {
        Important.stage = stage;
        Important.scene = scene;
        Important.root = root;
        Important.width = width;
        Important.height = height;

        list.add(stage);
        list.add(scene);
        list.add(width);
        list.add(height);
    }

    public static void print() {
        list.forEach((obj) -> {
            System.out.println(obj.toString());
        });
    }

    public static Group getRoot() {
        return root;
    }

    public static void setRoot(Group root) {
        Important.root = root;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        Important.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        Important.stage = stage;
    }

    public static double getWidth() {
        return width;
    }

    public static void setWidth(double width) {
        Important.width = width;
    }

    public static double getHeight() {
        return height;
    }

    public static void setHeight(double height) {
        Important.height = height;
    }
}
