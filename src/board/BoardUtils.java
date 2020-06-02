/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import java.time.LocalDate;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import operations.GetDimension;

/**
 *
 * @author Perfection
 */
public class BoardUtils {

    public static Node AddCourrielLineWithTextField(String label, String textFieldHint, float textSize, float tfWidth, float thHeight, float distance, StringProperty textToModify) {
        HBox hb = new HBox(GetDimension.getDynamicWidth(distance));
        Text text = new Text(label);
        text.setFont(Font.font(GetDimension.getDynamicWidth(textSize)));
        TextField tf = new TextField();
        tf.setPrefSize(GetDimension.getDynamicWidth(tfWidth), GetDimension.getDynamicWidth(thHeight));
        tf.setFont(Font.font(GetDimension.getDynamicWidth(textSize)));
        tf.setText(textFieldHint);
        textToModify.setValue(textFieldHint);
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            textToModify.setValue(newValue);
        });

        hb.getChildren().addAll(text, tf);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPadding(new Insets(GetDimension.getDynamicWidth(15)));

        StackPane centeredPane = new StackPane();
        //centeredPane.setStyle("-fx-border-color: red");
        // centeredPane.setPrefSize(addCourrierPane.getMaxWidth() / 2, addCourrierPane.getMaxHeight() / 2);
        centeredPane.getChildren().add(hb);

        GridPane outerPane = new GridPane();
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        row.setFillHeight(false);
        row.setValignment(VPos.CENTER);
        outerPane.getRowConstraints().add(row);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        col.setFillWidth(false);
        col.setHalignment(HPos.CENTER);
        outerPane.getColumnConstraints().add(col);

        outerPane.add(centeredPane, 0, 0);

        return hb;
    }

    public static Node AddCourrielLineWithDate(String label, String value, float textSize, float dateWidth, float dateHeight, float distance, StringProperty textToModify) {
        HBox hb = new HBox(GetDimension.getDynamicWidth(distance));
        Text text = new Text(label);
        text.setFont(Font.font(textSize));
        DatePicker date = new DatePicker();
        if(!value.isEmpty()) {
        	date.setValue(LocalDate.parse(value));
        }else {
        date.setValue(LocalDate.now());
        }
        textToModify.set(date.getValue().toString());
        date.setPrefSize(GetDimension.getDynamicWidth(dateWidth), GetDimension.getDynamicHeight(dateHeight));
        date.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate d = newValue;
            textToModify.set("" + d);
        });

        hb.getChildren().addAll(text, date);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPadding(new Insets(GetDimension.getDynamicWidth(15)));

        StackPane centeredPane = new StackPane();
        //centeredPane.setStyle("-fx-border-color: red");
        // centeredPane.setPrefSize(addCourrierPane.getMaxWidth() / 2, addCourrierPane.getMaxHeight() / 2);
        centeredPane.getChildren().add(hb);

        GridPane outerPane = new GridPane();
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        row.setFillHeight(false);
        row.setValignment(VPos.CENTER);
        outerPane.getRowConstraints().add(row);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        col.setFillWidth(false);
        col.setHalignment(HPos.CENTER);
        outerPane.getColumnConstraints().add(col);

        outerPane.add(centeredPane, 0, 0);

        return hb;
    }

    public static Node AddCourrielLineWithComboBox(String[] items, String value, String label, String textFieldHint, float textSize, float cbWidth, float cbHeight, float distance, StringProperty textToModify) {
        HBox hb = new HBox(GetDimension.getDynamicWidth(distance));
        Text text = new Text(label);
        text.setFont(Font.font(GetDimension.getDynamicWidth(textSize)));
        ComboBox<String> cb = new ComboBox<>();
        for (int i = 0; i < items.length; ++i) {
            cb.getItems().add(items[i]);
        }
        cb.setPromptText(items[0]);
        textToModify.set(items[0]);
        cb.setPrefSize(GetDimension.getDynamicWidth(cbWidth), GetDimension.getDynamicHeight(cbHeight));
        cb.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                textToModify.set(t1);
            }
        });
        if(!value.isEmpty()) {
        	cb.setValue(value);
        }
        hb.getChildren().addAll(text, cb);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPadding(new Insets(GetDimension.getDynamicWidth(15)));

        StackPane centeredPane = new StackPane();
        //centeredPane.setStyle("-fx-border-color: red");
        // centeredPane.setPrefSize(addCourrierPane.getMaxWidth() / 2, addCourrierPane.getMaxHeight() / 2);
        centeredPane.getChildren().add(hb);

        GridPane outerPane = new GridPane();
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        row.setFillHeight(false);
        row.setValignment(VPos.CENTER);
        outerPane.getRowConstraints().add(row);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        col.setFillWidth(false);
        col.setHalignment(HPos.CENTER);
        outerPane.getColumnConstraints().add(col);

        outerPane.add(centeredPane, 0, 0);

        return hb;
    }

    public static ScrollPane GetBoardScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMinWidth(GetDimension.window.width - 100);
        scrollPane.setMaxWidth(GetDimension.window.width - 100);
        scrollPane.setMinHeight(GetDimension.window.height - 200);
        //scrollPane.setMaxHeight(GetDimension.window.height - 200);

        return scrollPane;
    }

}
