package Vue;

import Builder.FieldNotFoundException;
import Gestionnaire.GestionnaireNote;
import Structure.Deck;
import Structure.Note;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class VueApprentissage {

    @FXML
    private Label score;

    private GestionnaireNote gestNotes;
    private Note currentNote;
    private Deck currentDeck;
    private boolean onVerso = false;
    private boolean noUpdateForStudy = false;

    @FXML
    private VBox noteVBOX;

    @FXML
    private ListView<String> listView;

    @FXML
    private CheckBox multipleStudies;

    private ObservableList<String> obs = FXCollections.observableArrayList();
    private ObservableList<String> obsDeck = FXCollections.observableArrayList();

    private VueApprentissage(GestionnaireNote gestNotes) {
        this.gestNotes = gestNotes;
    }

    public static Node createVue(GestionnaireNote gestNotes){
        FXMLLoader fxmlLoader = new FXMLLoader(VueApprentissage.class.getClassLoader().getResource("apprentissage.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new VueApprentissage(gestNotes));

        Node n = null;
        try {
            n = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return n;
    }

    @FXML
    public void initialize() {
        initializeList();
    }

    private void initializeList()
    {
        updateObs();
        gestNotes.getObsNotes().addListener(new MapChangeListener<Integer,Note>() {

            @Override
            public void onChanged(Change<? extends Integer, ? extends Note> change) {
                updateObs();
            }
        });

        gestNotes.getObsDeck().addListener(new MapChangeListener<String,Deck>() {

            @Override
            public void onChanged(Change<? extends String, ? extends Deck> change) {
                updateObs();
            }
        });


        listView.setItems(obs);
        listView.getSelectionModel().selectedIndexProperty().addListener(
                (old,o,n)-> {
                    if (n.intValue() > -1) {
                        currentDeck = gestNotes.getObsDeck().get(obsDeck.get(n.intValue()));
                    } else {
                        currentDeck=null;
                    }
                }
        );
    }

    private void updateObs() {
        obs.clear();
        obsDeck.clear();
        for (Deck d : gestNotes.getObsDeck().values()) {
            obs.add(d.getName() + " : " + Math.round(d.getStudyDone()));
            obsDeck.add(d.getName());
        }
    }

    public void loadNoteRecto() {
        onVerso=false;
        updateScore();
        this.noteVBOX.getChildren().clear();
        try {
            this.noteVBOX.getChildren().addAll(Builder.VueNoteBuilder.getFront(currentNote));
        }
        catch (FieldNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadNoteVerso() {
        onVerso=true;
        updateScore();
        this.noteVBOX.getChildren().clear();
        try {
            this.noteVBOX.getChildren().addAll(Builder.VueNoteBuilder.getBack(currentNote));
        }
        catch (FieldNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void afficherVerso() {
        if (currentNote != null) {
            loadNoteVerso();
        }
    }

    @FXML
    public void aRevoir() throws SQLException {
        if (onVerso && currentNote != null) {
            nextNote(-20);
        }
    }

    @FXML
    public void correct() throws SQLException {
        if (onVerso && currentNote != null) {
            nextNote(5);

        }
    }

    @FXML
    public void facile() throws SQLException {
        if (onVerso && currentNote != null) {
            nextNote(20);
        }
    }

    private void nextNote(int coef) throws SQLException {
        updateScore();
        if (!noUpdateForStudy && coef != 0 && currentNote != null) {
            currentNote.updateKnowledgeRate(coef);
            gestNotes.updateNoteName_Deck_rate(currentNote);
        }
        if (currentDeck.isStudied()) {
            updateScore();
            etudeFinie();
        }
        else  {
            this.currentNote=currentDeck.getNextNote();
            loadNoteRecto();
        }
    }

    private void etudeFinie() {
        currentNote=null;
        onVerso=false;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification ");
        alert.setHeaderText(null);
        alert.setContentText("You've studied all cards in the deck.");

        alert.showAndWait();
        noteVBOX.getChildren().clear();
        updateObs();
    }

    private void updateScore() {
        if (currentNote != null) score.setText(""+Math.round(currentNote.getEffectiveKnowledgeRate())+"%");
    }

    @FXML
    public void startStudy() throws SQLException {
        if (currentDeck != null) {
            boolean today = currentDeck.studiedToday();
            noUpdateForStudy = multipleStudies.isSelected();
            if (today && !noUpdateForStudy){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification ");
                alert.setHeaderText(null);
                alert.setContentText("You've already studied this today");
                alert.showAndWait();
            }
            else {
                currentDeck.beginStudy();
                this.nextNote(0);
            }
        }
    }

}
