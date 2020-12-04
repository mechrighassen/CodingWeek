package Vue;

import Structure.FieldNote;
import Structure.FieldNoteString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class VueFieldTypeString extends VueFieldType {

    @FXML
    private Label label;
    @FXML
    private TextField text;

    private String name;
    private FieldNoteString fieldNote;


    public VueFieldTypeString(FieldNote fNote, String name) {
        fieldNote = (FieldNoteString)fNote;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("VueFieldTypeString.fxml"));
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        label.setText(name);
        text.setText(fieldNote.getContent());

    }

    public void setField() {
        fieldNote.setContent(text.getText());
    }
}
