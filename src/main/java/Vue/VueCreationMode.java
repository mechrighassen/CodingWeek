package Vue;

import Builder.TagsBuilder;
import Gestionnaire.GestionnaireNote;
import Structure.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

//import static jdk.xml.internal.SecuritySupport.getClassLoader;

public class VueCreationMode extends Parent {
    @FXML
    private VBox boxDecks,boxTypes,boxNotes;

    @FXML
    private ListView<String> deckList;
    @FXML
    private ListView<String> typeList;
    @FXML
    private ListView<String> noteList;
    @FXML
    private ListView<String> tagList;

    @FXML
    private TabPane tabPane;

    private VueCreationCard root2;
    private GestionnaireNote gn;
    private ObservableList<String> obsNoteType = FXCollections.observableArrayList();
    private ObservableList<String> obsNote = FXCollections.observableArrayList();
    private ObservableList<Integer> obsNoteTypeId = FXCollections.observableArrayList();
    private ObservableList<Integer> obsNoteId = FXCollections.observableArrayList();
    private ObservableList<String> obsDeckId = FXCollections.observableArrayList();
    //private ObservableList<Integer> obsDeck= FXCollections.observableArrayList();
    private ObservableList<String> obsTagId=FXCollections.observableArrayList();
    private Integer currentNoteType = null;
    private String currentDeckId = null;
    private Note currentNote = null;
    private Tag currentTag=null;

    public VueCreationMode(GestionnaireNote gestNotes, URL a){
        gn=gestNotes;

        FXMLLoader fxmlLoader = new FXMLLoader(a);
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void initialize() {
        updateObsNote();
        updateObsType();
        updateObsDeck();
        updateObsTag();
        gn.getObsNotes().addListener(new MapChangeListener<Integer,Note>() {

            @Override
            public void onChanged(Change<? extends Integer, ? extends Note> change) {
                updateObsNote();
                updateObsTag();
            }
        });
        gn.getObsNotesType().addListener(new MapChangeListener<Integer,NoteType>() {

            @Override
            public void onChanged(Change<? extends Integer, ? extends NoteType> change) {
                updateObsType();
            }
        });
        gn.getObsDeck().addListener(new MapChangeListener<String, Deck>() {

            @Override
            public void onChanged(Change<? extends String, ? extends Deck> change) {
                updateObsDeck();
            }
        });

        deckList.setItems(obsDeckId);
        typeList.setItems(obsNoteType);
        noteList.setItems(obsNote);
        tagList.setItems(obsTagId);
        noteList.getSelectionModel().selectedIndexProperty().addListener(
                (old, o, n) -> {
                    if (n.intValue() >= 0) {
                        currentNote = gn.getNote(obsNoteId.get(n.intValue()));
                    }

                }
        );

        typeList.getSelectionModel().selectedIndexProperty().addListener(
                (old, o, n) -> {
                    if (n.intValue() >= 0) {
                        currentNoteType = obsNoteTypeId.get(n.intValue());
                        updateObsNote();
                    }
                    else {
                        currentNoteType=null;
                    }

                }
        );

        deckList.getSelectionModel().selectedIndexProperty().addListener(
                (old, o, n) -> {
                    if (n.intValue() >= 0) {
                        currentDeckId= obsDeckId.get(n.intValue());
                        updateObsNote();
                    }
                    else {
                        currentDeckId=null;
                    }
                }
        );
        tagList.getSelectionModel().selectedIndexProperty().addListener(
                (old,o,n)->{
                    if(n.intValue()>=0) {
                        String s1 = obsTagId.get(n.intValue());
                        currentTag = gn.getTags().get(s1);
                        updateObsNote();
                    }
                    else{
                        currentTag=null;
                    }



                }
        );
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
                (old,o,n)->{
                    deckList.getSelectionModel().clearSelection();
                    tagList.getSelectionModel().clearSelection();
                    noteList.getSelectionModel().clearSelection();
                    typeList.getSelectionModel().clearSelection();
                    currentDeckId=null;
                    currentNote=null;
                    currentTag=null;
                    currentNoteType=null;
                    obsNote.clear();
                }
        );

    }

    public void clearSelection() {
        noteList.getSelectionModel().clearSelection();
        deckList.getSelectionModel().clearSelection();
        typeList.getSelectionModel().clearSelection();
        tagList.getSelectionModel().clearSelection();
    }

    private void updateObsType() {
        obsNoteType.clear();
        obsNoteTypeId.clear();
        for (NoteType n : this.gn.getObsNotesType().values()) {
            obsNoteType.add(n.getName());
            obsNoteTypeId.add(n.getIdNoteType());
        }
    }

