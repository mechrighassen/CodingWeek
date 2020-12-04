package Vue;


import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import javafx.scene.text.FontWeight;


public class VueFieldString extends VueField {

    public VueFieldString(String c) {
        Label label = new Label(c);
        Region r1 = new Region();
        Region r2 = new Region();
        HBox hBox = new HBox();
        label.setStyle("-fx-font-size:18px;");
        hBox.getChildren().addAll(r1, label, r2);
        HBox.setHgrow(r1, Priority.ALWAYS);
        HBox.setHgrow(r2, Priority.ALWAYS);
        hBox.setMaxWidth(Double.MAX_VALUE);
        hBox.setPrefWidth(558);
        this.getChildren().add(hBox);
    }
}
