package Vue;

import Structure.FieldNote;
import Structure.FieldNoteLatex;
import Structure.FieldNoteString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class VueFieldTypeLatex extends VueFieldType {

    @FXML
    private Label label;
    @FXML
    private TextField latex;

    private String name;
    private FieldNoteLatex fieldNote;


    public VueFieldTypeLatex(FieldNote fNote, String name) {
        fieldNote = (FieldNoteLatex)fNote;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("VueFieldTypeLatex.fxml"));
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        label.setText(name);
        latex.setText(fieldNote.getContent());

    }

    public void setField() {
        fieldNote.setContent(latex.getText());
    }
}
