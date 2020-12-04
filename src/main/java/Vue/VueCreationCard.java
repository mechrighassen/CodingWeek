package Vue;

import Structure.FieldNote;
import Structure.FieldNoteType;
import Structure.Note;
import Structure.NoteType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VueCreationCard extends Parent {
    @FXML
    private VBox box;

    @FXML
    private Button submit;

    @FXML
    private TextField nameNote;

    @FXML
    private TextField tagsField;


    private NoteType TypeNote;
    private Note note;
    private int id;
    private URL u;
    private ArrayList<VueFieldType> vueFieldTypes;
    private Stage stage;
    public VueCreationCard(URL u, NoteType note, int idLibre, Stage stage){
        this.TypeNote=note;
        this.id=idLibre;
        this.u=u;
        this.stage=stage;
        vueFieldTypes=new ArrayList<VueFieldType>();
        FXMLLoader fxmlLoader = new FXMLLoader(u);
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public VueCreationCard(URL u, Note note, Stage stage) {
        this.note=note;
        this.TypeNote = note.getNoteType();
        this.u=u;
        this.stage=stage;
        vueFieldTypes=new ArrayList<VueFieldType>();
        FXMLLoader fxmlLoader = new FXMLLoader(u);
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize(){

        if (note == null) this.fillForNew();
        else this.fillForEdit();
    }




    private void fillForNew(){
        note=new Note(id,TypeNote);
        for (FieldNoteType i : TypeNote.getFields()){
            FieldNote fNote = i.createEmptyField(note.getIdNote());
            note.addField(fNote);
            VueFieldType vueFieldType=i.getBuilder().build(fNote);
            vueFieldTypes.add(vueFieldType);
            box.getChildren().add( vueFieldType);
        }
    }

    private void fillForEdit() {
        this.nameNote.setText(note.getName());
        this.tagsField.setText(Builder.TagsBuilder.tagsToString(note));
        for (FieldNoteType i : TypeNote.getFields()) {
            FieldNote fNote = note.getField(i.getIdField());
            VueFieldType vueFieldType = i.getBuilder().build(fNote);
            vueFieldTypes.add(vueFieldType);
            box.getChildren().add(vueFieldType);
        }
    }

    public void closeButton(){
        stage.close();
    }

    public Button getSubmit(){
        return submit;
    }

    public List<VueFieldType> getVueFieldTypeList(){
        return vueFieldTypes;
    }

    public Note getNote(){
        return note;
    }

    public String getNoteName() {
        return nameNote.getText();
    }

    public TextField getTagsField(){
        return tagsField;
    }
}