    private void updateObsNote() {
        currentNote=null;
        this.noteList.getSelectionModel().clearSelection();
        obsNote.clear();
        obsNoteId.clear();
        String s = tabPane.getSelectionModel().getSelectedItem().getText();
        if (s.equals("Deck")) {
            if (currentNoteType != null && currentDeckId != null) {
                for (Note n : this.gn.getNoteByTypesAndDeck(gn.getObsDeck().get(currentDeckId), currentNoteType)) {
                    obsNote.add(n.getName() + " : " + Math.round(n.getEffectiveKnowledgeRate()));
                    obsNoteId.add(n.getIdNote());

                }
            }
        } else {
            if (currentNoteType != null && currentTag != null) {
                for (Note note : currentTag.getNotes()) {
                    obsNote.add(note.getName());
                }
            }
        }
    }

    private void updateObsDeck() {
        obsDeckId.clear();
        for (Deck n : this.gn.getObsDeck().values()) {
            obsDeckId.add(n.getName());
        }
    }

    private void updateObsTag(){
        obsTagId.clear();
        for (Tag t : gn.getTags().values()){
            obsTagId.add(t.getName());
        }
    }


    public void appuiNew(){

        Stage stage=new Stage();
        Parent root = new VueNormalDeckCreate(getClass().getClassLoader().getResource("NormalDeck.fxml"),stage,gn);

        stage.setScene(new Scene(root, 400, 185 ));
        stage.setMaxHeight(185);
        stage.setMaxWidth(400);
        stage.setMinHeight(185);
        stage.setMinWidth(400);
        stage.show();
    }

    public void newCard() {

        if (currentNoteType != null && currentDeckId != null && gn.getObsDeck().get(currentDeckId) instanceof NormalDeck) {
            Stage stage = new Stage();

            root2 = new VueCreationCard(getClass().getClassLoader().getResource("CreationCard.fxml"), gn.getNoteType(currentNoteType), gn.getFreeID("note"),stage);
            Button submit = root2.getSubmit();

            submit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    TextField tagsField=root2.getTagsField();
                    List<VueFieldType> vueFieldTypeList = root2.getVueFieldTypeList();
                    for (VueFieldType i : vueFieldTypeList) {
                        i.setField();
                    }
                    Note note = root2.getNote();
                    note.setNameDeck(currentDeckId);
                    note.setName(root2.getNoteName());
                    String tagsFieldString=tagsField.getText();
                    try {
                        gn.removeTagFromNote(note);
                        TagsBuilder.parseTags(tagsFieldString,note,gn);
                        updateObsTag();
                        gn.insertUpdateNote(note);
                        gn.addNote(note);
                    }
                    catch (SQLException sqlE) {
                        System.out.println(sqlE.getMessage());
                    }
                    stage.close();
                }
            });

            stage.setScene(new Scene(root2, 500, 405));
            stage.setMaxHeight(405);
            stage.setMaxWidth(500);
            stage.setMinHeight(405);
            stage.setMinWidth(500);
            stage.show();
        }
    }

    public void editCard()  {

        if (currentNoteType != null && currentDeckId != null && currentNote != null) {
            Stage stage = new Stage();

            root2 = new VueCreationCard(getClass().getClassLoader().getResource("CreationCard.fxml"), currentNote,stage);
            Button submit = root2.getSubmit();
            submit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    TextField tagsField=root2.getTagsField();
                    List<VueFieldType> vueFieldTypeList = root2.getVueFieldTypeList();
                    for (VueFieldType i : vueFieldTypeList) {
                        i.setField();
                    }
                    Note note = root2.getNote();
                    note.setName(root2.getNoteName());
                    String tagsFieldString=tagsField.getText();
                    try {
                        gn.removeTagFromNote(note);
                        TagsBuilder.parseTags(tagsFieldString,note,gn);
                        updateObsTag();
                        updateObsNote();
                        gn.insertUpdateNote(note);
                        gn.addNote(note);
                        updateObsTag();
                    }
                    catch (SQLException sqlE) {
                        System.out.println(sqlE.getMessage());
                    }
                    stage.close();
                }
            });

            stage.setScene(new Scene(root2, 500, 405));
            stage.setMaxHeight(405);
            stage.setMaxWidth(500);
            stage.setMinHeight(405);
            stage.setMinWidth(500);
            stage.show();
        }
    }

    public void createNoteType(){
        Stage stage=new Stage();
        Parent root = new VueCreationNoteType(stage,gn);
        stage.setScene(new Scene(root, 655, 560));
        stage.setMaxHeight(560);
        stage.setMaxWidth(655);
        stage.setMinHeight(560);
        stage.setMinWidth(655);
        stage.show();
    }

    @FXML
    public void deleteNote() {
        if (currentNote != null) {
            try {
                gn.removeNote(currentNote);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification ");
                alert.setHeaderText(null);
                alert.setContentText("Current note has been deleted");
            }
            catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
    @FXML
    public void deleteNoteType(){
        if (currentNoteType != null) {
            try {
                gn.removeNoteType(gn.getNoteType(currentNoteType));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification ");
                alert.setHeaderText(null);
                alert.setContentText("Current note type has been deleted");
            }
            catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
    @FXML
    public void DeleteDeck(){
        if (currentDeckId!=null){
            try {
                gn.removeDeck(currentDeckId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification ");
                alert.setHeaderText(null);
                alert.setContentText("Current deck has been deleted");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

}
