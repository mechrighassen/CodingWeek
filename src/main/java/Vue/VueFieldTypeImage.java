package Vue;

import Structure.FieldNote;
import Structure.FieldNoteImage;
import Structure.FieldNoteString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class VueFieldTypeImage extends VueFieldType {

    @FXML
    private ImageView imageview;

    @FXML
    private TextField text;

    @FXML
    private Label label;


    private String name;
    private FieldNoteImage fieldNote;


    public VueFieldTypeImage(FieldNote fNote, String name) {
        fieldNote = (FieldNoteImage) fNote;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("VueFieldTypeImage.fxml"));
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        label.setText(name);
        text.setText(fieldNote.getContent());
        imageview.setImage(fieldNote.getImage());
        text.setOnAction(e->  {
            setField();
            imageview.setImage(fieldNote.getImage());
        });
    }

    public void setField() {
        fieldNote.setImage(text.getText());
    }
}
